package java.io;

/**
 * 当严重的 I/O 错误发生时抛出.
 *
 * @author Xueming Shen
 * @since 1.6
 */
public class IOError extends Error {
    /**
     * 使用指定的原因构造一个新的 IOError 实例. 使用 <tt>( cause == null ? null : cause.toString() )</tt>
     * (通常包含cause的类和详细消息) 的详细消息创建I OError.
     *
     * @param cause 导致此错误的原因, 如果原因未知, 则为 <tt>null</tt>
     */
    public IOError(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 67100927991680413L;
}
