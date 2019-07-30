package java.io;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import sun.nio.cs.StreamDecoder;


/**
 * InputStreamReader 是字节流和字符流之间的桥梁: 它读取字节并使用指定的 {@link
 * java.nio.charset.Charset charest} 编码来解码它们. 字符集的使用可以通过名称
 * 指定或者可以明确的给出, 或者可以接受平台的默认字符集.
 *
 * <p> 每次调用一个 InputStreamReader 的 read() 方法都可能导致从底层输入流中读取
 * 一个或者多个字节. 为了能过有效地将字节转换为字符, 可以从基础流中提取可以满足当前操作
 * 所需的更多字节.
 *
 * <p> 为了获得更高的效率, 请考虑在 BufferedReader 中包装 InputStreamReader.
 * 例如:
 *
 * <pre>
 * BufferedReader in
 *   = new BufferedReader(new InputStreamReader(System.in));
 * </pre>
 *
 * @author Mark Reinhold
 * @see BufferedReader
 * @see InputStream
 * @see java.nio.charset.Charset
 * @since JDK1.1
 */

public class InputStreamReader extends Reader {

    private final StreamDecoder sd;

    /**
     * 使用默认的字符集创建一个 InputStreamReader.
     *
     * @param in 一个输入流
     */
    public InputStreamReader(InputStream in) {
        super(in);
        try {
            // 流编码器, 它也是一个 Reader 的子类
            sd = StreamDecoder.forInputStreamReader(in, this, (String) null); // ## check lock object
        } catch (UnsupportedEncodingException e) {
            // 默认编码应始终可用
            throw new Error(e);
        }
    }

    /**
     * 使用指定的字符集创建一个 InputStreamReader.
     *
     * @param in          一个输入流
     * @param charsetName 支持的字符集
     *                    {@link java.nio.charset.Charset charset} 的名字
     * @throws UnsupportedEncodingException 如果字符集不支持
     */
    public InputStreamReader(InputStream in, String charsetName)
            throws UnsupportedEncodingException {
        super(in);
        if (charsetName == null) {
            throw new NullPointerException("charsetName");
        }
        sd = StreamDecoder.forInputStreamReader(in, this, charsetName);
    }

    /**
     * 使用指定的字符集创建一个 InputStreamReader.
     *
     * @param in 输入流
     * @param cs 一个字符集
     * @spec JSR-51
     * @since 1.4
     */
    public InputStreamReader(InputStream in, Charset cs) {
        super(in);
        if (cs == null) {
            throw new NullPointerException("charset");
        }
        sd = StreamDecoder.forInputStreamReader(in, this, cs);
    }

    /**
     * 使用给定的字符集解码器创建一个 InputStreamReader.
     *
     * @param in  输入流
     * @param dec 字符集解码器
     * @spec JSR-51
     * @since 1.4
     */
    public InputStreamReader(InputStream in, CharsetDecoder dec) {
        super(in);
        if (dec == null) {
            throw new NullPointerException("charset decoder");
        }
        sd = StreamDecoder.forInputStreamReader(in, this, dec);
    }

    /**
     * 返回这个流使用的字符集解码器的名字.
     *
     * <p> 如果编码具有历史名字, 则返回该名称; 否则, 返回编码的规范名称.
     *
     * <p> 如果此实例使用 {@link #InputStreamReader(InputStream, String)}
     * 创建则返回传入的字符集名称, 对于编码而言是唯一的, 可能与传递给构造函数的名称不同.
     * 这个方法将会返回 null, 如果流已经关闭了.
     * </p>
     *
     * @return 编码的历史名字, 如果流已经关闭了则返回 null .
     * @revised 1.4
     * @spec JSR-51
     * @see java.nio.charset.Charset
     */
    public String getEncoding() {
        return sd.getEncoding();
    }

    /**
     * 读取一个单一的字符.
     *
     * @return 读取到的字符, 或者流到达末尾返回 -1.
     * @throws IOException 如果发生 I/O 错误
     */
    @Override
    public int read() throws IOException {
        return sd.read();
    }

    /**
     * 读取一些字符到数组的指定位置.
     *
     * @param cbuf   目标缓冲区
     * @param offset 开始存储字符的偏移量
     * @param length 读取到的字符数量
     * @return 添加到缓冲区的字符数, 如果此字符源位于其末尾, 则返回-1
     * @throws IOException 如果发生了一个 I/O 错误
     */
    @Override
    public int read(char cbuf[], int offset, int length) throws IOException {
        return sd.read(cbuf, offset, length);
    }

    /**
     * 判断此流是否可以读取. 如果 InputSteamReader 的输入缓冲区不为空, 或者可以从
     * 底层字节流中读取字节, 则它就准备就绪, 可以被读取.
     *
     * @throws IOException 如果发生 I/O 错误
     */
    @Override
    public boolean ready() throws IOException {
        return sd.ready();
    }

    @Override
    public void close() throws IOException {
        sd.close();
    }
}
