package java.lang.annotation;

/**
 * 当注解解析器尝试从文件中读取注解并确定注解格式错误时抛出.
 * {@linkplain java.lang.reflect.AnnotatedElement API 用于反射读取注解}
 * 可以抛出此错误.
 *
 * @author Josh Bloch
 * @see java.lang.reflect.AnnotatedElement
 * @since 1.5
 */
public class AnnotationFormatError extends Error {
    private static final long serialVersionUID = -4256701562333669892L;

    /**
     * 使用给定的详细信息构造一个新的 <tt>AnnotationFormatError</tt>.
     *
     * @param message 详细的信息
     */
    public AnnotationFormatError(String message) {
        super(message);
    }

    /**
     * 使用给定的详细信息和原因构造一个新的 <tt>AnnotationFormatError</tt>.
     * 请注意, 与<code> cause </code>关联的详细消息<i> 不会 </i>自动合并到此错误的详细消息中.
     *
     * @param message 详细信息
     * @param cause   原因(允许一个 <tt> null </tt>值, 表示原因不存在或未知. )
     */
    public AnnotationFormatError(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * 使用给定的原因和一个详细的信息 <tt>(cause == null ? null : cause.toString()</tt>
     * 来构造一个新的 <tt>AnnotationFormatError</tt>. (它通常包含 <tt>cause</tt> 的类和详细消息).
     *
     * @param cause 原因(允许一个 <tt>null</tt> 值, 表示原因不存在或未知. )
     */
    public AnnotationFormatError(Throwable cause) {
        super(cause);
    }
}
