package java.lang.ref;

import sun.misc.Cleaner;
import sun.misc.JavaLangRefAccess;
import sun.misc.SharedSecrets;

/**
 * 引用对象的抽象基类. 这个类定义了所有引用类型的常见操作. 因为引用对象是和垃圾收集器紧密合作实现的,
 * 所以这个类不能被直接子例化.
 *
 * @author Mark Reinhold
 * @since 1.2
 */

public abstract class Reference<T> {

    /* 引用实例处于四种可能的内部状态之一:
     *
     *     Active: 服从于垃圾处理器的特殊处理. 有时在收集器检测到所指对象的
     *     可访问性已更改为适当的状态一段时间后, 收集器改变实例的状态为 Pending
     *     或者 Inactive 状态, 这取决于创建该实例时是否在队列中注册了该实例. 在前
     *     一种情况下, 它还将实例添加到挂起引用列表. 新创建的实例是 Active 的.
     *
     *     Pending: 一个挂起引用列表中等待进入引用处理线程的元素.
     *     未注册的实例永远不会处于这个状态.
     *
     *     Enqueued: 一个队列里面的在创建时就被注册的实例元素. 当一个实例从它的
     *     引用队列中删除时, 它变为 Inactive. 未注册的实例永远不会处于这个状态.
     *
     *     Inactive: 不会做更多的事情. 一旦实例变为 Inactive, 它的状态将不再更改.
     *
     * 状态像下面这样被编码到 queue 和 next 字段：
     *
     *     Active: 实例注册时 queue = ReferenceQueue , 或者如果实例没有被注册
     *     ReferenceQueue.NULL; next = null .
     *
     *     Pending: 实例注册时 queue = ReferenceQueue; next = this
     *
     *     Enqueued: queue = ReferenceQueue.ENQUEUED; next = queue 中的下一个实例,
     *     如果实例是队列最后一个元素.
     *
     *     Inactive: queue = ReferenceQueue.NULL; next = this.
     *
     * 有了这些约束, 收集器为了确定一个引用对象是否需要特别对待只需要检查next字段;
     * 如果next字段是null这个实例就是活跃的; 如果不为null, 这收集器就应该正常对待
     * 这个实例了.
     *
     * 为了保证并发收集器能发现活跃的引用对象而不干涉可能在这些对象上调用 queue()
     * 方法的线程收集器应该通过已发现的字段链接已发现的对象. 发现的对象还应该用于
     * 链接挂起列表中的引用对象.
     */

    private T referent;         /* 被 GC 特别对待 */

    volatile ReferenceQueue<? super T> queue; /* 引用队列 */

    /* 下一个引用实例
     * 当 active 时:   NULL
     *   pending 时:   this
     *  Enqueued 时:   队列中的下一个引用 (如果是最后一个元素则是自身)
     *  Inactive 时:   this
     */
    @SuppressWarnings("rawtypes")
    volatile Reference next;

    /* 被发现的引用
     * 当 active 时:   GC维护的被发现的引用列表中的下一个 (如果是最后一个元素则是自身)
     *   pending 时:   挂起列表中的下一个元素 (如果是最后一个元素则是本身)
     *   其他情况时:   NULL
     */
    transient private Reference<T> discovered;  /* 被 VM 使用 */


    /* 对象常常是和垃圾收集器同步的.  收集器在每一个收集周期开始时必须获取这个锁.
     * 因此至关重要的是任何持有这个锁的代码必须尽快完成, 不必分配新的对象, 避免
     * 调用这个锁.
     */
    static private class Lock {
    }

    /**
     * 锁对象
     */
    private static Lock lock = new Lock();


    /* 等待排队的引用列表. 当引用处理线程移除引用, 收集器就把它加到这个列表.
     * 这个列表被上边的锁对象保护. 这个列表用于发现的字段链接到它的元素.
     */
    private static Reference<Object> pending = null;

    /* 排队挂起引用的高优先级线程
     */
    private static class ReferenceHandler extends Thread {

        /**
         * 确保类已经初始化
         *
         * @param clazz 要确保的类
         */
        private static void ensureClassInitialized(Class<?> clazz) {
            try {
                // 查看类是否已经初始化
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
            } catch (ClassNotFoundException e) {
                // 找不到类, 说明类未实例化
                throw (Error) new NoClassDefFoundError(e.getMessage()).initCause(e);
            }
        }

        static {
            // 预加载并初始化 InterruptedException 和 Cleaner 类, 这样, 如果
            // 延迟加载/初始化它们时出现内存不足, 我们就不会在以后的运行循环
            // 中遇到麻烦.
            ensureClassInitialized(InterruptedException.class);
            ensureClassInitialized(Cleaner.class);
        }

        ReferenceHandler(ThreadGroup g, String name) {
            super(g, name);
        }

        public void run() {
            while (true) {
                tryHandlePending(true);
            }
        }
    }

    /**
     * 如果有挂起的 {@link Reference}, 尝试处理它.<p>
     * 可能有另外一个 {@link Reference} 挂起返回 {@code true} 作为一个提示
     * 或者当没有更多的挂起的 {@link Reference}时返回 {@code false} 这样程序
     * 就可以做别的事情而不是循环.
     *
     * @param waitForNotify 如果 {@code true} 并且没有挂起的 {@link Reference},
     *                      等待直到被 VM 唤起或者中断; 如果 {@code false},当
     *                      没用挂起的 {@link Reference}时立即返回.
     * @return {@code true} 如果有一个 {@link Reference} 挂起状态并且它正在被处理,
     * 或者任意一个等到唤起的通知, 或者线程在通知之前被中断;
     * 其他情况返回 {@code false}.
     */
    static boolean tryHandlePending(boolean waitForNotify) {
        Reference<Object> r;
        Cleaner c;
        try {
            // 加锁
            synchronized (lock) {
                // 有一个挂起状态的 Reference
                if (pending != null) {
                    r = pending;
                    // 'instanceof' 有时可能抛出 OutOfMemoryError
                    // 所以在此之前先解除 'r' 到 'pending' 的链接...
                    c = r instanceof Cleaner ? (Cleaner) r : null;
                    // 解除 'r' 到 'pending' 的链接
                    pending = r.discovered;
                    r.discovered = null;
                } else {
                    // 等待锁可能会导致 OutOfMemoryError 错误
                    // 因为它试图分配异常对象.
                    if (waitForNotify) {
                        lock.wait();
                    }
                    // 如果等待就重试
                    return waitForNotify;
                }
            }
        } catch (OutOfMemoryError x) {
            // 给其他线程 CPU 时间, 这样它们就有希望删除一些活动引用, 而 GC 会回收一些空间.
            // 还可以防止CPU密集型旋转, 以防上面的'r instanceof Cleaner'
            // 持续抛出 OOME 一段时间......
            Thread.yield();
            // 重试
            return true;
        } catch (InterruptedException x) {
            // 重试
            return true;
        }

        // cleaners 的快速通道
        if (c != null) {
            c.clean();
            return true;
        }

        ReferenceQueue<? super Object> q = r.queue;
        if (q != ReferenceQueue.NULL) q.enqueue(r);
        return true;
    }

    static {
        // 当前线程组
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        for (ThreadGroup tgn = tg;
             tgn != null;
             tg = tgn, tgn = tg.getParent())
            ;
        // 引用处理线程
        Thread handler = new ReferenceHandler(tg, "Reference Handler");
        /*
         * 如果有一个特殊的系统优先级大于 MAX_PRIORITY , 那么它将在这里使用
         */
        handler.setPriority(Thread.MAX_PRIORITY);
        handler.setDaemon(true);
        handler.start();

        // 在SharedSecrets中提供访问权限
        SharedSecrets.setJavaLangRefAccess(new JavaLangRefAccess() {
            @Override
            public boolean tryHandlePendingReference() {
                return tryHandlePending(false);
            }
        });
    }

    /* -- Referent accessor and setters -- */

    /**
     * 返回此引用对象的引用对象. 如果已通过程序或垃圾收集器清除此引用对象,
     * 则此方法返回<code> null </code>.
     *
     * @return 此引用引用的对象, 如果已清除此引用对象, 则为<code> null </code>
     */
    public T get() {
        return this.referent;
    }

    /**
     * 清除此引用对象. 调用此方法不会导致此对象入队.
     *
     * <p> 此方法仅由Java代码调用; 当垃圾收集器对引用进行排队时,
     * 它会直接执行, 而不会调用此方法.
     */
    public void clear() {
        this.referent = null;
    }


    /* -- Queue operations -- */

    /**
     * 通过程序或垃圾收集器判断此引用对象是否已入队.
     * 如果此引用对象在创建时未在队列中注册,
     * 则此方法将始终返回 <code>false</code>.
     *
     * @return <code>true</code> 如果引用已经入队
     */
    public boolean isEnqueued() {
        return (this.queue == ReferenceQueue.ENQUEUED);
    }

    /**
     * 将此引用对象添加到与其注册的队列(如果有).
     *
     * <p> 此方法仅由Java代码调用; 当垃圾收集器对引用进行排队时,
     * 它会直接执行, 而不会调用此方法.
     *
     * @return <code>true</code> 如果引用对象成功入队;
     * <code>false</code> 如果它已经入队或者在创建时没有在队列中注册
     */
    public boolean enqueue() {
        return this.queue.enqueue(this);
    }


    /* -- Constructors -- */

    Reference(T referent) {
        this(referent, null);
    }

    Reference(T referent, ReferenceQueue<? super T> queue) {
        this.referent = referent;
        this.queue = (queue == null) ? ReferenceQueue.NULL : queue;
    }

}
