package java.io;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * <code>BufferedInputStream</code> 添加一些新的功能到另一个输入流 - 即
 * 换缓冲输入并支持 <code>mark</code> 和 <code>reset</code> 方法的功能.
 * 创建 <code>BufferedInputStream</code> 时, 会创建一个内部缓冲区数组.
 * 当读取或跳过来自流的字节时, 内部缓冲区根据需要从包含的输入流中重新填充,
 * 一次多个字节. <code>mark</code> 操作会记住输入流中的一个点, <code>reset</code>
 * 操作会导致自最近的 <code>mark</code> 操作以来读取所有的字节被重读从包含的
 * 输入流中获取新的字节.
 *
 * @author Arthur van Hoff
 * @since JDK1.0
 */
public
class BufferedInputStream extends FilterInputStream {

    /**
     * 默认的最大缓冲区大小
     */
    private static int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * 要分配的最大数组大小. 一些 VM 在阵列中保留了一些标题字.
     * 尝试分配更大的数组可能会导致 OutOfMemoryError:
     * Requested array size exceeds VM limit
     */
    private static int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 存储数据的内部缓冲数组. 必要时, 它可以被另一个不同大小的数组替换.
     */
    protected volatile byte buf[];

    /**
     * 原子更新器为 buf 提供了 CAS 的功能.
     * 这是必要的, 因为关闭可以是异步的.
     * 我们使用 buf[] 的 null 值作为此流关闭的主要指标.
     * (关闭时, "in"字段也会被删除.)
     */
    private static final
    AtomicReferenceFieldUpdater<BufferedInputStream, byte[]> bufUpdater =
            AtomicReferenceFieldUpdater.newUpdater
                    (BufferedInputStream.class, byte[].class, "buf");

    /**
     * 大于缓冲区中最后一个有效字节的索引.
     * 这个值一直在 <code>0</code> 到 <code>buf.length</code> 之间;
     * 元素 <code>buf[0]</code> 到 <code>buf[count-1]</code> 包含从底层输入流获得的
     * 缓冲输入数据.
     */
    protected int count;

    /**
     * 缓冲区的当前位置. 下一个将从 <code>buf</code> 数组中读取的字节的下标索引.
     * <p>
     * 该值始终在 <code>0</code> 到 <code>count</code> 范围之间. 如果它小于
     * <code>count</code>, 则 <code>buf[pos]</code>将作为输入下一个提供的字符;
     * 如果等于 <code>count</code>, 那么下一次 <code>read</code> 或 <code>skip</code>
     * 操作将需要从包含的输入流中读取更多的字节.
     *
     * @see java.io.BufferedInputStream#buf
     */
    protected int pos;

    /**
     * 上一次调用 <code>mark</code> 方法时 <code>pos</code> 字段的值.
     * <p>
     * 该值始终在 <code>-1</code> 和 <code>pos</code> 范围之间.
     * 如果输入流没有标记位置, 该字段是 <code>-1</code>.
     * 如果输入流有标记位置, 那么 <code>buf[markpos]</code> 将作为 <code>reset</code>
     * 方法调用之后输入流提供的第一个字节. 如果 <code>markpos</code> 不是 <code>-1</code>,
     * 那么来自位置 <code>buf[markpos]</code> 到 <code>buf[pos-1]</code> 的所有字节
     * 必须保留在缓冲区数组中(尽管可以将他们移动到缓冲区数组中的另一个位置, 并适当调整 <code>count</code>,
     * <code>pos</code> 和 <code>markpos</code> 的值); 除非直到 <code>pos</code> 和 <code>markpos</code>
     * 之间的差异超过<code>marklimit</code>, 否则它们不会被丢弃.
     *
     * @see java.io.BufferedInputStream#mark(int)
     * @see java.io.BufferedInputStream#pos
     */
    protected int markpos = -1;

    /**
     * 在后续调用 <code>reset</code> 方法失败之前调用 <code>mark</code>
     * 方法后允许的最大预读. 只要 <code>pos</code> 和 <code>markpos</code>
     * 之间的差异超过 <code>marklimit</code>, 就可以通过将 <code>markpos</code>
     * 设置为 <code>-1</code> 来删除标记.
     *
     * @see java.io.BufferedInputStream#mark(int)
     * @see java.io.BufferedInputStream#reset()
     */
    protected int marklimit;

    /**
     * 检测以确保输入流由于关闭而未被清除;如果没被清除则返回它;
     */
    private InputStream getInIfOpen() throws IOException {
        InputStream input = in;
        if (input == null) {
            throw new IOException("Stream closed");
        }
        return input;
    }

    /**
     * 检测以确保缓冲区由于关闭而未被清除;如果没被清除则返回它;
     */
    private byte[] getBufIfOpen() throws IOException {
        byte[] buffer = buf;
        if (buffer == null) {
            throw new IOException("Stream closed");
        }
        return buffer;
    }

    /**
     * 创建 <code>BufferedInputStream</code> 并保存其参数,
     * 即输入流 <code>in</code> , 供以后使用. 创建内部缓冲区数组
     * 并将其存储在 <code>buf</code> 中.
     *
     * @param in 底层输入流.
     */
    public BufferedInputStream(InputStream in) {
        this(in, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 使用指定的缓冲大小创建 <code>BufferedInputStream</code>
     * 并保存其参数, 即输入流 <code>in</code>, 供以后使用.
     * 一个 <code>size</code> 长度的内部缓冲数组并将其存储在 <code>buf</code> 中.
     *
     * @param in   底层输入流.
     * @param size 缓冲大小.
     * @throws IllegalArgumentException if {@code size <= 0}.
     */
    public BufferedInputStream(InputStream in, int size) {
        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        buf = new byte[size];
    }

    /**
     * 使用更多数组填充缓冲区, 考虑到用于处理标记的混洗和其他技巧.
     * 假设它是由同步方法调用的. 此方法还假定已读取 in 的所有数据,
     * 因此 pos > count.
     */
    private void fill() throws IOException {
        byte[] buffer = getBufIfOpen();
        if (markpos < 0) {
            pos = 0;            /* 没有标记: 扔掉缓冲区 */
        } else if (pos >= buffer.length)  /* 没有留在缓冲区的空间 */ {
            if (markpos > 0) {  /* 可以扔掉早期的缓冲区 */
                // 扔掉早期的缓冲区, 将 markpos 到 pos 的缓冲复制到 0 到 (pos - markpos) 的位置
                int sz = pos - markpos;
                System.arraycopy(buffer, markpos, buffer, 0, sz);
                pos = sz;
                markpos = 0;
            } else if (buffer.length >= marklimit) {
                // pos >= buffer.length >= marklimit, pos 已经超过了 marklimit 限制, 标记无效
                markpos = -1;   /* 缓冲区太大, 标记无效 */
                pos = 0;        /* 删除缓冲区内容 */
            } else if (buffer.length >= MAX_BUFFER_SIZE) {
                // 大于允许的最大缓冲区大小
                throw new OutOfMemoryError("Required array size too large");
            } else {            /* 增加缓冲区大小 */
                // markpos <= 0 && buffer.length < marklimit && buffer.length < MAX_BUFFER_SIZE
                // 倍增缓冲区大小, 最大 MAX_BUFFER_SIZE
                int nsz = (pos <= MAX_BUFFER_SIZE - pos) ?
                        pos * 2 : MAX_BUFFER_SIZE;
                // 最大只到 marklimit
                if (nsz > marklimit) {
                    nsz = marklimit;
                }
                byte nbuf[] = new byte[nsz];
                // 拷贝之前缓冲到新的缓冲区去
                System.arraycopy(buffer, 0, nbuf, 0, pos);
                if (!bufUpdater.compareAndSet(this, buffer, nbuf)) {
                    // 如果存在异步关闭, 则无法替换 buf.
                    // 注意: 如果多个线程可以访问 fill(), 则需要更改此值.
                    // 但是现在, CAS 失败的唯一方法是通过关闭.
                    // 断言 buf == null;
                    throw new IOException("Stream closed");
                }
                buffer = nbuf;
            }
        }
        // count 等于当前的位置
        count = pos;
        // 填充整个缓冲区
        int n = getInIfOpen().read(buffer, pos, buffer.length - pos);
        if (n > 0) {
            count = n + pos;
        }
    }

    /**
     * 参阅 <code>InputStream</code> 的 <code>read</code> 方法.
     *
     * @return 数据的下一个字节, 或者到达流的末尾时返回 <code>-1</code>.
     * @throws IOException 如果流通过调用 {@link #close()} 方法被关闭, 或者出现 I/O 错误.
     * @see java.io.FilterInputStream#in
     */
    @Override
    public synchronized int read() throws IOException {
        if (pos >= count) {
            // 不够用了? 填充缓冲区
            fill();
            // 还不够? 可能没东西了
            if (pos >= count) {
                return -1;
            }
        }
        // 把 pos 位置的字节取低 8 位 转换为 int 类型的值
        return getBufIfOpen()[pos++] & 0xff;
    }

    /**
     * 将字符读入数组的一部分, 必要时最多从基础流中读取一次.
     */
    private int read1(byte[] b, int off, int len) throws IOException {
        // 可以读取的长度
        int avail = count - pos;
        if (avail <= 0) {
            /* 如果请求的长度至少与缓冲区一样大, 并且没有 mark/reset 活动,
               则不必费心将字节复制到本地缓冲区.
               通过这种方式, 缓冲流将无害地级联.*/
            if (len >= getBufIfOpen().length && markpos < 0) {
                return getInIfOpen().read(b, off, len);
            }
            // len < getBufIfOpen().length || markpos >= 0
            // 长度没超过缓冲区, 或者存在标记
            fill();
            avail = count - pos;
            if (avail <= 0) {
                return -1;
            }
        }
        // 长度和可读大小取最小值 cnt , 读取 cnt 长度的字节
        int cnt = (avail < len) ? avail : len;
        System.arraycopy(getBufIfOpen(), pos, b, off, cnt);
        pos += cnt;
        return cnt;
    }

    /**
     * 从给定的偏移量开始, 将字节输入流中的字节读入指定的字节数组.
     *
     * <p>该方法实现了 <code>{@link InputStream}</code> 类
     * <code>{@link InputStream#read(byte[], int, int) read}</code>
     * 方法的一般契约. 作为额外的便利, 它试图通过反复调用底层流的 <code>read</code>
     * 方法来读取尽可能多的字节. 此迭代 <code>read</code> 直到满足以下条件之一为止: <ul>
     *
     * <li> 已读取指定的字节数,
     *
     * <li> 基础流的 <code>read</code> 方法返回 <code>-1</code>,
     * 表示文件结束, 或者
     *
     * <li> 基础流的 <code>available</code> 方法返回零, 表示进一步的输入请求被阻止.
     *
     * </ul> 基础流的 <code>available</code> 方法如果基础流上的第一个
     * <code>read</code> 返回 <code>-1</code>以指示文件结束, 则此方法返回 <code>-1</code>.
     * 否则, 此方法返回实际读取的字节数. 返回零, 表示进一步的输入请求将被阻止.
     *
     * <p> 鼓励但不要就此类的子类尝试以相同的方式读取尽可能多的字节.
     *
     * @param b   目标缓冲区.
     * @param off 开始存储字节的偏移量.
     * @param len 读取字节的最大数量.
     * @return 读取的字节数, 如果已到达流的末尾, 则为 <code>-1</code>.
     * @throws IOException 如果流通过调用 {@link #close()} 方法被关闭, 或者出现 I/O 错误.
     */
    @Override
    public synchronized int read(byte b[], int off, int len)
            throws IOException {
        getBufIfOpen(); // 检查是否关闭流
        if ((off | len | (off + len) | (b.length - (off + len))) < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int n = 0;
        for (; ; ) {
            int nread = read1(b, off + n, len - n);// nread 已读取的字节数
            if (nread <= 0) {
                // 读取到达末尾
                return (n == 0) ? nread : n;
            }
            n += nread; // 统计字节数
            if (n >= len) {
                return n;
            }
            // 如果没有关闭但没有可用的字节, 则返回
            InputStream input = in;
            if (input != null && input.available() <= 0) {
                return n;
            }
        }
    }

    /**
     * 请参阅 <code>InputStream</code> 类的 <code>skip</code> 方法的一般合约.
     *
     * @throws IOException 如果流不支持 seek, 或者通过调用其 {@link #close()} 方法
     *                     关闭此输入流, 或者发生 I/O 错误.
     */
    @Override
    public synchronized long skip(long n) throws IOException {
        getBufIfOpen(); //检查是否是关闭的流
        if (n <= 0) {
            return 0;
        }
        long avail = count - pos;

        if (avail <= 0) {
            // 如果没有设置标记位置, 则不要保留缓冲区
            if (markpos < 0) {
                return getInIfOpen().skip(n);
            }

            // 填写缓冲区以保存字节以进行重置
            fill();
            avail = count - pos;
            if (avail <= 0) {
                return 0;
            }
        }

        long skipped = (avail < n) ? avail : n;
        pos += skipped;
        return skipped;
    }

    /**
     * 返回可以从此输入流中读取(或跳过)的字节数的估计值, 而不会被下一次调用此输入流的方法阻塞.
     * 下一次调用可能是同一个线程或另一个线程. 单个读取或跳过这么多字节不会阻塞, 但可以读取或跳过更少的字节.
     * <p>
     * 此方法返回缓冲区中剩余要读取的字节数 (<code>count&nbsp;- pos</code>) 和
     * 调用 {@link java.io.FilterInputStream#in in}.available() 的结果之和.
     *
     * @return 估计可以从此输入流中无阻塞地读取(或跳过)的字节数.
     * @throws IOException 如果输入流通过调用其 {@link #close()} 方法关闭此输入流, 或者发生 I/O 错误.
     */
    @Override
    public synchronized int available() throws IOException {
        int n = count - pos;
        int avail = getInIfOpen().available();
        return n > (Integer.MAX_VALUE - avail)
                ? Integer.MAX_VALUE
                : n + avail;
    }

    /**
     * See the general contract of the <code>mark</code>
     * method of <code>InputStream</code>.
     *
     * @param readlimit the maximum limit of bytes that can be read before
     *                  the mark position becomes invalid.
     * @see java.io.BufferedInputStream#reset()
     */
    @Override
    public synchronized void mark(int readlimit) {
        marklimit = readlimit;
        markpos = pos;
    }

    /**
     * See the general contract of the <code>reset</code>
     * method of <code>InputStream</code>.
     * <p>
     * If <code>markpos</code> is <code>-1</code>
     * (no mark has been set or the mark has been
     * invalidated), an <code>IOException</code>
     * is thrown. Otherwise, <code>pos</code> is
     * set equal to <code>markpos</code>.
     *
     * @throws IOException if this stream has not been marked or,
     *                     if the mark has been invalidated, or the stream
     *                     has been closed by invoking its {@link #close()}
     *                     method, or an I/O error occurs.
     * @see java.io.BufferedInputStream#mark(int)
     */
    @Override
    public synchronized void reset() throws IOException {
        getBufIfOpen(); // Cause exception if closed
        if (markpos < 0) {
            throw new IOException("Resetting to invalid mark");
        }
        pos = markpos;
    }

    /**
     * 测试这个输入流是否支持 <code>mark</code> 和 <code>reset</code> 方法.
     * <code>BufferedInputStream</code> 的 <code>markSupported</code> 方法
     * 返回 <code>true</code> .
     *
     * @return 一个 <code>boolean</code> 值表明此输入流是否支持 <code>mark</code> 和 <code>reset</code> 方法.
     * @see java.io.InputStream#mark(int)
     * @see java.io.InputStream#reset()
     */
    @Override
    public boolean markSupported() {
        return true;
    }

    /**
     * 关闭输入流并且释放与流相关的所有系统资源.
     * 一旦流被关闭, 进一步的 read(), available(), reset() 或 skip() 调用将抛出 IOException.
     * close() 已经关闭的流不受影响, Wttch: 即幂等性.
     *
     * @throws IOException 如果一个 I/O 错误发生了.
     */
    @Override
    public void close() throws IOException {
        byte[] buffer;
        // 保证了 close 的幂等性
        while ((buffer = buf) != null) {
            // 删除 buf 数组
            if (bufUpdater.compareAndSet(this, buffer, null)) {
                InputStream input = in;
                in = null;
                if (input != null) {
                    input.close();
                }
                return;
            }
            // 如果新的buf在fill() 中被 CAS 化, 则重试
        }
    }
}
