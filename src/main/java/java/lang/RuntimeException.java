package java.lang;

/**
 * {@code RuntimeException} 是所有在 JVM 的正常操作期间可以抛出的那些异常的超类.
 *
 * <p>{@code RuntimeException} 和它的子类是 <em>不受检异常</em>.
 * 不受检异常 <em>不</em> 需要在方法或构造函数的 {@code throws} 子句中声明, 如果它们可以通过
 * 执行方法或者构造函数抛出并在方法和构造函数边界外传播.
 *
 * @author Frank Yellin
 * @jls 11.2 编译时受检异常
 * @since JDK1.0
 */
public class RuntimeException extends Exception {
    static final long serialVersionUID = -7034897190745766939L;

    /**
     * 构造一个新的运行时异常使用 {@code null} 作为它的详细信息. 原因不会被初始化,
     * 随后可以通过调用 {@link #initCause(Throwable)} 来初始化原因.
     */
    public RuntimeException() {
        super();
    }

    /**
     * 使用指定的信息来构造新的运行时异常. 原因不会被初始化, 随后可以通过调用
     * {@link #initCause(Throwable)} 来初始化原因.
     *
     * @param message 详细信息. 详细信息被保存并且可以通过调用 {@link #getMessage()} 方法恢复.
     */
    public RuntimeException(String message) {
        super(message);
    }

    /**
     * 使用指定的详细信息和原因构造新的运行时异常. <p>注意与 {@code cause} 相关的信息 <em>不会</em>
     * 自动合并到此异常的详细信息中.
     *
     * @param message 详细信息(详细信息被保存并且可以通过调用 {@link #getMessage()} 方法恢复.)
     * @param cause   原因(原因被保存并且稍后可以通过调用 {@link #getCause()} 方法恢复).
     *                (允许 <tt>null</tt> 值, 表示原因不存在或者未知.)
     * @since 1.4
     */
    public RuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用指定的原因和详细消息构造一个新的运行时异常,
     * ({@code (cause==null ? null : cause.toString())}
     * (通常包含 {@code cause} 的类和详细消息)).
     * 这个构造函数对 throwables 包装其他 throwables 是有用的.
     * (例如, {@link java.security.PrivilegedActionException}).
     *
     * <p>调用 {@link #fillInStackTrace()} 方法来初始化新创建的运行时异常中的堆栈跟踪数据.
     *
     * @param cause 原因 (将被保存, 稍候可以通过 {@link #getCause()} 方法恢复).
     *              (允许 {@code null} 值, 表示原因不存在或未知.)
     * @since 1.4
     */
    public RuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * 使用指定的详细信息, 原因, 是否启用抑制，堆栈跟踪是否开启来构造新的运行时异常.
     *
     * @param message            详细信息.
     * @param cause              原因.  (允许 {@code null} 值, 表示原因不存在或者未知.)
     * @param enableSuppression  是否启用异常抑制
     * @param writableStackTrace 堆栈跟踪是否可写
     * @since 1.7
     */
    protected RuntimeException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
