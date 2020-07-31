package java.util.function;

/**
 * 表示一个函数, 该函数接受一个 double 参数并产生一个整数值的结果. 这是 {@link Function}的{@code double}到{@code int} 原始类型专业化.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #applyAsInt(double)}.
 *
 * @see Function
 * @since 1.8
 */
@FunctionalInterface
public interface DoubleToIntFunction {

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    int applyAsInt(double value);
}
