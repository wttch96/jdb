package java.lang.annotation;

/**
 * 指示注解类型适用的上下文. 在 JLS 9.6.4.1 中指定了可以应用注解类型的声明上下文
 * 和类型上下文, 并在源代码中用 {@link ElementType java.lang.annotation.ElementType}
 * 的枚举常量表示.
 *
 * <p>如果注解类型 {@code T} 上不存在 {@code @Target} 元注解,
 * 则可以将类型为 {@code T} 的注解写为除类型参数声明之外的
 * 任何声明的修饰符.
 *
 * <p>如果存在 {@code @Target} 元注解, 编译器将根据
 * JLS 9.7.4强制执行 {@code ElementType} 枚举常量指示的使用限制.
 * 如果不正确使用, 将会出现编译错误.
 *
 * <p>例如, 这个 {@code @Target} 元注解表明声明的类型本身就是元注解类型.
 * 它只能用于注解类型声明:
 * <pre>
 *    &#064;Target(ElementType.ANNOTATION_TYPE)
 *    public &#064;interface MetaAnnotationType {
 *        ...
 *    }
 * </pre>
 *
 * <p>此{@code @Target}元注解表示声明类型仅用作复杂注解类型声明中的成员类型.
 * 它不能直接注释任何东西:
 * <pre>
 *    &#064;Target({})
 *    public &#064;interface MemberType {
 *        ...
 *    }
 * </pre>
 *
 * <p>单个{@code ElementType}常量在{@code @Target}注解中出现多次是一个编译时错误.
 * 例如, 以下{@code @Target} 元注解是非法的:
 * <pre>
 *    &#064;Target({ElementType.FIELD, ElementType.METHOD, ElementType.FIELD})
 *    public &#064;interface Bogus {
 *        ...
 *    }
 * </pre>
 *
 * @jls 9.6.4.1 @Target
 * @jls 9.7.4 Where Annotations May Appear
 * @since 1.5
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Target {
    /**
     * 返回可以应用注解类型的元素种类的数组.
     *
     * @return 可以应用注解类型的元素种类的数组
     */
    ElementType[] value();
}
