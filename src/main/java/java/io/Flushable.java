package java.io;

/**
 *  <tt>Flushable</tt> 是指目标数据可以被刷新. 调用flush方法将任何缓冲输出写入底层流.
 *
 * @since 1.5
 */
public interface Flushable {

    /**
     * 通过将任何缓冲输出写入底层流来刷新此流.
     *
     * @throws IOException 发生 I/O 异常
     */
    void flush() throws IOException;
}
