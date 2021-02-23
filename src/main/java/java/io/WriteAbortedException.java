package java.io;

/**
 * 表示在写操作期间抛出了ObjectStreamExceptions之一. 在写操作期间抛出ObjectStreamExceptions之一时, 在读操作期间抛出.
 * 在详细信息字段中可以找到终止写入的异常. 流将重置为其初始状态, 并且所有对已反序列化的对象的引用都将被丢弃.
 *
 * <p>从版本1.4开始, 已对该异常进行了改进, 以符合通用异常链机制.
 * 在构造时提供并通过公共{@link #detail}字段访问的“导致中止的异常”现在称为<i>cause<i>，可以通过{@link Throwable#getCause()}访问方法,
 * 以及前面提到的“旧版字段”.
 *
 * @author unascribed
 * @since JDK1.1
 */
public class WriteAbortedException extends ObjectStreamException {

  private static final long serialVersionUID = -3326426625597282442L;

  /**
   * Exception that was caught while writing the ObjectStream.
   *
   * <p>This field predates the general-purpose exception chaining facility.
   * The {@link Throwable#getCause()} method is now the preferred means of obtaining this
   * information.
   *
   * @serial
   */
  public Exception detail;

  /**
   * Constructs a WriteAbortedException with a string describing the exception and the exception
   * causing the abort.
   *
   * @param s  String describing the exception.
   * @param ex Exception causing the abort.
   */
  public WriteAbortedException(String s, Exception ex) {
    super(s);
    initCause(null);  // Disallow subsequent initCause
    detail = ex;
  }

  /**
   * Produce the message and include the message from the nested exception, if there is one.
   */
  @Override
  public String getMessage() {
    if (detail == null) {
      return super.getMessage();
    } else {
      return super.getMessage() + "; " + detail.toString();
    }
  }

  /**
   * Returns the exception that terminated the operation (the <i>cause</i>).
   *
   * @return the exception that terminated the operation (the <i>cause</i>), which may be null.
   * @since 1.4
   */
  @Override
  public Throwable getCause() {
    return detail;
  }
}
