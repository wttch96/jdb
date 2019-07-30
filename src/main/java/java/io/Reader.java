package java.io;


/**
 * 读取字符流的抽象类. 子类必须实现的唯一方法是 read(char[], int, int) 和 close().
 * 但是, 大多数子类讲覆盖此处定义的一些方法, 以便提供更高的效率, 添加附加功能或者两者.
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
     * 最大的可跳过 buffer 的大小
     */
    private static final int maxSkipBufferSize = 8192;

    /**
     * 跳过缓冲区, 在分配之前为 null
     */
    private char skipBuffer[] = null;

    /**
     * 跳过一些字符. 此方法将阻塞, 直到字符可用, 发生 I/O 错误或到达流的末尾.
     * wttch: 还是用 {@link #read(char[], int, int)} 方法, 直接抛弃所读到的东西.
     *
     * @param n 要跳过的字符数
     * @return 实际跳过的字符数
     * @throws IllegalArgumentException 如果 <code>n</code> 为负数.
     * @throws IOException              如果发生 I/O 错误
     */
    public long skip(long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException("skip value is negative");
        }
        int nn = (int) Math.min(n, maxSkipBufferSize); // 不能超过最大可跳过数
        synchronized (lock) {
            // 加锁
            if ((skipBuffer == null) || (skipBuffer.length < nn)) {
                // skip buffer 不够用
                skipBuffer = new char[nn];
            }
            long r = n; // 反向计数
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
     * 判断此流是否可以读取.
     *
     * @return 如果保证下一个 read() 不阻止输入, 则返回 true, 否则返回 false.
     * 请注意, 返回 false 并不能保证下一次读取将一定被阻止.
     * @throws IOException If an I/O error occurs
     */
    public boolean ready() throws IOException {
        return false;
    }

    /**
     * 判断此流是否支持 mark() 操作.
     * 默认实现始终返回 false. 子类应该重写此方法.
     *
     * @return 如果此流支持 mark() 操作则返回 true.
     */
    public boolean markSupported() {
        return false;
    }

    /**
     * 标记流中的当前位置. 对 reset() 的后续调用将尝试将流重新定位到此点.
     * 并非所有字符输入流都支持 mark() 操作.
     *
     * @param readAheadLimit 在保留标记的同时限制可以读取的字符数. 读取这么多字符后,
     *                       reset 此流可能会失败.
     * @throws IOException 如果此流不支持 mark() 操作, 或者发生了 I/O 错误.
     */
    public void mark(int readAheadLimit) throws IOException {
        throw new IOException("mark() not supported");
    }

    /**
     * 重置此流. 如果流被标记, 尝试在标记位置复位它. 如果流没有被标记, 然后尝试以某种
     * 适合于特定流的方式重置它, 例如通过将其重新定位到其起始点.
     * wttch: 主要看子类如何实现它.
     * 不是所有的字符输入流都支持 reset() 操作, 并且有些 reset() 操作并不需要 mark().
     *
     * @throws IOException 如果流没有被标记, 或者标记无效, 或者流不支持 reset(),
     *                     或者发生了什么 I/O 错误
     */
    public void reset() throws IOException {
        throw new IOException("reset() not supported");
    }

    /**
     * 关闭流并释放相关的系统资源. 流一旦关闭, 进一步的 read(), ready(), mark(),
     * reset() 和 skip() 调用将会抛出 IOException.
     * 关闭一个已经关闭的流没有影响.
     *
     * @throws IOException 如果发生 I/O 错误
     */
    @Override
    abstract public void close() throws IOException;

}
