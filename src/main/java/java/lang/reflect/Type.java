package java.lang.reflect;

/**
 * Type是所有Java编程语言类型的共同超类接口. 它们包含原始类型, 参数化类型,
 * 数组类型, 类型变量和基本类型.
 *
 * @since 1.5
 */
public interface Type {
    /**
     * 返回一个字符串来描述这个类型, 包含任何有关类型参数的信息.
     *
     * @return 一个字符串来描述这个类型
     * @implSpec 默认实现是调用 {@code toString}.
     * @since 1.8
     */
    default String getTypeName() {

        return toString();
    }
}
