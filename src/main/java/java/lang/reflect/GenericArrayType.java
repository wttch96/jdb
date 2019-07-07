package java.lang.reflect;

/**
 * {@code GenericArrayType}表示一种数组类型, 其组件类型是参数化类型或类型变量.
 *
 * @since 1.5
 */
public interface GenericArrayType extends Type {
    /**
     * 返回一个 {@code Type} 对象来表示这个数组的组件类型.
     * 这个方法创建这个数组的组件类型. 参阅
     * {@link java.lang.reflect.ParameterizedType ParameterizedType}
     * 了解参数化类型创建过程的语义,  参阅
     * {@link java.lang.reflect.TypeVariable TypeVariable}
     * 了解类型变量创建过程的语义.
     *
     * @return 一个 {@code Type} 对象来表示这个数组的组件类型
     * @throws TypeNotPresentException             如果基础数组类型的组件类型引用了一个不存在的类型声明.
     * @throws MalformedParameterizedTypeException 如果基础数组类型的组件类型引用了一个参数化类型,
     *                                             并且由于某种原因它不能被实例化.
     */
    Type getGenericComponentType();
}
