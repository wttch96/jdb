package java.lang.reflect;

/**
 * 被注解的类型变量.
 * {@code AnnotatedTypeVariable} 表示类型变量的潜在注解用法, 其声明可能具有自身代表类型的注解用法的边界.
 *
 * @since 1.8
 */
public interface AnnotatedTypeVariable extends AnnotatedType {

    /**
     * 返回此类型变量的潜在注解边界.
     *
     * @return 此类型变量的潜在注解边界
     */
    AnnotatedType[] getAnnotatedBounds();
}
