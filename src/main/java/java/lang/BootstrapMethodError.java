package java.lang;

/**
 * 抛出表示 {@code invokedynamic} 指令未能找到其引导方法, 或者引导方法未能为
 * {@linkplain java.lang.invoke.CallSite 调用点} 提供正确 {@linkplain java.lang.invoke.MethodHandle#type() 方法类型}
 * 的{@linkplain java.lang.invoke.CallSite＃getTarget target}.
 *
 * @author John Rose, JSR 292 EG
 * @since 1.7
 */
public class BootstrapMethodError extends LinkageError {
    private static final long serialVersionUID = 292L;

    /**
     * 构造一个没有详细消息的 {@code BootstrapMethodError}.
     */
    public BootstrapMethodError() {
        super();
    }

    /**
     * 使用指定的详细消息构造 {@code BootstrapMethodError}.
     *
     * @param s 详细消息
     */
    public BootstrapMethodError(String s) {
        super(s);
    }

    /**
     * 使用指定的详细消息和原因构造 {@code BootstrapMethodError}.
     *
     * @param s     详细消息.
     * @param cause 原因, 可以是 {@code null}.
     */
    public BootstrapMethodError(String s, Throwable cause) {
        super(s, cause);
    }

    /**
     * 使用指定的原因构造一个 {@code BootstrapMethodError}.
     *
     * @param cause 原因, 可以是 {@code null}.
     */
    public BootstrapMethodError(Throwable cause) {
        // cf. Throwable(Throwable cause) constructor.
        super(cause == null ? null : cause.toString());
        initCause(cause);
    }
}
