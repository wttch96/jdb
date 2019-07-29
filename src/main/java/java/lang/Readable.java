package java.lang;

import java.io.IOException;

/**
 * <tt>Readable</tt> 是一个字符来源. 来自 <tt>Readable</tt> 的字符通过
 * {@link java.nio.CharBuffer} 提供的 read 方法的调用者.
 *
 * @since 1.5
 */
public interface Readable {

    /**
     * 试图读取字符放入指定的字符缓冲区. 缓冲区按原样用作字符存储库:
     * 唯一变化是 put 操作的结果. 不执行缓冲器的翻转或者逆序.
     *
     * @param cb 用于读取字符的缓冲区
     * @return 添加到缓冲区的 {@code char} 值的数量,
     * 如果此字符源位于其末尾, 则为 -1.
     * @throws IOException                      if an I/O error occurs
     * @throws NullPointerException             if cb is null
     * @throws java.nio.ReadOnlyBufferException if cb is a read only buffer
     */
    public int read(java.nio.CharBuffer cb) throws IOException;
}
