package java.lang;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

/**
 * 实现这个接口允许对象成为 "for-each loop" 语法的目标. 参见
 * <strong>
 * <a href="{@docRoot}/../technotes/guides/language/foreach.html">For-each Loop</a>
 * </strong>
 *
 * @param <T> 迭代器将会返回的元素的类型
 * @jls 14.14.2 The enhanced for statement
 * @since 1.5
 */
public interface Iterable<T> {
    /**
     * 返回 {@code T} 类型元素的迭代器.
     *
     * @return 一个迭代器.
     */
    Iterator<T> iterator();

    /**
     * 对 {@code Iterable} 的每个元素执行给定操作, 知道处理完所有元素或者操作引发异常.
     * 除非实现类另有指定, 否则操作按迭代顺序执行(如果指定了迭代顺序). 操作抛出的异常会
     * 转发给调用者.
     *
     * @param action 要为每个元素执行的操作
     * @throws NullPointerException 指定异常为空
     * @implSpec <p>默认的实现行为是:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     * @since 1.8
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * 在 {@code Iterable} 描述的元素上创建 {@link Spliterator}.
     *
     * @return 关于此 {@code Iterable} 描述的元素的 {@code Spliterator}.
     * @implSpec 默认实现是 <em><a href="Spliterator.html#binding">early-binding</a></em>
     * 来自迭代器 {@code Iterator} 的分裂器. 分裂器继承了迭代器的 <em>fail-fast</em> 属性.
     * @implNote 通常应该覆盖默认实现. 该默认实现返回的 Spliterator 分裂具有较差的分割能力, 未分级,
     * 并且不报告任何分裂器特征. 实现类几乎总能提供更好的实现.
     * @since 1.8
     */
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
