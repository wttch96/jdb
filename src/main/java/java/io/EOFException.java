package java.io;

/**
 * 表示在输入过程中意外到达文件结尾或流结尾.
 *
 * <p>此异常主要由数据输入流用来通知流结束. 注意, 许多其他输入操作在流的末尾返回一个特殊值, 而不是引发异常.
 * <p>
 * wttch: 常用在某些文件需要用固定的文件格式, 比如图片格式或者DataStream读取某种数据(Short,Long 等)格式, 意外的到达了文件末尾,
 * 尚未读取到正确的格式(图片提前结束, Short/Long等数据位数不够).
 *
 * @author Frank Yellin
 * @see java.io.DataInputStream
 * @see java.io.IOException
 * @since JDK1.0
 */
public class EOFException extends IOException {

  private static final long serialVersionUID = 6433858223774886977L;

  /**
   * Constructs an <code>EOFException</code> with <code>null</code> as its error detail message.
   */
  public EOFException() {
    super();
  }

  /**
   * Constructs an <code>EOFException</code> with the specified detail message. The string
   * <code>s</code> may later be retrieved by the
   * <code>{@link java.lang.Throwable#getMessage}</code> method of class
   * <code>java.lang.Throwable</code>.
   *
   * @param s the detail message.
   */
  public EOFException(String s) {
    super(s);
  }
}
