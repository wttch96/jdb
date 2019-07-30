package java.lang;

import java.io.IOException;

/**
 * 可以追加 <tt>char</tt> 序列和值的对象. <tt>Appendable</tt> 接口必须由其实例旨在
 * 接受来自 {@link java.util.Formatter} 的格式化输出的任何类来实现.
 *
 * <p> 要附加的字符应为有效的 Unicode 字符, 如
 * <a href="Character.html#unicode">Unicode 字符表示</a> 中所述.
 * 注意, 追加字符可以由多个16位 <tt>char</tt> 值组成.
 *
 * <p> 对于多线程访问, Appendable 不一定线程安全.
 * 线程安全是有扩展和实现此接口的类的责任.
 *
 * <p> 由于此接口可以由具有不同错误处理样式的现有类实现, 因此无法保证错误将传播给调用者.
 *
 * <p>wttch: 这个接口的所有方法都返回自身的一个引用, 那就是说,
 * Appendable 是可以链式调用的.
 *
 * @since 1.5
 */
public interface Appendable {

    /**
     * 追加指定的字符序列到这个 <tt>Appendable</tt>.
     *
     * <p> 根据哪个类实现字符序列 <tt>csq</tt>, 可能不会追加整个序列.
     * 例如, 如果 <tt>csq</tt> 是 {@link java.nio.CharBuffer}, 则追加的
     * 子序列由缓冲区的位置和限制来定义.
     *
     * @param csq 要追加的字符序列. 如果 <tt>csq</tt> 是 <tt>null</tt>,
     *            那么 <tt>"null"</tt> 四个字符将被追加到这个 Appendable 末尾.
     * @return 这个 <tt>Appendable</tt> 的一个引用
     * @throws IOException 如果发生 I/O 错误
     */
    Appendable append(CharSequence csq) throws IOException;

    /**
     * 添加指定字符序列的子序列到这个 <tt>Appendable</tt>.
     *
     * <p> 当 <tt>csq</tt>不是 <tt>null</tt> 时, 调用 <tt>out.append(csq, start, end)</tt>
     * 形如此方法, 行为完全相同的调用方式.
     *
     * <pre>
     *     out.append(csq.subSequence(start, end)) </pre>
     *
     * @param csq   将附加子序列的字符序列. 如果 <tt>csq</tt> 是 <tt>null</tt>,
     *              那么 <tt>csq</tt> 将包含四个字符 <tt>"null"</tt>.
     * @param start 子序列的第一个下标索引
     * @param end   子序列中最后一个字符后面的字符索引. wttch: [start, end)
     * @return 这个 <tt>Appendable</tt> 的一个引用
     * @throws IndexOutOfBoundsException If <tt>start</tt> or <tt>end</tt> are negative, <tt>start</tt>
     *                                   is greater than <tt>end</tt>, or <tt>end</tt> is greater than
     *                                   <tt>csq.length()</tt>
     * @throws IOException               如果发生 I/O 错误
     */
    Appendable append(CharSequence csq, int start, int end) throws IOException;

    /**
     * 追加指定的字符到这个 <tt>Appendable</tt>.
     *
     * @param c 要找追加的字符
     * @return 这个 <tt>Appendable</tt> 的一个引用
     * @throws IOException 如果发生 I/O 错误
     */
    Appendable append(char c) throws IOException;
}
