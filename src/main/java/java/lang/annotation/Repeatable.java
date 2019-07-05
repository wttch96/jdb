package java.lang.annotation;

/**
 * 注解类型 {@code java.lang.annotation.Repeatable} 用于指示其声明
 * (元)注解的注解类型是可重复的. {@code @Repeatable}的值表示可重复
 * 注解类型的 <em>包含注解类型</em> .
 *
 * @jls 9.6 Annotation Types
 * @jls 9.7 Annotations
 * @since 1.8
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Repeatable {
    /**
     * {@code @Repeatable}的值表示可重复注解类型的 <em>包含注解类型</em> .
     * <p>
     * 它是一个注解类型的容器, 重复的注解类型将被放入该只所标示的注解中去.
     * 这个注解类型中包含 {@code Repeatable}标注的注解的一个数组, 来存放
     * 重复的注解.
     * <code>@Target(ElementType.TYPE)</code>
     * <code>@Retention(RetentionPolicy.RUNTIME)</code>
     * <code>public @interface As {A[] value();}</code>
     * <code>@Repeatable(As.class) public @interface A {}</code>
     *
     * @return 包含的注解类型
     */
    Class<? extends Annotation> value();
}
