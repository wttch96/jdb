package java.io;

/**
 * java.io.Serializable 接口的实现类的可串行化是可行的。不实现该接口的类将
 * 不会有任何序列化和反序列状态。可序列化类的所有子类本身都是可序列化的。
 * 序列化接口没有任何方法和字段，仅用于标示可序列化的语义。</p>
 *
 * 为了允许序列化不可序列化类的子类型,子类型需要自己负责保存和恢复超类数据
 * 类型的 public, protected 和 (如果可以访问的)包字段. 只有当扩展类具有可
 * 访问的无参数构造函数来初始化类的状态时,子类型才可以承担此责任.如果不这
 * 样做,则声明一个颗序列化的类是错误的. 运行时将可能检测到错误.wttch:主要
 * 是在反序列化的时候会用到,序列化没有无参构造函数并没有发现相关影响.<p>
 *
 * 在反序列化期间,将使用该类的public或protected的无参构造函数初始化不可序
 * 列化类的字段. 无参构造函数必须可被反序列化的子类访问. 可序列化子类的字
 * 段将从流中恢复.<p>
 *
 * 在遍历类依赖的图时,可能会遇到不支持序列化接口的对象.在这种情况下,
 * NotSerializableException 异常将被抛出.并标识不可序列化对象的类.<p>
 *
 * 在序列化和反序列化过程中需要特殊处理的类必须实现具有以下确切签名的
 * 特殊方法:
 *
 * <PRE>
 * private void writeObject(java.io.ObjectOutputStream out)
 *     throws IOException
 * private void readObject(java.io.ObjectInputStream in)
 *     throws IOException, ClassNotFoundException;
 * private void readObjectNoData()
 *     throws ObjectStreamException;
 * </PRE>
 *
 * <p> writeObject 方法负责为其特定的类编写对象的序列化状态,以便响应的利用
 * readObject 方法能够恢复它. 保存对象字段的默认机制可以通过调用 
 * out.defaultWriteObject 来序列化. 该方法本身并不需要关心属于其超类或子类
 * 的状态. 通过使用 writeObject 方法将各个字段写入 ObjectOutputStream, 或者
 * 使用 DataOutput 支持原始数据类型的方法来保存对象的状态.
 *
 * <p> readObject 方法负责从流中读取并恢复类字段. 它默认可以调用 in.defaultReadObject
 * 来恢复对象的非静态和非瞬态字段. defaultReadObject 方法中使用流中的信息来
 * 分配保存在流中的对象的字段, 并在当前对象中指定相对应的字段. 当类演化为
 * 添加新字段时, 这将处理这种情况. 该方法本身不需要关心属于其超类或子类的状态.
 * 通过使用 writeObject 方法将各个字段写入 ObjectOutputStream, 或者使用 DataOutput 
 * 写入原始的数据类型来保存状态.
 *     
 * <p> 如果序列化流没有将给定的类作为反序列化对象的超类列出, readObjectNoData 方法
 * 负责为其特定类初始化对象的状态. 当接收方与发送方使用不同的反序列化版本时, 并且
 * 接收方的版本扩展类发送方版本没有的扩展类时, 可能会发生这种情况. 如果序列化流被
 * 篡改,也有可能发生这种情况; readObjectNoData 对于正确的初始化反序列化对象非常有用,
 * 尽管存在"敌对的"或不完整的源流.
 *
 * <p>当向流写入对象时, 需要指定要使用的代替对象的可序列化类.
 * wttch: 这个方法将取代 writeObject 方法执行,也就是说序列化时将使用这个可序列化的
 * 返回值来代替该对象.
 * 应当实现这个特殊的方法与精确的签名:
 *
 * <PRE>
 * ANY-ACCESS-MODIFIER Object writeReplace() throws ObjectStreamException;
 * </PRE><p>
 *
 * 如果这个方法存在,并且可以从正在序列化的对象的类中定义的方法访问,则通过调用
 * writeReplace 方法序列化. 因此,该方法可以是 private,protected,package-private.
 * 对该方法的子类访问遵守 java 颗访问性规则.<p>
 *
 * 当从流中读取替换实例时,
 * wttch: 这个方法在反序列化的时候调用,如果反序列的对象是该类的实例, 最后反序列化
 * 成的对象将有该方法的返回值来代替. 当然,这个方法可以很好的解决反序列化过程中会
 * 打破单例的情况,只需让该方法返回单例的 INSTANCE.
 * 应当实现这个特殊的方法与精确的签名:
 *
 * <PRE>
 * ANY-ACCESS-MODIFIER Object readResolve() throws ObjectStreamException;
 * </PRE><p>
 *
 * 此 readResolve 方法和 writeReplace 方法具有相同的调用规则和可访问性,它们是
 * 相互的逆操作. <p>
 *
 * 每一个可序列化类在运行时都有一个相关联的版本数字, 称为 serialVersionUID,
 * 它在反序列化期间使用它来验证序列化对象的发送方和接收方是否为加载该对象
 * 所兼容的类. 如果接收方为对象加载一个类, 该对象的 serialVersionUID 与对应
 * 发送方的类不同,反序列化将导致 {@link InvalidClassException} 异常. 可序列化类
 * 可以显式地声明自己的 <code>"serialVersionUID"</code>, 它必须是静态的, final的,
 * 类型为 <code>long</code>:
 *
 * <PRE>
 * ANY-ACCESS-MODIFIER static final long serialVersionUID = 42L;
 * </PRE>
 *
 * 如果可序列化类没有显示声明 serialVersionUID, 则序列化运行时将根据该类的各个
 * 方面计算该类的默认 serialVersionUID 的值, 如Java(TM)对象序列化规范中描述的那样.
 * 但是,<em>强烈建议</em>所有可序列化类显式的声明 serialVersionUID 的值,因为默认
 * serialVersionUID 计算对类的细节非常敏感, 类细节可能因为编译器实现的不同而有所不同,
 * 因此,在反序列化期间可能导致 <code>InvalidClassException</code> 异常.
 * 因此,为了保证跨不同java编译器实现的 serialVersionUID值一致, serializable 类必须
 * 显式声明 serialVersionUID 的值. 另外,还强烈建议显式声明的 serialVersionUID 尽可能
 * 使用 private 修饰符, 以为此类声明只适用当前的类,对其继承成员是没有作用的. 数组类
 * 不能显式的声明 serialVersionUID 的值, 因此他们始终具有默认的计算值,但是数组类
 * 放弃了匹配serialVersionUID 的要求.
 *
 * @author  unascribed
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 * @see java.io.ObjectOutput
 * @see java.io.ObjectInput
 * @see java.io.Externalizable
 * @since   JDK1.1
 */
public interface Serializable {
}
