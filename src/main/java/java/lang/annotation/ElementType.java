package java.lang.annotation;

/**
 * 此枚举类型的常量提供了注解可能出现在 java 程序中的语法位置的简单分类.
 * 这些常量用于 {@link Target java.lang.annotation.Targe} 元注解, 标注
 * 给定类型可以添加注解的合法位置.
 *
 * <p>注解可能出现的语法位置分为适用于声明的<em>声明上下文</em>, 适用于
 * 声明和表达式的 <em>类型上下文</em>.
 *
 * <p>常量 {@link #ANNOTATION_TYPE} , {@link #CONSTRUCTOR} , {@link #FIELD} ,
 * {@link #LOCAL_VARIABLE} , {@link #METHOD} , {@link #PACKAGE} , {@link #PARAMETER} ,
 * {@link #TYPE} , and {@link #TYPE_PARAMETER} 对应声明上下文,
 * 于java语言规范第9.6.4.1节.
 *
 * <p>举个例子, 一个注解类型被元注解 {@code @Target(ElementType.FIELD)}
 * 只能写为字段声明的修饰符.
 *
 * <p>常量 {@link #TYPE_USE} 对应 JLS 4.11 中的 15 种类型上下文,
 * 以及两个声明上下文: 类型声明(包括注解类型声明)和类型参数声明.
 *
 * <p>举个例子, 一个注解类型被元注解 {@code @Target(ElementType.TYPE_USE)} 标注
 * 可以写在字段的类型上(或者在字段的类型内, 如果它是嵌套的, 参数化的或数组类型),
 * 也可以作为类声明的修饰符出现.
 *
 * <p> {@code TYPE_USE} 常量包含类型声明类型参数声明作为类型检查器设计者的边里,
 * 如果一个注解类型 {@code NonNull} 被源主机 {@code @Target(ElementType.TYPE_USE)}
 * 标注, 那么 {@code @NonNull} {@code class C {...}} 可以通过类型检查来表明所有
 * {@code C} 的对象是非空的, 同时仍然允许其他类的变量为非null或非null,
 * 这取决于{@code @NonNull}是否出现在变量的声明中.
 *
 * @author Joshua Bloch
 * @jls 9.6.4.1 @Target
 * @jls 4.1 The Kinds of Types and Values
 * @since 1.5
 */
public enum ElementType {
    /**
     * 声明 类, 接口(包含注解类型)或者枚举
     */
    TYPE,

    /**
     * 声明字段(包含枚举常量)
     */
    FIELD,

    /**
     * 声明方法
     */
    METHOD,

    /**
     * 声明形参
     */
    PARAMETER,

    /**
     * 声明构造器
     */
    CONSTRUCTOR,

    /**
     * 声明局部变量
     */
    LOCAL_VARIABLE,

    /**
     * 声明注解类型
     */
    ANNOTATION_TYPE,

    /**
     * 声明包(在 package-info.java 文件 package 声明的位置使用)
     */
    PACKAGE,

    /**
     * 声明自定义类型
     * 声明泛型时使用
     *
     * @since 1.8
     */
    TYPE_PARAMETER,

    /**
     * 使用在声明类型的任意语句中
     *
     * @since 1.8
     */
    TYPE_USE
}
