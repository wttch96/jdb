package java.util.function;

import java.util.Objects;

/**
 * 表示一个接受两个参数并且没有返回值的操作. 这是 {@link Consumer} 的二元特殊化.
 * 和其他大多数函数式接口不同, {@code BiConsumer} 会产生预期的副作用.
 *
 * <p>这是一个<a href="package-summary.html">函数式接口</a> 函数值方法是 {@link #accept(Object, Object)}.
 *
 * @param <T> 该操作的第一个参数的类型
 * @param <U> 该操作的第二个参数的类型
 *
 * @see Consumer
 * @since 1.8
 */
@FunctionalInterface
public interface BiConsumer<T, U> {

    /**
     * 对给定参数执行此操作.
     *
     * @param t 第一个输入参数
     * @param u 第二个输入参数
     */
    void accept(T t, U u);

    /**
     * Returns a composed {@code BiConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code BiConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> after) {
        Objects.requireNonNull(after);

        return (l, r) -> {
            accept(l, r);
            after.accept(l, r);
        };
    }
}
