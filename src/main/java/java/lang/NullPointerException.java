package java.lang;

/**
 * 当程序试图在需要 Object 的时候使用 {@code null} 时抛出. 它们包括:
 * <ul>
 * <li>调用 {@code null} 对象的实例方法时.
 * <li>访问或者修改 {@code null} 对象的字段时.
 * <li>将 {@code null} 的长度视为数组.
 * <li>访问或修改 {@code null} 的插槽, 就像它是一个数组一样.
 * <li>抛出 {@code null},  好像它是 {@code Throwable} 值.
 * </ul>
 * <p>
 * 应用程序应抛出此类的实例以指示 {@code null} 对象的其他非法使用.
 * <p>
 * {@code NullPointerException} 对象可有虚拟机构造, 就好像
 * {@linkplain Throwable#Throwable(String, Throwable, boolean, boolean)
 * 抑制被禁用和/或堆栈跟踪不可写}.
 *
 * @author unascribed
 * @since JDK1.0
 */
public
class NullPointerException extends RuntimeException {
    private static final long serialVersionUID = 5162710183389028792L;

    /**
     * 构造 {@code NullPointerException} 不使用详细消息.
     */
    public NullPointerException() {
        super();
    }

    /**
     * 构造 {@code NullPointerException} 使用指定的详细消息.
     *
     * @param s 详细消息.
     */
    public NullPointerException(String s) {
        super(s);
    }
}
