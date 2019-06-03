package java.lang;

import java.io.Closeable;

/**
 * 一个对象可能持有资源(比如一个文件或是套接字的处理)直到它(这个对象)关闭. 在资源规范的头
 * 中定义的{@code AutoCloseable}对象当它从 {@code try}-with-resources 语法块中退出时,会自
 * 动调用 {@link #close()}方法. 这种结构确保了它可以及时的释放资源,避免了可能发生的资源耗
 * 尽引起的异常和错误.
 *
 * @author Josh Bloch
 * @apiNote <p>事实上,即使不是所有的子类或实例都具有可释放的资源,基类实现可自动关闭也是可能的,这很
 * 常见. 对于必须以完全的通用性操作的代码,或者当已知 {@code AutoCloseable} 实例需要释放资
 * 源时,建议在 {@code try}-with-resources 中构造资源. 但是,在使用支持基于 I/O 或者 非 I/O
 * 的形式,比如 {@link java.util.stream.Stream}的资源时,通常不需要 {@code try} 代码块.
 * @since 1.7
 */
public interface AutoCloseable {
    /**
     * 关闭这个资源,放弃任何基础的资源.当对象被 {@code try}-with-resources
     * 管理时这个方法会自动的执行.
     * <p>
     * 虽然这个接口方法在定义时抛出 {@code Exception}, 但是<em>强烈</em>建议这个方法的具体实现时
     * 在{@code close}方法中抛出更多的具体异常,或者不抛出任何异常当关闭操作不会失败的时候.
     * <p>
     * 实现者要特别注意关闭操作可能失败的情况. 强烈建议在抛出异常之前,放弃内部底层资源并在
     * 内部将资源<em>标记</em>为关闭的.{@code close} 方法不太可能被调用很多次,因此这样可以确保资源被及时释放.
     * 此外,它还减少资源包装(被另一个资源包装)时可能出现的问题.
     * <p><em>也强烈建议实现该接口的实现者让 {@code close} 方法不抛出 {@link InterruptedException}异常.</em>
     * <p>
     * 这个异常与线程的中断状态交互,如果 {@code InterruptedException}被{@linkplain Throwable#addSuppressed(Throwable)}抑制,
     * 运行时的不当行为很可能发生.
     * <p>更平常的,如果它会导致问题的异常被抑制,{@code AutoCloseable.close} 方法就不会抛出它.
     * <p>
     * 注意,不同于 {@link java.io.Closeable} 的 {@link Closeable#close()} 方法,
     * 这个 {@code close} 方法<em>不</em>要求是幂等的.换句话说,调用这个{@code close}方法多于一次会出现可见的影响,
     * 不像{@code Closeable.close} 被要求调用多次时时没有影响的.
     * <p>
     * 但是,这个接口的实现都应该保证 {@code close}方法具有幂等性.
     *
     * @throws Exception 如果这个资源无法关闭
     */
    void close() throws Exception;
}
