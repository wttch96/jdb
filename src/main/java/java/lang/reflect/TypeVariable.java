package java.lang.reflect;

/**
 * TypeVariable 是类型变量的公共超类接口. 一个类型变量在第一次被反射方法需要时创建, 如此包中指定的那样.
 * 如果一个类型变量 t 一个类型 T (i.e, class, interface 或者 annotation 类型)的引用, 并且 T 由第 n 个
 * 封闭类 T 声明(参见 JLS 8.1.2), 之后创建 t 需要的解决方案(参见 JVMS 5) 包含 T 类的第 i 个(i = [0, n]).
 * 创建类型变量不得导致其边界的创建. 重复创建类型变量无效.
 *
 * <p>可以在运行时实例化多个对象以表示给定的类型变量. 尽管类型变量只创建一次, 但这并不意味着
 * 需要缓存表示类型变量的实例. 但是, 表示类型变量的所有实例必须彼此 equal(). 因此, 类型变量
 * 的用户不得依赖于实现此接口的类的实例的标识.
 * <p>
 * wttch:它就是来保存参数化类型列表的, 其中 {@link #getGenericDeclaration()} 是保存该参数化类型的定义相关的东西,
 * {@link GenericDeclaration#getTypeParameters()} 获取的就是该类型的数组, 包含所有的参数化类型及其边界等信息.
 *
 * @param <D> 声明基础类型变量的泛型声明的类型
 * @since 1.5
 */
public interface TypeVariable<D extends GenericDeclaration> extends Type, AnnotatedElement {
    /**
     * 返回一个 {@code Type} 对象数组来表示这个类型变量的上限.
     * 请注意, 如果未声明上限, 则上限为 {@code Object}.
     *
     * <p>对于每一个上限 B: <ul> <li>如果 B 是一个参数化类型或者类型变量, 它被创造了,
     * (参见 {@link java.lang.reflect.ParameterizedType} )
     * type or a type variable, it is created, (see {@link
     * java.lang.reflect.ParameterizedType ParameterizedType}查看有关参数化类型的创建过程的详细信息).
     * <li>否则, B 被解决. </ul>
     *
     * @return 一个 {@code Type} 对象数组来表示这个类型变量的上限
     * @throws TypeNotPresentException             如果这些边界引用了一个不存在的类型声明
     * @throws MalformedParameterizedTypeException 如果这些边界引用了一个参数化类型,
     *                                             并且由于某种原因, 该类型无法被实例化.
     */
    Type[] getBounds();

    /**
     * 返回表示声明此类型变量的泛型声明的{@code GenericDeclaration}对象.
     *
     * @return 表示声明此类型变量的泛型声明的对象
     * @since 1.5
     */
    D getGenericDeclaration();

    /**
     * 返回此类型变量的名称, 因为它出现在源代码中.
     *
     * @return 此类型变量的名称, 因为它出现在源代码中
     */
    String getName();

    /**
     * 返回 AnnotatedType 对象的数组, 这些对象表示使用类型来表示此 TypeVariable 表示的类型参数的上限.
     * 数组中对象的顺序对应于 type 参数声明中的边界顺序.
     * <p>
     * 如果参数声明没有边界, 则返回长度为 0 的数组.
     *
     * @return 表示类型参数界限的对象数组
     * @since 1.8
     */
    AnnotatedType[] getAnnotatedBounds();
}
