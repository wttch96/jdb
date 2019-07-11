package java.lang.instrument;

/**
 * 无法修改其中一个指定的类时, 由 {@link java.lang.instrument.Instrumentation#redefineClasses(ClassDefinition...)}
 * 的实现抛出.
 *
 * @see java.lang.instrument.Instrumentation#redefineClasses
 * @since 1.5
 */
public class UnmodifiableClassException extends Exception {
    private static final long serialVersionUID = 1716652643585309178L;

    /**
     * 构建一个 <code>UnmodifiableClassException</code> 不包含任何详细信息.
     */
    public UnmodifiableClassException() {
        super();
    }

    /**
     * 构建一个 <code>UnmodifiableClassException</code> 包含指定的详细信息.
     *
     * @param s 详细信息
     */
    public UnmodifiableClassException(String s) {
        super(s);
    }
}
