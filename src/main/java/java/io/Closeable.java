
package java.io;

/**
 * 一个{@code Closeable}对象可以关闭数据源或是目标. 当处理资源时可以调用
 * {@code close}方法关闭,比如处理打开的文件时.
 *
 * @since 1.5
 */
public interface Closeable extends AutoCloseable {

    /**
     * 关闭流并且释放系统相关的资源.如果流已经关闭,调用该方法则没有任何效果.
     *
     * <p> 要注意的是在 {@link AutoCloseable#close()}中, 关闭可能出现失败的情况.
     * 强烈建议在丢弃基础资源,<em>标记</em> {@code Closeable} 是关闭的时, 优先
     * 抛出 {@code IOException}.
     *
     * <p>即,如果抛出异常则没有完成标记关闭这一动作.
     *
     * @throws IOException 如果 I/O 发生错误.
     */
    @Override
    public void close() throws IOException;
}
