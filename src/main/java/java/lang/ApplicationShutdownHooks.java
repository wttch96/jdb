package java.lang;

import java.util.*;

/**
 * 用于跟踪和运行通过 <tt>{@link Runtime#addShutdownHook(Thread)
 * Runtime.addShutdownHook}</tt> 注册的用户级别关闭钩子的类.
 *
 * @see java.lang.Runtime#addShutdownHook(Thread)
 * @see java.lang.Runtime#removeShutdownHook(Thread)
 */
class ApplicationShutdownHooks {
    /* 注册的钩子集合 */
    private static IdentityHashMap<Thread, Thread> hooks;

    static {
        try {
            Shutdown.add(1 /* shutdown hook 调用顺序 */,
                    false /* 如果进程正在关闭, 则不注册 */,
                    ApplicationShutdownHooks::runHooks
            );
            hooks = new IdentityHashMap<>();
        } catch (IllegalStateException e) {
            // 当进程正在关闭的时候, 程序关闭钩子不能被添加
            hooks = null;
        }
    }


    private ApplicationShutdownHooks() {
    }

    /*
     * 添加一个新的关闭钩子. 检查关闭状态和钩子自身, 但是不做任何安全检查.
     */
    static synchronized void add(Thread hook) {
        if (hooks == null)
            // 进程正在关闭
            throw new IllegalStateException("Shutdown in progress");

        if (hook.isAlive())
            // 钩子已经被添加, 并且正在运行
            throw new IllegalArgumentException("Hook already running");

        if (hooks.containsKey(hook))
            // 钩子已经存在了
            throw new IllegalArgumentException("Hook previously registered");

        hooks.put(hook, hook);
    }

    /* Remove a previously-registered hook. 像 add 方法一样, 它也不做安全检查.
     * 移除之前注册的钩子.
     */
    static synchronized boolean remove(Thread hook) {
        if (hooks == null)
            // 正在关闭
            throw new IllegalStateException("Shutdown in progress");

        if (hook == null)
            // 空钩子
            throw new NullPointerException();

        return hooks.remove(hook) != null;
    }

    /*
     * 迭代所有的程序钩子, 为它们创建一个线程来运行它们.
     * 钩子们并行运行, 并且这个方法等待它们运行结束.
     */
    static void runHooks() {
        Collection<Thread> threads;
        synchronized (ApplicationShutdownHooks.class) {
            threads = hooks.keySet();
            hooks = null;
        }

        for (Thread hook : threads) {
            hook.start();
        }
        for (Thread hook : threads) {
            while (true) {
                try {
                    hook.join();
                    break;
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
