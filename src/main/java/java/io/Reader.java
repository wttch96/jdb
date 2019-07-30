package java.io;


/**
 * 读取字符流的抽象类. 子类必须实现的唯一方法是 read(char[], int, int) 和 close().
 * 但是, 大多数子类讲覆盖此处定义的一些方法，以便提供更高的效率, 添加附加功能或者两者.
 *
 * @author Mark Reinhold
 * @see BufferedReader
 * @see LineNumberReader
 * @see CharArrayReader
 * @see InputStreamReader
 * @see FileReader
 * @see FilterReader
 * @see PushbackReader
 * @see PipedReader
 * @see StringReader
 * @see Writer
 * @since JDK1.1
 */

public abstract class Reader implements Readable, Closeable {

    /**
     * 用于同步此流上的操作的对象. 为了提高效率, 字符流对象可以使用除自身之外的对象来
     * 保护关键部分. 因此, 子类应该使用此字段中的对象而不是 <tt>this</tt> 或
     * synchronized 方法.
     */
    protected Object lock;

    /**
     * 创建一个新的字符流阅读器, 其关键部分将在阅读器本身上同步.
     */
    protected Reader() {
        this.lock = this;
    }

    /**
     * 创建一个新的字符流阅读器, 其关键部分将在给定对象上同步.
     *
     * @param lock 同步时要使用的对象.
     */
    protected Reader(Object lock) {
        if (lock == null) {
            throw new NullPointerException();
        }
        this.lock = lock;
    }

    /**
     * 试图读取字符到指定的字符缓冲区. 缓冲区按原样用作字符存储库:
     * 唯一的更改是 put 操作的结果. 不执行缓冲区的翻转或逆序.
     *
     * @param target 用于读取字符的缓冲区
     * @return 添加到缓冲区的字符数, 如果此字符源位于其末尾, 则返回-1
     * @throws IOException                      如果一个 I/O 错误发生
     * @throws NullPointerException             目标为空
     * @throws java.nio.ReadOnlyBufferException 目标只读
     * @see #read(char[], int, int)
     * @since 1.5
     */
    @Override
    public int read(java.nio.CharBuffer target) throws IOException {
        // buffer 可以容纳的字符数
        int len = target.remaining();
        char[] cbuf = new char[len];
        int n = read(cbuf, 0, len);
        if (n > 0) {
            target.put(cbuf, 0, n);
        }
        return n;
    }

    /**
     * 读一个字符. 此方法将阻塞, 直到字符可用, 发生 I/O 错误或到达流的末尾.
     *
     * <p> 打算支持高效单字符输入的子类应该重写此方法.
     *
     * @return 读到的字符, 一个 0 到 65535 大小的整型(<tt>0x00-0xFFFF</tt>),
     * 或者流到达末尾时返回 -1
     * @throws IOException 如果发生了一个 I/O 错误
     * @see #read(char[], int, int)
     */
    public int read() throws IOException {
        char cb[] = new char[1];
        if (read(cb, 0, 1) == -1) {
            return -1;
        } else {
            return cb[0];
        }
    }

    /**
     * 读一个字符数组. 此方法将阻塞, 直到字符可用, 发生 I/O 错误或到达流的末尾.
     *
     * @param cbuf 目标缓冲区
     * @return 添加到缓冲区的字符数, 如果此字符源位于其末尾, 则返回-1
     * @throws IOException 如果发生了一个 I/O 错误
     * @see #read(char[], int, int)
     */
    public int read(char cbuf[]) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }

    /**
     * 读取字符到数组的指定位置. 此方法将阻塞, 直到字符可用, 发生 I/O 错误或到达流的末尾.
     * wttch: 该抽象类必须实现的方法, 不像输入流那样, Reader 的 {@link #read()} {@link #read(char[])}
     * 等方法都是通过该函数实现的(读取数组长度为1的字符数组, 或不指定偏移量).
     *
     * @param cbuf 目标缓冲区
     * @param off  开始存储字符的偏移量
     * @param len  读取到的字符数量
     * @return 添加到缓冲区的字符数, 如果此字符源位于其末尾, 则返回-1
     * @throws IOException 如果发生了一个 I/O 错误
     */
    abstract public int read(char cbuf[], int off, int len) throws IOException;

    /**
     * Maximum skip-buffer size
     */
    private static final int maxSkipBufferSize = 8192;

    /**
     * Skip buffer, null until allocated
     */
    private char skipBuffer[] = null;

    /**
     * Skips characters.  This method will block until some characters are
     * available, an I/O error occurs, or the end of the stream is reached.
     *
     * @param n The number of characters to skip
     * @return The number of characters actually skipped
     * @throws IllegalArgumentException If <code>n</code> is negative.
     * @throws IOException              If an I/O error occurs
     */
    public long skip(long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException("skip value is negative");
        }
        int nn = (int) Math.min(n, maxSkipBufferSize);
        synchronized (lock) {
            if ((skipBuffer == null) || (skipBuffer.length < nn)) {
                skipBuffer = new char[nn];
            }
            long r = n;
            while (r > 0) {
                int nc = read(skipBuffer, 0, (int) Math.min(r, nn));
                if (nc == -1) {
                    break;
                }
                r -= nc;
            }
            return n - r;
        }
    }

    /**
     * Tells whether this stream is ready to be read.
     *
     * @return True if the next read() is guaranteed not to block for input,
     * false otherwise.  Note that returning false does not guarantee that the
     * next read will block.
     * @throws IOException If an I/O error occurs
     */
    public boolean ready() throws IOException {
        return false;
    }

    /**
     * Tells whether this stream supports the mark() operation. The default
     * implementation always returns false. Subclasses should override this
     * method.
     *
     * @return true if and only if this stream supports the mark operation.
     */
    public boolean markSupported() {
        return false;
    }

    /**
     * Marks the present position in the stream.  Subsequent calls to reset()
     * will attempt to reposition the stream to this point.  Not all
     * character-input streams support the mark() operation.
     *
     * @param readAheadLimit Limit on the number of characters that may be
     *                       read while still preserving the mark.  After
     *                       reading this many characters, attempting to
     *                       reset the stream may fail.
     * @throws IOException If the stream does not support mark(),
     *                     or if some other I/O error occurs
     */
    public void mark(int readAheadLimit) throws IOException {
        throw new IOException("mark() not supported");
    }

    /**
     * Resets the stream.  If the stream has been marked, then attempt to
     * reposition it at the mark.  If the stream has not been marked, then
     * attempt to reset it in some way appropriate to the particular stream,
     * for example by repositioning it to its starting point.  Not all
     * character-input streams support the reset() operation, and some support
     * reset() without supporting mark().
     *
     * @throws IOException If the stream has not been marked,
     *                     or if the mark has been invalidated,
     *                     or if the stream does not support reset(),
     *                     or if some other I/O error occurs
     */
    public void reset() throws IOException {
        throw new IOException("reset() not supported");
    }

    /**
     * Closes the stream and releases any system resources associated with
     * it.  Once the stream has been closed, further read(), ready(),
     * mark(), reset(), or skip() invocations will throw an IOException.
     * Closing a previously closed stream has no effect.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    abstract public void close() throws IOException;

}
