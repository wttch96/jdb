package java.io;

/**
 * 表示同步操作失败.
 * <p>
 * 强制所有系统缓冲区与基础设备同步时出现同步失败.
 *
 * @author Ken Arnold
 * @see java.io.FileDescriptor#sync
 * @see java.io.IOException
 * @since JDK1.1
 */
public class SyncFailedException extends IOException {

  private static final long serialVersionUID = -2353342684412443330L;

  /**
   * Constructs an SyncFailedException with a detail message. A detail message is a String that
   * describes this particular exception.
   *
   * @param desc a String describing the exception.
   */
  public SyncFailedException(String desc) {
    super(desc);
  }
}
