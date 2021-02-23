package java.io;

/**
 * 表示已在数据输入流或任何实现数据输入接口的类中读取了格式为<a href="DataInput.html#modified-utf-8">修改后的UTF-8</a>格式错误的字符串. 请参阅<a
 * href="DataInput.html#modified-utf-8"> <code> DataInput <code> <a>类说明, 以获取读写修改后的UTF-8字符串的格式.
 *
 * @author Frank Yellin
 * @see java.io.DataInput
 * @see java.io.DataInputStream#readUTF(java.io.DataInput)
 * @see java.io.IOException
 * @since JDK1.0
 */
public
class UTFDataFormatException extends IOException {

  private static final long serialVersionUID = 420743449228280612L;

  /**
   * Constructs a <code>UTFDataFormatException</code> with
   * <code>null</code> as its error detail message.
   */
  public UTFDataFormatException() {
    super();
  }

  /**
   * Constructs a <code>UTFDataFormatException</code> with the specified detail message. The string
   * <code>s</code> can be retrieved later by the
   * <code>{@link java.lang.Throwable#getMessage}</code>
   * method of class <code>java.lang.Throwable</code>.
   *
   * @param s the detail message.
   */
  public UTFDataFormatException(String s) {
    super(s);
  }
}
