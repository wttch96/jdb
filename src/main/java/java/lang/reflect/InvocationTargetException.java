package java.lang.reflect;

/**
 * InvocationTargetException 是一个已检查的异常, 它包装被调用的方法或构造函数抛出的异常.
 *
 * <p>截止 1.4, 这个异常已经改进过, 以符合通用异常链机制. 在构造是提供并通过
 * {@link #getTargetException()} 方法访问的 "target exception", 现在称为原因,
 * 并可以通过 {@link Throwable#getCause()} 以及前述的 "遗留方法" 来访问.
 *
 * @see Method
 * @see Constructor
 */
public class InvocationTargetException extends ReflectiveOperationException {
    /**
     * 使用 jdk 1.1.X 中的 serialVersionUID 实现互操作性
     */
    private static final long serialVersionUID = 4085088731926701167L;

    /**
     * 如果使用 InvocationTargetException(Throwable target) 构造函数来实例化对象, 则此字段保存 target
     *
     * @serial
     */
    private Throwable target;

    /**
     * 构造一个以 {@code null} 为目标异常的 {@code InvocationTargetException}.
     */
    protected InvocationTargetException() {
        super((Throwable) null);  //禁止 initCause
    }

    /**
     * 构造具有目标异常的 InvocationTargetException.
     *
     * @param target 目标异常
     */
    public InvocationTargetException(Throwable target) {
        super((Throwable) null);  // 禁止 initCause
        this.target = target;
    }

    /**
     * 构造具有目标异常和详细信息的 InvocationTargetException.
     *
     * @param target 目标异常
     * @param s      详细信息
     */
    public InvocationTargetException(Throwable target, String s) {
        super(s, null);  // 禁止 initCause
        this.target = target;
    }

    /**
     * 获得抛出的目标异常.
     *
     * <p>这种方法早于通用异常链机制. {@link Throwable#getCause()} 方法现在是获取此信息的首选方法.
     *
     * @return 抛出的目标异常(此异常的原因).
     */
    public Throwable getTargetException() {
        return target;
    }

    /**
     * 返回此异常的原因 (抛出的目标异常, 可以是 {@code null}).
     *
     * @return 异常的原因.
     * @since 1.4
     */
    public Throwable getCause() {
        return target;
    }
}
