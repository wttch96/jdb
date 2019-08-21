package java.lang;

/**
 * 抛出表示断言失败.
 *
 * <p>此类提供的七个单参数公共构造函数确保调用返回的断言错误:
 * <pre>
 *     new AssertionError(<i>expression</i>)
 * </pre>
 * 具有<i>表达式</i>的<i>字符串转换</i>作为其详细信息 (如 <cite>The Java&trade; Language Specification</cite>
 * 第 15.18.1.1 节所定义), 不管 <i>expression</i> 的类型如何.
 *
 * @since 1.4
 */
public class AssertionError extends Error {
    private static final long serialVersionUID = -5013299493970297370L;

    /**
     * 构造一个没有详细信息的 AssertionError .
     */
    public AssertionError() {
    }

    /**
     * 这个内部构造函数不处理其字符串参数, 即使它是一个空引用.
     * 公共构造函数永远不会使用 null 参数调用此构造函数.
     */
    private AssertionError(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 构造一个 AssertionError 及其从指定对象派生的详细信息, 该对象被转换为
     * <cite>The Java&trade; Language Specification</cite> 的 15.18.1.1 节中定义的字符串.
     * <p>
     * 如果指定的对象是 {@code Throwable} 的实例, 则它将成为新构造的断言错误的 <i>cause</i>.
     *
     * @param detailMessage 用于构造详细消息的值
     * @see Throwable#getCause()
     */
    public AssertionError(Object detailMessage) {
        this(String.valueOf(detailMessage));
        if (detailMessage instanceof Throwable)
            initCause((Throwable) detailMessage);
    }

    /**
     * 构造一个 AssertionError, 其详细消息派生自指定的 <code>boolean</code>, 它被转换为
     * <cite>The Java&trade; Language Specification</cite> 的 15.18.1.1 节中定义的字符串.
     *
     * @param detailMessage 用于构造详细消息的值
     */
    public AssertionError(boolean detailMessage) {
        this(String.valueOf(detailMessage));
    }

    /**
     * 构造一个 AssertionError, 其详细消息派生自指定的 <code>char</code>, 它被转换为
     * <cite>The Java&trade; Language Specification</cite> 的 15.18.1.1 节中定义的字符串.
     *
     * @param detailMessage 用于构造详细消息的值
     */
    public AssertionError(char detailMessage) {
        this(String.valueOf(detailMessage));
    }


    /**
     * 构造一个 AssertionError, 其详细消息派生自指定的 <code>int</code>, 它被转换为
     * <cite>The Java&trade; Language Specification</cite> 的 15.18.1.1 节中定义的字符串.
     *
     * @param detailMessage 用于构造详细消息的值
     */
    public AssertionError(int detailMessage) {
        this(String.valueOf(detailMessage));
    }

    /**
     * 构造一个 AssertionError, 其详细消息派生自指定的 <code>long</code>, 它被转换为
     * <cite>The Java&trade; Language Specification</cite> 的 15.18.1.1 节中定义的字符串.
     *
     * @param detailMessage 用于构造详细消息的值
     */
    public AssertionError(long detailMessage) {
        this(String.valueOf(detailMessage));
    }


    /**
     * 构造一个 AssertionError, 其详细消息派生自指定的 <code>float</code>, 它被转换为
     * <cite>The Java&trade; Language Specification</cite> 的 15.18.1.1 节中定义的字符串.
     *
     * @param detailMessage 用于构造详细消息的值
     */
    public AssertionError(float detailMessage) {
        this(String.valueOf(detailMessage));
    }


    /**
     * 构造一个 AssertionError, 其详细消息派生自指定的 <code>double</code>, 它被转换为
     * <cite>The Java&trade; Language Specification</cite> 的 15.18.1.1 节中定义的字符串.
     *
     * @param detailMessage 用于构造详细消息的值
     */
    public AssertionError(double detailMessage) {
        this(String.valueOf(detailMessage));
    }

    /**
     * 构造一个 {@code AssertionError} 使用详细消息和原因.
     *
     * <p>注意 {@code cause} 相关联的详细信息 <em>不会</em>自动合并到这个 Error 的详细信息中去.
     *
     * @param message 详细消息, 可以为 {@code null}
     * @param cause   原因, 可以为 {@code null}
     * @since 1.7
     */
    public AssertionError(String message, Throwable cause) {
        super(message, cause);
    }
}
