package java.lang;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * <tt>CharSequence</tt> 是一个 <code>char</code> 值的可读序列.
 * 此接口提供对许多不同类型的 <code>char</code> 序列的统一/只读的访问.
 * <code>char</code> 值表示 <i>基本多文种平面(BMP)</i> 中的字符或代理项.
 * 到 <a href="Character.html#unicode">Unicode 字符表示</a> 查看详情.
 *
 * <p> 这个接口不会细化 {@link java.lang.Object#equals(java.lang.Object) equals}
 * 和 {@link java.lang.Object#hashCode() hashCode} 方法的常规协定.
 * 因此, 比较实现 <tt>CharSequence</tt> 的两个对象的结果通常是未定义的.
 * 每个对象可以由不同的类实现, 并且不能保证每个类能够测试其实例与另一个实例是否相等.
 * 因此, 将任意 <tt>CharSequence</tt> 实例用作集合中的元素或映射中的键都是不合适的.
 *
 * @author Mike McCloskey
 * @spec JSR-51
 * @since 1.4
 */

public interface CharSequence {

    /**
     * 返回这个字符序列的长度. 长度是序列中 16 位 <code>char</code> 的数量.
     *
     * @return 序列中 <code>char</code> 的数量.
     */
    int length();

    /**
     * 返回指定索引处的 <code>char</code> 的值. 索引范围从 0 到 <tt>length() - 1</tt>.
     * 第一个 <code>char</code> 值在序列的索引 0 处, 下一个在 1 处, 以此类推, 作为数组索引.
     *
     * <p>如果指定索引处的 <cod>char</cod> 值是一个
     * <a href="{@docRoot}/java/lang/Character.html#unicode">代理</a>, 代理的值将被返回.
     *
     * @param index index 索引处的 <code>char</code> 值将被返回
     * @return 指定的 <code>char</code> 值
     * @throws IndexOutOfBoundsException 如果索引参数 <tt>index</tt> 为负或者不小于 <tt>length()</tt>
     */
    char charAt(int index);

    /**
     * 返回这个序列的一个 <code>CharSequence</code> 子序列. 子序列
     * 子序列一指定索引处 <code>char</code> 值开始, 并以索引 <code>end - 1</code>
     * 处的 <code>char</code> 值结束. 返回序列的长度(在 <code>char</code> 中)是
     * <code>end - start</code>, 因此如果是 <tt>start == end</tt>, 则返回一个控序列.
     *
     * @param start 开始索引, 包含
     * @param end   结束索引, 不包含
     * @return 指定的子序列
     * @throws IndexOutOfBoundsException <tt>start</tt> 或者 <tt>end</tt> 为负数,
     *                                   <tt>end</tt> 大于 <tt>length()</tt>,
     *                                   或者 <tt>start</tt> 大于 <tt>end</tt>
     */
    CharSequence subSequence(int start, int end);

    /**
     * 以与此序列相同的顺序返回包含此序列中字符的字符串. 字符串的长度将是该序列的长度.
     *
     * @return 一个字符串, 由这个字符序列组成
     */
    public String toString();

    /**
     * 返回 {@code int} 的流, 对此序列中的 {@code char} 值进行零扩展.
     *
     * <p>如果在读取流时序列被突变, 则结果是未定义的.
     *
     * @return 来自此序列的 char 值的 IntStream
     * @since 1.8
     */
    public default IntStream chars() {
        class CharIterator implements PrimitiveIterator.OfInt {
            int cur = 0;

            public boolean hasNext() {
                return cur < length();
            }

            public int nextInt() {
                if (hasNext()) {
                    return charAt(cur++);
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void forEachRemaining(IntConsumer block) {
                for (; cur < length(); cur++) {
                    block.accept(charAt(cur));
                }
            }
        }

        return StreamSupport.intStream(() ->
                        Spliterators.spliterator(
                                new CharIterator(),
                                length(),
                                Spliterator.ORDERED),
                Spliterator.SUBSIZED | Spliterator.SIZED | Spliterator.ORDERED,
                false);
    }

    /**
     * 返回此序列中的代码点值流. 序列中遇到的任何代理对都被组合,
     * 就像 {@linkplain Character#toCodePoint(char, char)} 一样, 结果传递给流.
     * 任何其他代码单元(包括普通 BMP 字符, 未配对代理和未定义代码单元) 都将零扩展为 {@code int} 值,
     * 然后传递给流.
     *
     * <p>如果在读取流时序列被突变, 则结果是未定义的.
     *
     * @return Unicode 代码的 IntStream 指向此序列
     * @since 1.8
     */
    public default IntStream codePoints() {
        class CodePointIterator implements PrimitiveIterator.OfInt {
            int cur = 0;

            @Override
            public void forEachRemaining(IntConsumer block) {
                final int length = length();
                int i = cur;
                try {
                    while (i < length) {
                        char c1 = charAt(i++);
                        // 未使用代理
                        if (!Character.isHighSurrogate(c1) || i >= length) {
                            block.accept(c1);
                        } else {
                            // 高位代理, 如果下一位是低位代理, 将两个 char 转为 codePoint
                            char c2 = charAt(i);
                            if (Character.isLowSurrogate(c2)) {
                                i++;
                                block.accept(Character.toCodePoint(c1, c2));
                            } else {
                                block.accept(c1);
                            }
                        }
                    }
                } finally {
                    cur = i;
                }
            }

            public boolean hasNext() {
                return cur < length();
            }

            public int nextInt() {
                final int length = length();

                if (cur >= length) {
                    throw new NoSuchElementException();
                }
                char c1 = charAt(cur++);
                if (Character.isHighSurrogate(c1) && cur < length) {
                    // next codePoint
                    char c2 = charAt(cur);
                    if (Character.isLowSurrogate(c2)) {
                        cur++;
                        return Character.toCodePoint(c1, c2);
                    }
                }
                return c1;
            }
        }

        return StreamSupport.intStream(() ->
                        Spliterators.spliteratorUnknownSize(
                                new CodePointIterator(),
                                Spliterator.ORDERED),
                Spliterator.ORDERED,
                false);
    }
}
