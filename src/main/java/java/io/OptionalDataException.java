package java.io;

/**
 * 异常表示由于未读原始数据导致对象读取操作失败, 或者属于流中序列化对象的数据结束. 在两种情况下可能会抛出此异常:
 *
 * <ul>
 *   <li> 当流中的下一个元素是原始数据时, 尝试读取对象. 在这种情况下, OptionalDataException的长度字段设置为可立即从流中读取的原始数据的字节数, 并且eof字段设置为false.
 *
 *   <li>尝试通过类定义的readObject或readExternal方法读取可消耗数据的末尾. 在这种情况下, OptionalDataException的eof字段设置为true, length字段设置为0.
 * </ul>
 *
 * @author unascribed
 * @since JDK1.1
 */
public class OptionalDataException extends ObjectStreamException {

  private static final long serialVersionUID = -8011121865681257820L;

  /*
   * Create an <code>OptionalDataException</code> with a length.
   */
  OptionalDataException(int len) {
    eof = false;
    length = len;
  }

  /*
   * Create an <code>OptionalDataException</code> signifying no
   * more primitive data is available.
   */
  OptionalDataException(boolean end) {
    length = 0;
    eof = end;
  }

  /**
   * The number of bytes of primitive data available to be read in the current buffer.
   *
   * @serial
   */
  public int length;

  /**
   * True if there is no more data in the buffered part of the stream.
   *
   * @serial
   */
  public boolean eof;
}
