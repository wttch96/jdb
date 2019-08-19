package java.lang.reflect;

/**
 * {@code InvocationHandler} 是由代理实例的<i>调用处理程序</i>实现的接口.
 *
 * <p>每个代理实例都有一个关联的调用处理程序. 在代理实例上调用方法时,
 * 方法调用将被编码并调度到其处理程序的 {@code invoke} 方法.
 *
 * @author Peter Jones
 * @see Proxy
 * @since 1.3
 */
public interface InvocationHandler {

    /**
     * 处理代理实例上的方法调用并返回结果. 在与其关联的代理实例上调用方法时, 将在调用处理程序上调用此方法.
     *
     * @param proxy  调用该方法的代理实例
     * @param method 与在代理实例上调用的接口方法对应的 {@code Method} 实例. {@code Method} 对象的声明类
     *               将是声明方法的接口, 它可以是代理接口的超接口, 代理类通过该接口继承方法.
     * @param args   包含在代理实例上的方法调用中传递的参数值的对象数组, 如果接口方法不带参数,
     *               则为{@code null}. 原始类型的参数包含在适当的原始包装类的实例中, 例如
     *               {@code java.lang.Integer} 或 {@code java.lang.Boolean}.
     * @return 从代理实例上的方法调用返回的值. 如果接口方法的声明返回类型是基本类型, 则此方法返回的值必须是相应原始包装类的实例;
     * 否则, 它必须是可分配给声明的返回类型的类型. 如果此方法返回的值为 {@code null} 且接口方法的返回类型为原始值,
     * 则代理实例上的方法调用将抛出 {@code NullPointerException}. 如果此方法返回的值与上面描述的接口方法声明的
     * 返回类型不兼容, 则代理实例上的方法调用将抛出 {@code ClassCastException}.
     * @throws Throwable 从代理实例上的方法调用中抛出的异常. 异常的类型必须可以分配给接口方法的 {@code throws}
     *                   子句中声明的任何异常类型, 也可以分配给未经检查的异常类型 {@code java.lang.RuntimeException}
     *                   或 {@code java.lang.Error}. 如果此方法抛出了一个不能分配给接口方法的 {@code throws} 子句中
     *                   声明的任何异常类型的已检查异常, 那么包含此方法抛出的异常的 {@link UndeclaredThrowableException}
     *                   将由代理实例上的方法调用抛出.
     * @see UndeclaredThrowableException
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable;
}
