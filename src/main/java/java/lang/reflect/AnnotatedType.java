package java.lang.reflect;

/**
 * {@code AnnotatedType} 表明此 VM 中正在运行的程序中潜在的注解运用的类型.
 * 在 java 编程语言中它可能是任何类型, 包含数组类型, 参数化类型, 类型变量
 * 或者通配符类型(通配符表达式, 例如: ? extends A).
 *
 * @since 1.8
 */
public interface AnnotatedType extends AnnotatedElement {

    /**
     * 返回此带注解类型表示的基础类型.
     *
     * @return 此带注解类型表示的基础类型
     */
    public Type getType();
}
