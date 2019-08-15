package java.lang;

/**
 * {@code Error} 是表示一个合理的程序发生了严重错误并且不应该尝试捕获的 {@code Throwable} 子类.
 * 大多数错误是异常条件. {@code ThreadDeath} 错误, 虽然是"正常"条件, 但是也是 {@code Error} 的子类,
 * 因为大多数应用程序都不应该尝试捕获它.
 * <p>
 * 一个方法不需要在其 {@code throws} 子句中声明在执行期间可能抛出但未捕获的 {@code Error} 和
 * 它的任何子类, 因为这些错误是永远不应发生的异常情况.
 * <p>
 * 这就是, {@code Error} 和它的子类被视为不受检异常, 用于编译时异常检查.
 *
 * @author Frank Yellin
 * @jls 11.2 编译时异常检查
 * @see java.lang.ThreadDeath
 * @since JDK1.0
 */
public class Error extends Throwable {
    static final long serialVersionUID = 4980196508277280342L;

    /**
     * 使用 {@code null} 作为详细信息构造新的 Error. 原因未被初始化, 稍候可以通过调用
     * {@link #initCause(Throwable)} 来初始化原因.
     */
    public Error() {
        super();
    }

    /**
     * 使用指定的信息构造新的 Error, 原因未被初始化, 稍候可以通过调用 {@link #initCause(Throwable)}
     * 方法来初始化原因.
     *
     * @param message 详细消息. 详细消息被保存, 稍候可以通过 {@link #getMessage()} 方法恢复.
     */
    public Error(String message) {
        super(message);
    }

    /**
     * 使用指定的信息和原因构造新的 Error. <p>注意 {@code cause} 相关联的详细信息 <em>不会</em>
     * 自动合并到这个 Error 的详细信息中去.
     *
     * @param message 详细消息. 详细消息被保存, 稍候可以通过 {@link #getMessage()} 方法恢复.
     * @param cause   原因 (将被保存, 稍候可以通过 {@link #getCause()} 方法恢复).
     *                (允许 {@code null} 值, 表示原因不存在或未知.)
     * @since 1.4
     */
    public Error(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个新的 Error 使用指定的原因和详细消息
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
    public Error(Throwable cause) {
        super(cause);
    }


    /**
     * 使用指定的详细信息, 原因, 是否启用抑制，堆栈跟踪是否开启来构造新的 Error.
     *
     * @param message            详细信息.
     * @param cause              原因.  (允许 {@code null} 值, 表示原因不存在或者未知.)
     * @param enableSuppression  是否启用异常抑制
     * @param writableStackTrace 堆栈跟踪是否可写
     * @since 1.7
     */
    protected Error(String message, Throwable cause,
                    boolean enableSuppression,
                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
