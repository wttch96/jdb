package java.lang.ref;


/**
 * 虚引用对象, 在收集者确定他们的指示物可能以其他方式被收回之后排队.
 * 虚引用最常用于以比 Java 终结机制更灵活的方式调度预先清理操作.
 *
 * <p> 如果垃圾收集器在某个时间点确定虚引用的引用是
 * <a href="package-summary.html#reachability">phantom 可达</a>,
 * 那么在那时或稍后它将入队引用.
 *
 * <p> 为了确保可回收对象保持不变, 可能无法检索虚引用的引用;
 * 虚引用的 <code>get</code> 方法始终返回 <code>null</code>.
 *
 * <p> 与软引用和弱引用不同, 垃圾收集器在排队时不会自动清除虚引用.
 * 通过虚引用可访问的对象将保持不变, 直到所有此类引用都被清除或自身无法访问.
 * <p>
 * 虚引用 (PhantomReference), 这是一个最虚幻的引用类型.无论是从哪里都无法
 * 再次返回被虚引用所引用的对象. 虚引用在系统垃圾回收器开始回收对象时,
 * 将直接调用 finalize() 方法, 但不会立即将其加入回收队列. 只有在真正
 * 对象被 GC 清除时, 才会将其加入 Reference 队列中去.
 *
 * @author Mark Reinhold
 * @since 1.2
 */

public class PhantomReference<T> extends Reference<T> {

    /**
     * 返回此引用对象的引用对象. 由于虚引用的引用始终不可访问,
     * 因此此方法始终返回 <code>null</code>.
     *
     * @return <code>null</code>
     */
    public T get() {
        return null;
    }

    /**
     * 使用给定的引用对象和引用队列创建一个虚引用.
     *
     * <p>可以使用 <tt>null</tt>队列创建一个虚引用, 但这样的引用完全没用：
     * 它的 <tt>get</tt>方法将始终返回 null，因为它没有队列，它永远不会排队.
     *
     * @param referent 新虚引用将引用的对象
     * @param q        要注册引用的队列, 如果不需要注册, 则 <tt>null</tt>
     */
    public PhantomReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
    }

}
