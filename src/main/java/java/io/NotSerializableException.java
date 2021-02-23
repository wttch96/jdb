package java.io;

/**
 * 当需要实例具有可序列化接口时抛出. 序列化运行时或实例的类可以引发此异常. 参数应为类的名称.
 *
 * @author unascribed
 * @since JDK1.1
 */
public class NotSerializableException extends ObjectStreamException {

  private static final long serialVersionUID = 2906642554793891381L;

  /**
   * Constructs a NotSerializableException object with message string.
   *
   * @param classname Class of the instance being serialized/deserialized.
   */
  public NotSerializableException(String classname) {
    super(classname);
  }

  /**
   * Constructs a NotSerializableException object.
   */
  public NotSerializableException() {
    super();
  }
}
