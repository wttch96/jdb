package java.lang;

/**
 * 发生异常算术条件时抛出. 例如, 一个整型"除零"抛出这个类的实例.
 * <p>
 * {@code ArithmeticException} 对象可以由虚拟机构造, 就好像
 * {@linkplain Throwable#Throwable(String, Throwable, boolean, boolean)
 * 抑制被禁用和/或堆栈跟踪不可写}.
 *
 * @author unascribed
 * @since JDK1.0
 */
public class ArithmeticException extends RuntimeException {
    private static final long serialVersionUID = 2256477558314496007L;

    /**
     * 构造 {@code ArithmeticException} 不使用详细信息.
     */
    public ArithmeticException() {
        super();
    }

    /**
     * 构造 {@code ArithmeticException} 使用制定的详细信息.
     *
     * @param s 详细信息.
     */
    public ArithmeticException(String s) {
        super(s);
    }
}
