package java.lang.annotation;

/**
 * 注解的保留策略. 此枚举类型常量描述了用于保留注解的各种策略.
 * 它们与 {@link Retention} 元注解类型一起使用, 以指定保留注解的时间.
 *
 * @author Joshua Bloch
 * @since 1.5
 */
public enum RetentionPolicy {
    /**
     * 注解仅在源代码中保留, 编译器将丢弃注解.
     */
    SOURCE,

    /**
     * 注解将由编译器记录在类文件中, 但在运行时不需要由 VM 保留.
     * 这是默认行为.
     */
    CLASS,

    /**
     * 注释将由编译器记录在类文件中, 并在运行时由 VM 保留.
     * 因此可以反射性地读取它们.
     *
     * @see java.lang.reflect.AnnotatedElement
     */
    RUNTIME
}
