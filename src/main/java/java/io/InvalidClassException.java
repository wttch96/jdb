package java.io;

/**
 * 序列化运行时检测到以下类问题之一时引发:
 * <ul>
 * <li> 该类的串行版本与从流中读取的类描述符的串行版本不匹配
 * <li> 该类包含未知的数据类型
 * <li> 该类没有可访问的无参数构造函数
 * </ul>
 *
 * @author unascribed
 * @since JDK1.1
 */
public class InvalidClassException extends ObjectStreamException {

  private static final long serialVersionUID = -4333316296251054416L;

  /**
   * Name of the invalid class.
   *
   * @serial Name of the invalid class.
   */
  public String classname;

  /**
   * Report an InvalidClassException for the reason specified.
   *
   * @param reason String describing the reason for the exception.
   */
  public InvalidClassException(String reason) {
    super(reason);
  }

  /**
   * Constructs an InvalidClassException object.
   *
   * @param cname  a String naming the invalid class.
   * @param reason a String describing the reason for the exception.
   */
  public InvalidClassException(String cname, String reason) {
    super(reason);
    classname = cname;
  }

  /**
   * Produce the message and include the classname, if present.
   */
  @Override
  public String getMessage() {
    if (classname == null) {
      return super.getMessage();
    } else {
      return classname + "; " + super.getMessage();
    }
  }
}
