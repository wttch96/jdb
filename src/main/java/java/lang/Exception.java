package java.lang;

/**
 * {@code Exception} 类和它的子类是 {@code Throwable} 的一种形式.
 * 表示合理的应用程序可能想要捕获的条件.
 *
 * <p>{@code Exception} 和它的任何子类但不是 {@link RuntimeException} 类
 * 都是<em>受检异常</em>. 受检异常可以通过执行方法或构造函数抛出并在方法或构造函数
 * 边界外传播, 则需要在方法或构造函数的 throws 子句中声明受检异常.
 *
 * @author Frank Yellin
 * @jls 11.2 编译时受检异常
 * @see java.lang.Error
 * @since JDK1.0
 */
public class Exception extends Throwable {
    static final long serialVersionUID = -3387516993124229948L;

    /**
     * 构造一个新的异常使用 {@code null} 作为它的详细信息. 原因不会被初始化,
     * 随后可以通过调用 {@link #initCause(Throwable)} 来初始化原因.
     */
    public Exception() {
        super();
    }

    /**
     * 使用指定的信息来构造新的异常. 原因不会被初始化, 随后可以通过调用
     * {@link #initCause(Throwable)} 来初始化原因.
     *
     * @param message 详细信息. 详细信息被保存并且可以通过调用 {@link #getMessage()} 方法恢复.
     */
    public Exception(String message) {
        super(message);
    }

    /**
     * 使用指定的详细信息和原因构造新的异常. <p>注意与 {@code cause} 相关的信息 <em>不会</em>
     * 自动合并到此异常的详细信息中.
     *
     * @param message 详细信息(详细信息被保存并且可以通过调用 {@link #getMessage()} 方法恢复.)
     * @param cause   原因(原因被保存并且稍后可以通过调用 {@link #getCause()} 方法恢复).
     *                (允许 <tt>null</tt> 值, 表示原因不存在或者未知.)
     * @since 1.4
     */
    public Exception(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个新的 Throwable 使用指定的原因和详细消息
     * ({@code (cause==null ? null : cause.toString())}
     * (通常包含 {@code cause} 的类和详细消息)).
     * 这个构造函数对 throwables 包装其他 throwables 是有用的.
     * (例如, {@link java.security.PrivilegedActionException}).
     *
     * <p>调用 {@link #fillInStackTrace()} 方法来初始化新创建的 Throwable 中的堆栈跟踪数据.
     *
     * @param cause 原因 (将被保存, 稍候可以通过 {@link #getCause()} 方法恢复).
     *              (允许 {@code null} 值, 表示原因不存在或未知.)
     * @since 1.4
     */
    public Exception(Throwable cause) {
        super(cause);
    }

    /**
     * 使用指定的详细信息, 原因, 是否启用抑制，堆栈跟踪是否开启来构造新的异常.
     *
     * @param message            详细信息.
     * @param cause              原因.  (允许 {@code null} 值, 表示原因不存在或者未知.)
     * @param enableSuppression  是否启用异常抑制
     * @param writableStackTrace 堆栈跟踪是否可写
     * @since 1.7
     */
    protected Exception(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
