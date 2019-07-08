package java.lang.reflect;

/**
 * Member是反映单个成员(字段或方法)或构造函数的标识信息的接口.
 *
 * @author Nakul Saraiya
 * @see java.lang.Class
 * @see Field
 * @see Method
 * @see Constructor
 */
public
interface Member {

    /**
     * 标示类或接口的所有公共成员的集合, 包括继承的成员.
     */
    public static final int PUBLIC = 0;

    /**
     * 标示类或接口的已声明成员集合.
     * 不包括继承的成员.
     */
    public static final int DECLARED = 1;

    /**
     * 返回表示声明由此 Member 表示的成员或构造函数的类或接口的 Class 对象.
     *
     * @return 表示底层成员的声明类的对象
     */
    public Class<?> getDeclaringClass();

    /**
     * 返回此 Member 表示的基础成员或构造函数的简单名称.
     *
     * @return 基础成员的简单名称
     */
    public String getName();

    /**
     * 返回这个成员或者构造器的 java 语言修饰符(int 类型).
     * 应使用 Modifier 类来解码整数中的修饰符.
     *
     * @return the Java language modifiers for the underlying member
     * @see Modifier
     */
    public int getModifiers();

    /**
     * 返回 {@code true} 如果这个成员是由编译器引入的, 否则 {@code false}.
     *
     * @return true 如果这个成员是由编译器引入的
     * @jls 13.1 The Form of a Binary
     * @since 1.5
     */
    public boolean isSynthetic();
}
