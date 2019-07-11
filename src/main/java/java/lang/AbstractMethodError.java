package java.lang;

/**
 * 当应用程序尝试调用抽象方法时抛出这个异常.
 * 通常这个错误被编译器捕获;
 * 如果自上次编译当前正在执行的方法以来某些类的定义发生了不兼容的更改, 则次错误只能在运行时发生.
 *
 * @author unascribed
 * @since JDK1.0
 */
public
class AbstractMethodError extends IncompatibleClassChangeError {
    private static final long serialVersionUID = -1654391082989018462L;

    /**
     * 构造一个 <code>AbstractMethodError</code> 不使用任何消息.
     */
    public AbstractMethodError() {
        super();
    }

    /**
     * 用指定的消息构造一个 <code>AbstractMethodError</code> 对象.
     *
     * @param s 详细的消息
     */
    public AbstractMethodError(String s) {
        super(s);
    }
}
