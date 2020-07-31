package java.util.function;

/**
 * 表示一个接受 double 参数并产生 long 结果的函数. 这是{@link Function}的{@code double}到{@code long} 原始类型专业化.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #applyAsLong(double)}.
 *
 * @see Function
 * @since 1.8
 */
@FunctionalInterface
public interface DoubleToLongFunction {

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    long applyAsLong(double value);
}
