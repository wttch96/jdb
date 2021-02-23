package java.io;

/**
 * 指示一个或多个反序列化对象未通过验证测试. 该参数应提供失败的原因.
 *
 * @author unascribed
 * @see ObjectInputValidation
 * @since JDK1.1
 * @since JDK1.1
 */
public class InvalidObjectException extends ObjectStreamException {

  private static final long serialVersionUID = 3233174318281839583L;

  /**
   * Constructs an <code>InvalidObjectException</code>.
   *
   * @param reason Detailed message explaining the reason for the failure.
   * @see ObjectInputValidation
   */
  public InvalidObjectException(String reason) {
    super(reason);
  }
}
