package java.io;

/**
 * 在序列化或反序列化未激活时抛出.
 *
 * @author unascribed
 * @since JDK1.1
 */
public class NotActiveException extends ObjectStreamException {

  private static final long serialVersionUID = -3893467273049808895L;

  /**
   * Constructor to create a new NotActiveException with the reason given.
   *
   * @param reason a String describing the reason for the exception.
   */
  public NotActiveException(String reason) {
    super(reason);
  }

  /**
   * Constructor to create a new NotActiveException without a reason.
   */
  public NotActiveException() {
    super();
  }
}
