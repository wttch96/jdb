package java.io;

/**
 * 特定于对象流类的所有异常的超类.
 * <p>
 * wttch: 比如对象序列化时出现问题(序列化出错或者类型不支持序列化)等.
 *
 * @author unascribed
 * @since JDK1.1
 */
public abstract class ObjectStreamException extends IOException {

  private static final long serialVersionUID = 7260898174833392607L;

  /**
   * Create an ObjectStreamException with the specified argument.
   *
   * @param classname the detailed message for the exception
   */
  protected ObjectStreamException(String classname) {
    super(classname);
  }

  /**
   * Create an ObjectStreamException.
   */
  protected ObjectStreamException() {
    super();
  }
}
