package java.io;

/**
 * 从对象流读取的控制信息违反内部一致性检查时抛出.
 *
 * @author unascribed
 * @since JDK1.1
 */
public class StreamCorruptedException extends ObjectStreamException {

  private static final long serialVersionUID = 8983558202217591746L;

  /**
   * Create a StreamCorruptedException and list a reason why thrown.
   *
   * @param reason String describing the reason for the exception.
   */
  public StreamCorruptedException(String reason) {
    super(reason);
  }

  /**
   * Create a StreamCorruptedException and list no reason why thrown.
   */
  public StreamCorruptedException() {
    super();
  }
}
