package java.lang.reflect;

/**
 * 被注解的参数化类型.
 * {@code AnnotatedParameterizedType} 表示参数化类型的潜在注解用法,
 * 其类型参数本身可以表示类型的注解用法.
 *
 * @since 1.8
 */
public interface AnnotatedParameterizedType extends AnnotatedType {
    /**
     * 返回此参数化类型的可能带注解的实际类型参数.
     *
     * @return 此参数化类型的可能带注解的实际类型参数
     */
    AnnotatedType[] getAnnotatedActualTypeArguments();
}