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
     * The current position in the buffer. This is the index of the next
     * character to be read from the <code>buf</code> array.
     * <p>
     * This value is always in the range <code>0</code>
     * through <code>count</code>. If it is less
     * than <code>count</code>, then  <code>buf[pos]</code>
     * is the next byte to be supplied as input;
     * if it is equal to <code>count</code>, then
     * the  next <code>read</code> or <code>skip</code>
     * operation will require more bytes to be
     * read from the contained  input stream.
     *
     * @see java.io.BufferedInputStream#buf
     */
    protected int pos;

    /**
     * The value of the <code>pos</code> field at the time the last
     * <code>mark</code> method was called.
     * <p>
     * This value is always
     * in the range <code>-1</code> through <code>pos</code>.
     * If there is no marked position in  the input
     * stream, this field is <code>-1</code>. If
     * there is a marked position in the input
     * stream,  then <code>buf[markpos]</code>
     * is the first byte to be supplied as input
     * after a <code>reset</code> operation. If
     * <code>markpos</code> is not <code>-1</code>,
     * then all bytes from positions <code>buf[markpos]</code>
     * through  <code>buf[pos-1]</code> must remain
     * in the buffer array (though they may be
     * moved to  another place in the buffer array,
     * with suitable adjustments to the values
     * of <code>count</code>,  <code>pos</code>,
     * and <code>markpos</code>); they may not
     * be discarded unless and until the difference
     * between <code>pos</code> and <code>markpos</code>
     * exceeds <code>marklimit</code>.
     *
     * @see java.io.BufferedInputStream#mark(int)
     * @see java.io.BufferedInputStream#pos
     */
    protected int markpos = -1;

    /**
     * The maximum read ahead allowed after a call to the
     * <code>mark</code> method before subsequent calls to the
     * <code>reset</code> method fail.
     * Whenever the difference between <code>pos</code>
     * and <code>markpos</code> exceeds <code>marklimit</code>,
     * then the  mark may be dropped by setting
     * <code>markpos</code> to <code>-1</code>.
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
     * Creates a <code>BufferedInputStream</code>
     * and saves its  argument, the input stream
     * <code>in</code>, for later use. An internal
     * buffer array is created and  stored in <code>buf</code>.
     *
     * @param in the underlying input stream.
     */
    public BufferedInputStream(InputStream in) {
        this(in, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Creates a <code>BufferedInputStream</code>
     * with the specified buffer size,
     * and saves its  argument, the input stream
     * <code>in</code>, for later use.  An internal
     * buffer array of length  <code>size</code>
     * is created and stored in <code>buf</code>.
     *
     * @param in   the underlying input stream.
     * @param size the buffer size.
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
     * Fills the buffer with more data, taking into account
     * shuffling and other tricks for dealing with marks.
     * Assumes that it is being called by a synchronized method.
     * This method also assumes that all data has already been read in,
     * hence pos > count.
     */
    private void fill() throws IOException {
        byte[] buffer = getBufIfOpen();
        if (markpos < 0) {
            pos = 0;            /* no mark: throw away the buffer */
        } else if (pos >= buffer.length)  /* no room left in buffer */ {
            if (markpos > 0) {  /* can throw away early part of the buffer */
                int sz = pos - markpos;
                System.arraycopy(buffer, markpos, buffer, 0, sz);
                pos = sz;
                markpos = 0;
            } else if (buffer.length >= marklimit) {
                markpos = -1;   /* buffer got too big, invalidate mark */
                pos = 0;        /* drop buffer contents */
            } else if (buffer.length >= MAX_BUFFER_SIZE) {
                throw new OutOfMemoryError("Required array size too large");
            } else {            /* grow buffer */
                int nsz = (pos <= MAX_BUFFER_SIZE - pos) ?
                        pos * 2 : MAX_BUFFER_SIZE;
                if (nsz > marklimit) {
                    nsz = marklimit;
                }
                byte nbuf[] = new byte[nsz];
                System.arraycopy(buffer, 0, nbuf, 0, pos);
                if (!bufUpdater.compareAndSet(this, buffer, nbuf)) {
                    // Can't replace buf if there was an async close.
                    // Note: This would need to be changed if fill()
                    // is ever made accessible to multiple threads.
                    // But for now, the only way CAS can fail is via close.
                    // assert buf == null;
                    throw new IOException("Stream closed");
                }
                buffer = nbuf;
            }
        }
        count = pos;
        int n = getInIfOpen().read(buffer, pos, buffer.length - pos);
        if (n > 0) {
            count = n + pos;
        }
    }

    /**
     * See
     * the general contract of the <code>read</code>
     * method of <code>InputStream</code>.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     * stream is reached.
     * @throws IOException if this input stream has been closed by
     *                     invoking its {@link #close()} method,
     *                     or an I/O error occurs.
     * @see java.io.FilterInputStream#in
     */
    @Override
    public synchronized int read() throws IOException {
        if (pos >= count) {
            fill();
            if (pos >= count) {
                return -1;
            }
        }
        return getBufIfOpen()[pos++] & 0xff;
    }

    /**
     * Read characters into a portion of an array, reading from the underlying
     * stream at most once if necessary.
     */
    private int read1(byte[] b, int off, int len) throws IOException {
        int avail = count - pos;
        if (avail <= 0) {
            /* If the requested length is at least as large as the buffer, and
               if there is no mark/reset activity, do not bother to copy the
               bytes into the local buffer.  In this way buffered streams will
               cascade harmlessly. */
            if (len >= getBufIfOpen().length && markpos < 0) {
                return getInIfOpen().read(b, off, len);
            }
            fill();
            avail = count - pos;
            if (avail <= 0) {
                return -1;
            }
        }
        int cnt = (avail < len) ? avail : len;
        System.arraycopy(getBufIfOpen(), pos, b, off, cnt);
        pos += cnt;
        return cnt;
    }

    /**
     * Reads bytes from this byte-input stream into the specified byte array,
     * starting at the given offset.
     *
     * <p> This method implements the general contract of the corresponding
     * <code>{@link InputStream#read(byte[], int, int) read}</code> method of
     * the <code>{@link InputStream}</code> class.  As an additional
     * convenience, it attempts to read as many bytes as possible by repeatedly
     * invoking the <code>read</code> method of the underlying stream.  This
     * iterated <code>read</code> continues until one of the following
     * conditions becomes true: <ul>
     *
     * <li> The specified number of bytes have been read,
     *
     * <li> The <code>read</code> method of the underlying stream returns
     * <code>-1</code>, indicating end-of-file, or
     *
     * <li> The <code>available</code> method of the underlying stream
     * returns zero, indicating that further input requests would block.
     *
     * </ul> If the first <code>read</code> on the underlying stream returns
     * <code>-1</code> to indicate end-of-file then this method returns
     * <code>-1</code>.  Otherwise this method returns the number of bytes
     * actually read.
     *
     * <p> Subclasses of this class are encouraged, but not required, to
     * attempt to read as many bytes as possible in the same fashion.
     *
     * @param b   destination buffer.
     * @param off offset at which to start storing bytes.
     * @param len maximum number of bytes to read.
     * @return the number of bytes read, or <code>-1</code> if the end of
     * the stream has been reached.
     * @throws IOException if this input stream has been closed by
     *                     invoking its {@link #close()} method,
     *                     or an I/O error occurs.
     */
    @Override
    public synchronized int read(byte b[], int off, int len)
            throws IOException {
        getBufIfOpen(); // Check for closed stream
        if ((off | len | (off + len) | (b.length - (off + len))) < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int n = 0;
        for (; ; ) {
            int nread = read1(b, off + n, len - n);
            if (nread <= 0) {
                return (n == 0) ? nread : n;
            }
            n += nread;
            if (n >= len) {
                return n;
            }
            // if not closed but no bytes available, return
            InputStream input = in;
            if (input != null && input.available() <= 0) {
                return n;
            }
        }
    }

    /**
     * See the general contract of the <code>skip</code>
     * method of <code>InputStream</code>.
     *
     * @throws IOException if the stream does not support seek,
     *                     or if this input stream has been closed by
     *                     invoking its {@link #close()} method, or an
     *                     I/O error occurs.
     */
    @Override
    public synchronized long skip(long n) throws IOException {
        getBufIfOpen(); // Check for closed stream
        if (n <= 0) {
            return 0;
        }
        long avail = count - pos;

        if (avail <= 0) {
            // If no mark position set then don't keep in buffer
            if (markpos < 0) {
                return getInIfOpen().skip(n);
            }

            // Fill in buffer to save bytes for reset
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
     * Returns an estimate of the number of bytes that can be read (or
     * skipped over) from this input stream without blocking by the next
     * invocation of a method for this input stream. The next invocation might be
     * the same thread or another thread.  A single read or skip of this
     * many bytes will not block, but may read or skip fewer bytes.
     * <p>
     * This method returns the sum of the number of bytes remaining to be read in
     * the buffer (<code>count&nbsp;- pos</code>) and the result of calling the
     * {@link java.io.FilterInputStream#in in}.available().
     *
     * @return an estimate of the number of bytes that can be read (or skipped
     * over) from this input stream without blocking.
     * @throws IOException if this input stream has been closed by
     *                     invoking its {@link #close()} method,
     *                     or an I/O error occurs.
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
