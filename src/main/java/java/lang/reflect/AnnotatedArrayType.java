package java.lang.reflect;


/**
 * {@code AnnotatedArrayType} 表示数组类型的潜在注解用法, 其组件类型本身可以表示类型的注释用法.
 *
 * @since 1.8
 */
public interface AnnotatedArrayType extends AnnotatedType {

    /**
     * 返回此数组类型的可能带注释的通用组件类型.
     *
     * @return 此数组类型的可能带注释的通用组件类型
     */
    AnnotatedType getAnnotatedGenericComponentType();
}
