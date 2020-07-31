package java.util.function;

/***代表{@code double}值结果的供应者. 这是{@link Supplier}对 {@code double}产生的原始类型特例化.
 * <p>There is no requirement that a distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #getAsDouble()}.
 *
 * @see Supplier
 * @since 1.8
 */
@FunctionalInterface
public interface DoubleSupplier {

    /**
     * Gets a result.
     *
     * @return a result
     */
    double getAsDouble();
}
