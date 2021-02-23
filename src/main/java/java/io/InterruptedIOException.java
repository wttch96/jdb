package java.io;

/**
 * 表示IO操作已中断. 抛出<code>InterruptedIOException<code>表示输入或输出传输已终止, 因为执行该输入或输出的线程已中断. 字段{@link
 * #bytesTransferred}指示中断发生之前成功传输了多少字节.
 * <p>
 * wttch: 一般是在 io 操作中 wait() 等方法调用时, 出现 {@link InterruptedException} 时包装 InterruptedException.
 *
 * @author unascribed
 * @see java.io.InputStream
 * @see java.io.OutputStream
 * @see java.lang.Thread#interrupt()
 * @since JDK1.0
 */
public
class InterruptedIOException extends IOException {

  private static final long serialVersionUID = 4020568460727500567L;

  /**
   * Constructs an <code>InterruptedIOException</code> with
   * <code>null</code> as its error detail message.
   */
  public InterruptedIOException() {
    super();
  }

  /**
   * Constructs an <code>InterruptedIOException</code> with the specified detail message. The
   * string
   * <code>s</code> can be retrieved later by the
   * <code>{@link java.lang.Throwable#getMessage}</code>
   * method of class <code>java.lang.Throwable</code>.
   *
   * @param s the detail message.
   */
  public InterruptedIOException(String s) {
    super(s);
  }

  /**
   * Reports how many bytes had been transferred as part of the I/O operation before it was
   * interrupted.
   *
   * @serial
   */
  public int bytesTransferred = 0;
}
