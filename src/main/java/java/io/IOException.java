package java.io;

/**
 * 表示发生某种 I/O 异常的信号. 此类是由失败或中断的 I/O 操作产生的一般异常类.
 *
 * @author unascribed
 * @see java.io.InputStream
 * @see java.io.OutputStream
 * @since JDK1.0
 */
public
class IOException extends Exception {
    static final long serialVersionUID = 7818375828146090155L;

    /**
     * 使用 {@code null} 构造一个 {@code IOException} 作为其错误详细消息.
     */
    public IOException() {
        super();
    }

    /**
     * 使用指定的错误详细消息构造一个 {@code IOException} 异常.
     *
     * @param message 详细消息 (保存之后可以使用 {@link #getMessage()} 方法获得)
     */
    public IOException(String message) {
        super(message);
    }

    /**
     * 使用指定的消息和原因构造一个　{@code IOException} 异常.
     *
     * <p> 请注意, 与 {@code cause} 关联的详细消息 <i>不</i>会自动合并到此异常的详细信息中.
     *
     * @param message 详细消息 (保存之后可以使用 {@link #getMessage()} 方法获得)
     * @param cause   原因 (可以稍候使用 {@link #getCause()} 方法恢复).  (允许空值, 表示原因不存在或未知.)
     * @since 1.6
     */
    public IOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个　{@code IOException}, 其中包括　{@code (cause == null?null:cause.toString() } 的指定原因
     * 和详细消息(通常包含 {@code cause} 的类和详细消息). 次构造函数对于 IO 异常非常有用, 这些异常只是其他
     * Throwable 的包装器.
     *
     * @param cause 原因 (可以稍候使用 {@link #getCause()} 方法恢复).  (允许空值, 表示原因不存在或未知.)
     * @since 1.6
     */
    public IOException(Throwable cause) {
        super(cause);
    }
}
