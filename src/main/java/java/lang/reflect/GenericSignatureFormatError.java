package java.lang.reflect;


/**
 * 当需要解释类型, 方法或构造函数的通用签名信息的反射方法遇到语法错误的签名属性时抛出.
 *
 * @since 1.5
 */
public class GenericSignatureFormatError extends ClassFormatError {
    private static final long serialVersionUID = 6709919147137911034L;

    /**
     * 构造一个新的 {@code GenericSignatureFormatError}.
     */
    public GenericSignatureFormatError() {
        super();
    }

    /**
     * 利用指定的消息构造一个新的{@code GenericSignatureFormatError}
     *
     * @param message 详细消息, 可能为 {@code null}
     */
    public GenericSignatureFormatError(String message) {
        super(message);
    }
}
