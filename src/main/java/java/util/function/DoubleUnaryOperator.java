package java.util.function;

import java.util.Objects;

/**
 * 表示对单个{@code double}值操作数的运算, 该操作数产生一个{@code double}值的结果。这是 {@link UnaryOperator}对于{@code double}的原始类型特化.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #applyAsDouble(double)}.
 *
 * @see UnaryOperator
 * @since 1.8
 */
@FunctionalInterface
public interface DoubleUnaryOperator {

    /**
     * Applies this operator to the given operand.
     *
     * @param operand the operand
     * @return the operator result
     */
    double applyAsDouble(double operand);

    /**
     * Returns a composed operator that first applies the {@code before}
     * operator to its input, and then applies this operator to the result.
     * If evaluation of either operator throws an exception, it is relayed to
     * the caller of the composed operator.
     *
     * @param before the operator to apply before this operator is applied
     * @return a composed operator that first applies the {@code before}
     * operator and then applies this operator
     * @throws NullPointerException if before is null
     * @see #andThen(DoubleUnaryOperator)
     */
    default DoubleUnaryOperator compose(DoubleUnaryOperator before) {
        Objects.requireNonNull(before);
        return (double v) -> applyAsDouble(before.applyAsDouble(v));
    }

    /**
     * Returns a composed operator that first applies this operator to
     * its input, and then applies the {@code after} operator to the result.
     * If evaluation of either operator throws an exception, it is relayed to
     * the caller of the composed operator.
     *
     * @param after the operator to apply after this operator is applied
     * @return a composed operator that first applies this operator and then
     * applies the {@code after} operator
     * @throws NullPointerException if after is null
     * @see #compose(DoubleUnaryOperator)
     */
    default DoubleUnaryOperator andThen(DoubleUnaryOperator after) {
        Objects.requireNonNull(after);
        return (double t) -> after.applyAsDouble(applyAsDouble(t));
    }

    /**
     * Returns a unary operator that always returns its input argument.
     *
     * @return a unary operator that always returns its input argument
     */
    static DoubleUnaryOperator identity() {
        return t -> t;
    }
}
