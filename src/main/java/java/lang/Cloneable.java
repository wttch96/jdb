package java.lang;

/**
 * 类实现 <code>Cloneable</code> 接口, 以指示{@link java.lang.Object#clone()}方法,
 * 该方法对于那个类的实例的字段实地复制是合法的.
 * <p>
 * 在未实现 <code>Cloneable</code> 接口的实例上调用 Object 的 clone 方法会导致
 * 抛出异常 <code>CloneNotSupportedException </code>.
 * <p>
 * 按照惯例, 实现此接口的类应使用公共方法覆盖 <tt>Object.clone</tt>(受保护).
 * 有关重写此方法的详细信息, 请参阅{@link java.lang.Object#clone()}.
 * <p>
 * 请注意, 此接口<i>不</i>包含<tt>clone</tt>方法.
 * 因此, 仅仅通过实现该接口的事实来克隆对象是不可能的.
 * 即使有效地调用clone方法, 也无法保证它会成功.
 *
 * @author unascribed
 * @see java.lang.CloneNotSupportedException
 * @see java.lang.Object#clone()
 * @since JDK1.0
 */
public interface Cloneable {
}
