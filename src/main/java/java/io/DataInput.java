package java.io;

/**
 * {@code DataInput}接口提供了从二进制流中读取字节并从中重建任何Java原语类型的数据的功能. 还有一种用于从<a href="modified-utf-8">修改后的UTF-8</a>格式的数据中重建{@code
 * String}的功能.
 * <p>通常, 在此接口中的所有读取例程中, 如果在读取所需的字节数之前已到达文件末尾, 则会抛出{@code EOFException}(一种{@code IOException}).
 * 如果由于文件末尾以外的其他原因而无法读取任何字节, 则会抛出{@code EOFException}以外的{@code IOException}. 特别是, 如果输入流已关闭,
 * 则可能引发{@code IOException}.
 * <h3><a name="modified-utf-8">Modified UTF-8</a></h3>
 * <p>DataInput和DataOutput接口的实现表示Unicode字符串, 其格式是对UTF-8的略微修改.(有关标准UTF-8格式的信息, 请参阅<i> Unicode标准版本4.0
 * <i>的<i> 3.9 Unicode编码形式<i>). 请注意, 在下表中, 最高有效位出现在最左侧的列中.
 *
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="8" summary="位值和字节">
 * <tr>
 * <th colspan="9"><span style="font-weight:normal">
 * {@code '\u005Cu0001'}到{@code '\u005Cu007F'}范围内的所有字符均由一个字节表示:</span></th>
 * </tr>
 * <tr>
 * <td></td>
 * <th colspan="8" id="bit_a">Bit Values</th>
 * </tr>
 * <tr>
 * <th id="byte1_a">Byte 1</th>
 * <td><center>0</center>
 * <td colspan="7"><center>bits 6-0</center>
 * </tr>
 * <tr>
 * <th colspan="9"><span style="font-weight:normal">
 * The null character {@code '\u005Cu0000'} and characters in the range {@code '\u005Cu0080'} to
 * {@code '\u005Cu07FF'} are represented by a pair of bytes:</span></th>
 * </tr>
 * <tr>
 * <td></td>
 * <th colspan="8" id="bit_b">Bit Values</th>
 * </tr>
 * <tr>
 * <th id="byte1_b">Byte 1</th>
 * <td><center>1</center>
 * <td><center>1</center>
 * <td><center>0</center>
 * <td colspan="5"><center>bits 10-6</center>
 * </tr>
 * <tr>
 * <th id="byte2_a">Byte 2</th>
 * <td><center>1</center>
 * <td><center>0</center>
 * <td colspan="6"><center>bits 5-0</center>
 * </tr>
 * <tr>
 * <th colspan="9"><span style="font-weight:normal">
 * {@code char} values in the range {@code '\u005Cu0800'} to {@code '\u005CuFFFF'} are represented
 * by three bytes:</span></th>
 * </tr>
 * <tr>
 * <td></td>
 * <th colspan="8"id="bit_c">Bit Values</th>
 * </tr>
 * <tr>
 * <th id="byte1_c">Byte 1</th>
 * <td><center>1</center>
 * <td><center>1</center>
 * <td><center>1</center>
 * <td><center>0</center>
 * <td colspan="4"><center>bits 15-12</center>
 * </tr>
 * <tr>
 * <th id="byte2_b">Byte 2</th>
 * <td><center>1</center>
 * <td><center>0</center>
 * <td colspan="6"><center>bits 11-6</center>
 * </tr>
 * <tr>
 * <th id="byte3">Byte 3</th>
 * <td><center>1</center>
 * <td><center>0</center>
 * <td colspan="6"><center>bits 5-0</center>
 * </tr>
 * </table>
 * </blockquote>
 * <p>此格式与标准UTF-8格式之间的区别如下:
 * <ul>
 * <li>空字节{@code '\u005Cu0000'}是以2字节格式而不是1字节编码的, 因此编码字符串永远不会嵌入空值.
 * 因为C语言等语言程序中, 单字节空字符是用来标志字符串结尾的. 当已编码字符串放到这样的语言中处理, 一个嵌入的空字符将把字符串一刀两断.
 * <li>仅使用1字节, 2字节和3字节格式.
 * <li><a href="../lang/Character.html#unicode">补充字符</a>以代理对的形式表示.
 * </ul>
 *
 * @author Frank Yellin
 * @see java.io.DataInputStream
 * @see java.io.DataOutput
 * @since JDK1.0
 */
public
interface DataInput {

  /**
   * 从输入流中读取一些字节, 并将其存储到缓冲区数组{@code b}中. 读取的字节数等于{@code b}的长度.
   * <p>该方法将阻塞, 直到出现以下情况之一:
   * <ul>
   * <li>输入数据的{@code b.length}个字节可用, 在这种情况下将进行正常返回.
   * <li>检测到文件结尾, 在这种情况下, 将抛出{@code EOFException}.
   *
   * <li>发生IO错误, 在这种情况下会抛出{@code EOFException}以外的{@code IOException}.
   * </ul>
   * <p>如果{@code b}为{@code null}, 则会引发{@code NullPointerException}. 如果{@code b.length}为零, 则不读取任何字节.
   * 否则, 将读取的第一个字节存储到元素{@code b[0]}中, 将下一个字节存储到{@code b[1]}中, 依此类推.
   * 如果此方法引发异常, 则可能是{@code b}的某些但不是全部字节已使用来自输入流的数据更新了.
   *
   * @param b 读取数据的缓冲区.
   * @throws EOFException 如果此流在读取所有字节之前到达末尾.
   * @throws IOException  如果发生IO错误.
   */
  void readFully(byte[] b) throws IOException;

  /**
   * 从输入流中读取{@code len}个字节.
   * <p>该方法将阻塞, 直到出现以下情况之一:
   * <ul>
   * <li>输入数据的{@code len}个字节可用, 在这种情况下将进行正常返回.
   *
   * <li>检测到文件末尾, 在这种情况下将抛出{@code EOFException}.
   *
   * <li>发生IO错误, 在这种情况下会抛出{@code EOFException}以外的{@code IOException}.
   * </ul>
   * <p>如果{@code b}为{@code null}, 则会引发{@code NullPointerException}.
   * 如果{@code off}为负, 或者{@code len}为负, 或者{@code off + len}大于数组{@code b}的长度, 则会引发{@code IndexOutOfBoundsException}.
   * 如果{@code len}为零, 则不读取任何字节. 否则, 将读取的第一个字节存储到元素{@code b[off + 1]}中, 将下一个字节存储到{@code b[off + 1]}中, 依此类推.
   * 读取的字节数最多等于{@code len}.
   *
   * @param b   读取数据的缓冲区.
   * @param off 指定数据中的偏移量的int.
   * @param len 一个整数, 指定要读取的字节数.
   * @throws EOFException 如果此流在读取所有字节之前到达末尾.
   * @throws IOException  如果发生IO错误.
   */
  void readFully(byte[] b, int off, int len) throws IOException;

  /**
   * 尝试从输入流中跳过{@code n}个字节的数据, 并丢弃跳过的字节. 但是, 它可能会跳过一些较小的字节, 可能为零. 这可能是由多种条件引起的; 在跳过{@code
   * n}字节之前到达文件末尾只是一种可能. 此方法从不抛出{@code EOFException}. 返回跳过的实际字节数.
   *
   * @param n 要跳过的字节数.
   * @return 实际跳过的字节数.
   * @throws IOException 如果发生IO错误.
   */
  int skipBytes(int n) throws IOException;

  /**
   * Reads one input byte and returns {@code true} if that byte is nonzero, {@code false} if that
   * byte is zero. This method is suitable for reading the byte written by the {@code writeBoolean}
   * method of interface {@code DataOutput}.
   *
   * @return the {@code boolean} value read.
   * @throws EOFException if this stream reaches the end before reading all the bytes.
   * @throws IOException  if an I/O error occurs.
   */
  boolean readBoolean() throws IOException;

  /**
   * Reads and returns one input byte. The byte is treated as a signed value in the range {@code
   * -128} through {@code 127}, inclusive. This method is suitable for reading the byte written by
   * the {@code writeByte} method of interface {@code DataOutput}.
   *
   * @return the 8-bit value read.
   * @throws EOFException if this stream reaches the end before reading all the bytes.
   * @throws IOException  if an I/O error occurs.
   */
  byte readByte() throws IOException;

  /**
   * Reads one input byte, zero-extends it to type {@code int}, and returns the result, which is
   * therefore in the range {@code 0} through {@code 255}. This method is suitable for reading the
   * byte written by the {@code writeByte} method of interface {@code DataOutput} if the argument to
   * {@code writeByte} was intended to be a value in the range {@code 0} through {@code 255}.
   *
   * @return the unsigned 8-bit value read.
   * @throws EOFException if this stream reaches the end before reading all the bytes.
   * @throws IOException  if an I/O error occurs.
   */
  int readUnsignedByte() throws IOException;

  /**
   * Reads two input bytes and returns a {@code short} value. Let {@code a} be the first byte read
   * and {@code b} be the second byte. The value returned is:
   * <pre>{@code (short)((a << 8) | (b & 0xff))
   * }</pre>
   * This method is suitable for reading the bytes written by the {@code writeShort} method of
   * interface {@code DataOutput}.
   *
   * @return the 16-bit value read.
   * @throws EOFException if this stream reaches the end before reading all the bytes.
   * @throws IOException  if an I/O error occurs.
   */
  short readShort() throws IOException;

  /**
   * Reads two input bytes and returns an {@code int} value in the range {@code 0} through {@code
   * 65535}. Let {@code a} be the first byte read and {@code b} be the second byte. The value
   * returned is:
   * <pre>{@code (((a & 0xff) << 8) | (b & 0xff))
   * }</pre>
   * This method is suitable for reading the bytes written by the {@code writeShort} method of
   * interface {@code DataOutput}  if the argument to {@code writeShort} was intended to be a value
   * in the range {@code 0} through {@code 65535}.
   *
   * @return the unsigned 16-bit value read.
   * @throws EOFException if this stream reaches the end before reading all the bytes.
   * @throws IOException  if an I/O error occurs.
   */
  int readUnsignedShort() throws IOException;

  /**
   * Reads two input bytes and returns a {@code char} value. Let {@code a} be the first byte read
   * and {@code b} be the second byte. The value returned is:
   * <pre>{@code (char)((a << 8) | (b & 0xff))
   * }</pre>
   * This method is suitable for reading bytes written by the {@code writeChar} method of interface
   * {@code DataOutput}.
   *
   * @return the {@code char} value read.
   * @throws EOFException if this stream reaches the end before reading all the bytes.
   * @throws IOException  if an I/O error occurs.
   */
  char readChar() throws IOException;

  /**
   * Reads four input bytes and returns an {@code int} value. Let {@code a-d} be the first through
   * fourth bytes read. The value returned is:
   * <pre>{@code
   * (((a & 0xff) << 24) | ((b & 0xff) << 16) |
   *  ((c & 0xff) <<  8) | (d & 0xff))
   * }</pre>
   * This method is suitable for reading bytes written by the {@code writeInt} method of interface
   * {@code DataOutput}.
   *
   * @return the {@code int} value read.
   * @throws EOFException if this stream reaches the end before reading all the bytes.
   * @throws IOException  if an I/O error occurs.
   */
  int readInt() throws IOException;

  /**
   * Reads eight input bytes and returns a {@code long} value. Let {@code a-h} be the first through
   * eighth bytes read. The value returned is:
   * <pre>{@code
   * (((long)(a & 0xff) << 56) |
   *  ((long)(b & 0xff) << 48) |
   *  ((long)(c & 0xff) << 40) |
   *  ((long)(d & 0xff) << 32) |
   *  ((long)(e & 0xff) << 24) |
   *  ((long)(f & 0xff) << 16) |
   *  ((long)(g & 0xff) <<  8) |
   *  ((long)(h & 0xff)))
   * }</pre>
   * <p>
   * This method is suitable for reading bytes written by the {@code writeLong} method of interface
   * {@code DataOutput}.
   *
   * @return the {@code long} value read.
   * @throws EOFException if this stream reaches the end before reading all the bytes.
   * @throws IOException  if an I/O error occurs.
   */
  long readLong() throws IOException;

  /**
   * Reads four input bytes and returns a {@code float} value. It does this by first constructing an
   * {@code int} value in exactly the manner of the {@code readInt} method, then converting this
   * {@code int} value to a {@code float} in exactly the manner of the method {@code
   * Float.intBitsToFloat}. This method is suitable for reading bytes written by the {@code
   * writeFloat} method of interface {@code DataOutput}.
   *
   * @return the {@code float} value read.
   * @throws EOFException if this stream reaches the end before reading all the bytes.
   * @throws IOException  if an I/O error occurs.
   */
  float readFloat() throws IOException;

  /**
   * Reads eight input bytes and returns a {@code double} value. It does this by first constructing
   * a {@code long} value in exactly the manner of the {@code readLong} method, then converting this
   * {@code long} value to a {@code double} in exactly the manner of the method {@code
   * Double.longBitsToDouble}. This method is suitable for reading bytes written by the {@code
   * writeDouble} method of interface {@code DataOutput}.
   *
   * @return the {@code double} value read.
   * @throws EOFException if this stream reaches the end before reading all the bytes.
   * @throws IOException  if an I/O error occurs.
   */
  double readDouble() throws IOException;

  /**
   * Reads the next line of text from the input stream. It reads successive bytes, converting each
   * byte separately into a character, until it encounters a line terminator or end of file; the
   * characters read are then returned as a {@code String}. Note that because this method processes
   * bytes, it does not support input of the full Unicode character set.
   * <p>
   * If end of file is encountered before even one byte can be read, then {@code null} is returned.
   * Otherwise, each byte that is read is converted to type {@code char} by zero-extension. If the
   * character {@code '\n'} is encountered, it is discarded and reading ceases. If the character
   * {@code '\r'} is encountered, it is discarded and, if the following byte converts &#32;to the
   * character {@code '\n'}, then that is discarded also; reading then ceases. If end of file is
   * encountered before either of the characters {@code '\n'} and {@code '\r'} is encountered,
   * reading ceases. Once reading has ceased, a {@code String} is returned that contains all the
   * characters read and not discarded, taken in order. Note that every character in this string
   * will have a value less than {@code \u005Cu0100}, that is, {@code (char)256}.
   *
   * @return the next line of text from the input stream, or {@code null} if the end of file is
   * encountered before a byte can be read.
   * @throws IOException if an I/O error occurs.
   */
  String readLine() throws IOException;

  /**
   * Reads in a string that has been encoded using a
   * <a href="#modified-utf-8">modified UTF-8</a>
   * format. The general contract of {@code readUTF} is that it reads a representation of a Unicode
   * character string encoded in modified UTF-8 format; this string of characters is then returned
   * as a {@code String}.
   * <p>
   * First, two bytes are read and used to construct an unsigned 16-bit integer in exactly the
   * manner of the {@code readUnsignedShort} method . This integer value is called the
   * <i>UTF length</i> and specifies the number
   * of additional bytes to be read. These bytes are then converted to characters by considering
   * them in groups. The length of each group is computed from the value of the first byte of the
   * group. The byte following a group, if any, is the first byte of the next group.
   * <p>
   * If the first byte of a group matches the bit pattern {@code 0xxxxxxx} (where {@code x} means
   * "may be {@code 0} or {@code 1}"), then the group consists of just that byte. The byte is
   * zero-extended to form a character.
   * <p>
   * If the first byte of a group matches the bit pattern {@code 110xxxxx}, then the group consists
   * of that byte {@code a} and a second byte {@code b}. If there is no byte {@code b} (because byte
   * {@code a} was the last of the bytes to be read), or if byte {@code b} does not match the bit
   * pattern {@code 10xxxxxx}, then a {@code UTFDataFormatException} is thrown. Otherwise, the group
   * is converted to the character:
   * <pre>{@code (char)(((a & 0x1F) << 6) | (b & 0x3F))
   * }</pre>
   * If the first byte of a group matches the bit pattern {@code 1110xxxx}, then the group consists
   * of that byte {@code a} and two more bytes {@code b} and {@code c}. If there is no byte {@code
   * c} (because byte {@code a} was one of the last two of the bytes to be read), or either byte
   * {@code b} or byte {@code c} does not match the bit pattern {@code 10xxxxxx}, then a {@code
   * UTFDataFormatException} is thrown. Otherwise, the group is converted to the character:
   * <pre>{@code
   * (char)(((a & 0x0F) << 12) | ((b & 0x3F) << 6) | (c & 0x3F))
   * }</pre>
   * If the first byte of a group matches the pattern {@code 1111xxxx} or the pattern {@code
   * 10xxxxxx}, then a {@code UTFDataFormatException} is thrown.
   * <p>
   * If end of file is encountered at any time during this entire process, then an {@code
   * EOFException} is thrown.
   * <p>
   * After every group has been converted to a character by this process, the characters are
   * gathered, in the same order in which their corresponding groups were read from the input
   * stream, to form a {@code String}, which is returned.
   * <p>
   * The {@code writeUTF} method of interface {@code DataOutput} may be used to write data that is
   * suitable for reading by this method.
   *
   * @return a Unicode string.
   * @throws EOFException           if this stream reaches the end before reading all the bytes.
   * @throws IOException            if an I/O error occurs.
   * @throws UTFDataFormatException if the bytes do not represent a valid modified UTF-8 encoding of
   *                                a string.
   */
  String readUTF() throws IOException;
}
