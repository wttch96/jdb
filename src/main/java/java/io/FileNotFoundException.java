package java.io;


/**
 * 表示尝试打开由指定路径名表示的文件失败, 找不到文件.
 *
 * <p> 当具有指定路径名的文件不存在时, {@link FileInputStream}, {@link FileOutputStream}和{@link
 * RandomAccessFile}构造函数将抛出此异常. 如果文件确实存在, 但由于某些原因而无法访问, 例如: 当试图打开一个只读文件进行写入时, 这些构造方法也会抛出该文件.
 *
 * @author unascribed
 * @since JDK1.0
 */

public class FileNotFoundException extends IOException {

  private static final long serialVersionUID = -897856973823710492L;

  /**
   * Constructs a <code>FileNotFoundException</code> with
   * <code>null</code> as its error detail message.
   */
  public FileNotFoundException() {
    super();
  }

  /**
   * Constructs a <code>FileNotFoundException</code> with the specified detail message. The string
   * <code>s</code> can be retrieved later by the
   * <code>{@link java.lang.Throwable#getMessage}</code>
   * method of class <code>java.lang.Throwable</code>.
   *
   * @param s the detail message.
   */
  public FileNotFoundException(String s) {
    super(s);
  }

  /**
   * Constructs a <code>FileNotFoundException</code> with a detail message consisting of the given
   * pathname string followed by the given reason string.  If the <code>reason</code> argument is
   * <code>null</code> then it will be omitted.  This private constructor is invoked only by native
   * I/O methods.
   *
   * @since 1.2
   */
  private FileNotFoundException(String path, String reason) {
    super(path + ((reason == null)
        ? ""
        : " (" + reason + ")"));
  }

}
