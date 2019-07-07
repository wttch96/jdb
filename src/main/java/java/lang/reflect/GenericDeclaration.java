package java.lang.reflect;

/**
 * 声明类型变量的所有实体的通用接口.
 *
 * @since 1.5
 */
public interface GenericDeclaration extends AnnotatedElement {
    /**
     * 返回一个 {@code TypeVariable} 对象的数组, 这些对象表示由此
     * {@code GenericDeclaration}对象表示的泛型声明声明的类型变量,
     * 按声明的顺序. 如果底层泛型声明未声明的类型变量, 则返回长度为 0 的数组.
     *
     * @return 一个 {@code TypeVariable} 对象的数组, 这些数组表示泛型声明声明的类型变量
     * @throws GenericSignatureFormatError 如果此通用声明的通用签名不符合<cite>Java虚拟机规范</cite>中指定的格式
     */
    public TypeVariable<?>[] getTypeParameters();
}
