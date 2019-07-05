package java.lang.annotation;

/**
 * 所有注解类型扩展的公共接口. 请注意手动扩展该接口<i>不会</i>定义一个注解类型.
 * 此外, 此接口本身不定义注解类型.
 * <p>
 * 有关注解类型的更多信息, 请参阅 <cite>java语言规范</cite> 的第9.6节.
 * <p>
 * {@link java.lang.reflect.AnnotatedElement}接口讨论了在将注释类型从不可重复发展为可重复时的兼容性问题.
 *
 * @author Josh Bloch
 * @since 1.5
 */
public interface Annotation {
    /**
     * 如果指定的对象表示逻辑上等于此注解的注解, 则返回 true. 换句话说,
     * 如果指定的对象是与此实例相同的注解类型的实例, 则返回 true, 其所
     * 有成员都等于此注解的所有成员, 如下所示:
     * <ul>
     * <li>两个相应的原生数据类型 <tt>x</tt> 和 <tt>y</tt> 如果 <tt>x == y</tt>
     * 则被认定为相等的, 除非它们的类型是 <tt>float</tt> 或者 <tt>double</tt>.
     *
     * <li>两个相应的 <tt>float</tt> 成员 <tt>x</tt> 和 <tt>y</tt> 如果
     * <tt>Float.valueOf(x).equals(Float.valueOf(y))</tt> 则被认定为相等的.
     * (不像 <tt>==</tt> 操作符, NaN 被认定为等于它自身, 并且 <tt>0.0f</tt>
     * 不等于 <tt>-0.0f</tt>.)
     *
     * <li>两个相应的 <tt>double</tt> 成员 <tt>x</tt> 和 <tt>y</tt> 如果满足
     * <tt>Double.valueOf(x).equals(Double.valueOf(y))</tt> 则被认定为想等.
     * (不像 <tt>==</tt> 操作符, NaN 被认定为等于它自身, 并且 <tt>0.0</tt>
     * 不等于 <tt>-0.0</tt>.)
     *
     * <li>两个相应的 <tt>String</tt>, <tt>Class</tt>, 枚举, 或者注解类型成员
     * <tt>x</tt> and <tt>y</tt> 如果 <tt>x.equals(y)</tt> 则被认定为想等.
     * (此定义对于注解类型成员是递归的.)
     *
     * <li>如果<tt> Arrays.equals (x, y)</tt>, 两个相应的数组类型成员
     * <tt> x </tt>和<tt> y </tt>被认为是相等的. 当适当的重载
     * {@link java.util.Arrays#equals}的时候.
     * </ul>
     *
     * @return true 如果指定的对象表示逻辑上等于此注解的注解, 否则false
     */
    boolean equals(Object obj);

    /**
     * 返回此注解的哈希码, 如以下所示:
     *
     * <p>注解的哈希码是其成员(包含那些具有默认值的哈希码)的哈希码的总和,
     * 定义如下:
     * <p>
     * 注解成员的哈希码是 (计算成员名字{@link String#hashCode()}哈希码的 127倍)
     * 和成员名字的哈希值做异或运算, 定义如下:
     *
     * <p>成员值的哈希码取决于其类型:
     * <ul>
     * <li>基本类型 <tt><i>v</i></tt> 的哈希码等于 <tt><i>WrapperType</i>.valueOf(<i>v</i>).hashCode()</tt>,
     * <tt><i>WrapperType</i></tt> 是基本类型 <tt><i>v</i></tt> 的自动装箱形式 ({@link Byte},
     * {@link Character}, {@link Double}, {@link Float}, {@link Integer},
     * {@link Long}, {@link Short}, or {@link Boolean}).
     *
     * <li>string, enum, class, 或 annotation 成员值 <tt><i>v</i></tt>
     * 的哈希值计算方法是通过调用 <tt><i>v</i>.hashCode()</tt>.
     * (在注解成员值的情况下, 这是递归定义. )
     *
     * <li>数组成员值的哈希值是通过调用 {@link java.util.Arrays#hashCode(long[]) Arrays.hashCode}
     * 计算的.  (每种基本类型都有一个重载, 一个重载用于对象引用类型.)
     * </ul>
     *
     * @return 注解的哈希值
     */
    int hashCode();

    /**
     * 返回此批注的字符串表示形式. 表示的细节依赖于实现, 但以下可能被视为典型:
     * <pre>
     *   &#064;com.acme.util.Name(first=Alfred, middle=E., last=Neuman)
     * </pre>
     *
     * @return 注解的字符串表示形式
     */
    String toString();

    /**
     * 返回这个注解的注解类型.
     *
     * @return 这个注解的注解类型
     */
    Class<? extends Annotation> annotationType();
}
