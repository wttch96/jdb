package java.io;

/**
 * 这个抽象类是表示字节输入流的所有类的超类.
 *
 * <p> 应用定义 <code>InputStream</code> 的子类一定要提供一个方法来返回下一个输入字节.
 *
 * @author Arthur van Hoff
 * @see java.io.BufferedInputStream
 * @see java.io.ByteArrayInputStream
 * @see java.io.DataInputStream
 * @see java.io.FilterInputStream
 * @see java.io.InputStream#read()
 * @see java.io.OutputStream
 * @see java.io.PushbackInputStream
 * @since JDK1.0
 */
public abstract class InputStream implements Closeable {

    /**
     * 最大的可跳过长度,调用 {@code skip} 方法时,取传参和 MAX_SKIP_BUFFER_SIZE 中的最小值
     */
    private static final int MAX_SKIP_BUFFER_SIZE = 2048;

    /**
     * 从输入流中读取下一个字节. 字节返回一个<code>0</code>到<code>255</code>的
     * <code>int</code>类型的值. 如果因为到达了流的结尾而导致了没有字节可读,
     * 应该返回<code>-1</code>. 此方法阻塞,直到输入数据可用、检测到流的结尾或者抛出异常.
     *
     * <p> 子类必须提供这个类的实现.
     *
     * @return 下一个字节, 或者当流到达结尾时返回 <code>-1</code>
     * @throws IOException 发生了一个 I/O 错误
     */
    public abstract int read() throws IOException;

    /**
     * 从输入流中读取一些字节并将它们保存到缓冲数组 <code>b</code>中. 实际
     * 读取到的字节数将作为一个整数返回.  此方法阻塞,直到输入数据可用、检测
     * 到流的结尾或者抛出异常.
     *
     * <p> 如果 <code>b</code> 的长度为零, 然后不读取任何字节并且返回 <code>0</code>;
     * 否则,将尝试读取至少一个字节. 如果因为到达了流的结尾而导致了没有字节可读,
     * 则返回<code>-1</code>; 否则至少一个字节被读取并存储在 <code>b</code> 中.
     *
     * <p> 第一个读取的字节将被存储在 <code>b[0]</code> 元素中, 下一个存进
     * <code>b[1]</code>, 以此类推. 读取的字节数最多等于数组 <code>b</code>
     * 的长度. 令 <i>k</i> 为实际读取的字节数; 这些字节将被存储在
     * <code>b[0]</code> 到 <code>b[</code><i>k</i><code>-1]</code> 的元素中,
     * 剩余的在 <code>b[</code><i>k</i><code>]</code> 和 <code>b[b.length-1]</code>
     * 之间的元素将不受影响 .
     *
     * <p>  <code>InputStream</code> 的 <code>read(b)</code> 类成员方法和
     * <pre><code> read(b, 0, b.length) </code></pre> 拥有一样的效果.
     * (源代码就是调用的 <code> read(b, 0, b.length) </code>)
     *
     * @param b 读取到的数据存储的缓冲数组
     * @return 读取到的字节长多, 或者<code>-1</code>(如果因为流到达了末端而没有更多的数据)
     * @throws IOException          如果由于文件到达末尾之外的原因无法读取第一个字节:
     *                              输入流已关闭或者发生其他错误. wttch:注意,这个异常只会发生
     *                              在读取第一个字节时,之后的读取时发生的异常都会被捕获并丢弃.
     * @throws NullPointerException <code>b</code> 为 <code>null</code>.
     * @see java.io.InputStream#read(byte[], int, int)
     */
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * 从输入流中读取 <code> len </code>个字节数据保存到一个字节数组中去.
     * 尝试读取 <code> len </code>个字节的数量, 但是可以读取较小的数字.
     * 实际读取的字节数以整数形式返回.
     * wttch: 即可读取的字节数并没有 <code> len </code> 那么多
     *
     * <p> 此方法会阻塞除非以下情况发生: 输入的数据可用、检测到文件结尾或者抛出异常.
     *
     * <p> 如果 <code>len</code> 为 0, 则没有字节数据会被读取并返回, 同时返回 <code>0</code>;
     * 除此以外的情况下, 将尝试读取至少一个字节. 如果因为到达了流的结尾而导致了没有字节可读,
     * 则返回<code>-1</code>; 否则至少一个字节被读取并存储在 <code>b</code> 中.
     *
     * <p> 第一个读取的字节将被保存在 <code>b[off]</code> 中, 第二个保存在 <code>b[off+1]</code>中,
     * 以此类推. 最多读取的字节长度等于 <code>len</code>.
     * 令 <i>k</i> 为实际读取的字节数; 这些字节将被存储在
     * <code>b[off]</code> 到 <code>b[</code><i>off + k</i><code>-1]</code> 的元素中,
     * 剩余的在 <code>b[</code><i>off + k</i><code>]</code> 和 <code>b[b.length-1]</code>
     * 之间的元素将不受影响 .
     *
     * <p> 在任何情况下 <code>b[0]</code> 到 <code>b[off]</code> 和
     * <code>b[off+len]</code> 到 <code>b[b.length-1]</code> 之间的元素不受影响.
     *
     * <p> <code>InputStream</code> 的 <code>read(b,</code> <code>off,</code> <code>len)</code>
     * 方法, 只是反复地调用 <code>read()</code> 方法. 如果第一次调用 <code>read()</code> 时发生
     * <code>IOException</code>, 这个异常会抛出给  <code>read(b,</code> <code>off,</code> <code>len)</code>
     * 方法的调用者.  如果随后的任何 <code>read()</code> 调用的结果抛出了
     * <code>IOException</code>, 这个异常将被捕获并视为文件结束; 读取到该点的字节存储到
     * <code>b</code> 并且返回异常发生之前读取到的字节数. 此方法的默认实现将阻塞,
     * 直到读取了所请求的输入数据<code> len </code>,检测到文件结尾或引发异常.
     * 鼓励子类提供更有效的此方法实现。
     *
     * @param b   字节数据读取到之后缓存的位置.
     * @param off 数据写入的数组<code> b </code>中的起始偏移量.
     * @param len 要读取的最大字节数.
     * @return 读入缓冲区的总字节数, 如果由于已到达流末尾而没有更多数据, 则为<code> -1 </ code>.
     * @throws IOException               如果由于文件结尾以外的任何原因无法读取第一个字节,
     *                                   或者输入流已关闭,或者发生其他一些 I/O 错误.
     * @throws NullPointerException      If <code>b</code> is <code>null</code>.
     * @throws IndexOutOfBoundsException If <code>off</code> is negative,
     *                                   <code>len</code> is negative, or <code>len</code> is greater than
     *                                   <code>b.length - off</code>
     * @see java.io.InputStream#read()
     */
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            // 空指针异常
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            // 相关的 off, len 在操作 b 时必定发生数组越界
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            // 什么都不读取
            return 0;
        }

        // 读取第一个字节
        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        // 读取剩余的 len - 1 个字节
        // 读取时发生任何 IO 异常都会被捕获, 并返回已经读取到的字节长度
        int i = 1;
        try {
            for (; i < len; i++) {
                c = read();
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte) c;
            }
        } catch (IOException ignored) {
        }
        return i;
    }

    /**
     * 跳过并丢弃此输入流中的<code> n </code>字节数据. 由于各种原因,
     * <code> skip </code>方法最终可能会跳过一些较小的字节数,可能是
     * <code> 0 </code>. 这可能是由许多条件造成的;在跳过<code> n </code>
     * 字节之前到达文件末尾只有一种可能性. 返回跳过的实际字节数.
     * 如果{@code n}为负数,则类{@code InputStream}的{@code skip}方法始终返回0,
     * 并且不会跳过任何字节. 子类可以不同地处理负值.
     * <p>
     * * <p>此类的<code> skip </code>方法创建一个字节数组, 然后重复读入,
     * 直到读取<code> n </code>字节或到达流的末尾. 鼓励子类提供更有效的
     * 方法实现. 例如, 实现可能取决于搜索的能力.
     *
     * @param n 要跳过的字节数
     * @return 实际上跳过的字节数
     * @throws IOException 输入流不可移动,或是在操作时发生 I/O 错误.
     */
    public long skip(long n) throws IOException {
        // 剩下的字节数
        long remaining = n;
        // 已经读取的字节数
        int nr;
        // 0 及以下的数字不处理
        if (n <= 0) {
            return 0;
        }
        // 不能超过最大的可跳过大小
        int size = (int) Math.min(MAX_SKIP_BUFFER_SIZE, remaining);
        byte[] skipBuffer = new byte[size];
        // 一直尝试跳过字节,知道剩下的要跳过的字节数为0
        while (remaining > 0) {
            nr = read(skipBuffer, 0, (int) Math.min(size, remaining));
            if (nr < 0) {
                break;
            }
            remaining -= nr;
        }

        return n - remaining;
    }

    /**
     * 返回可以从此输入流中读取（或跳过）的字节数的估计值, 而不会被下一次调用
     * 此输入流的方法阻塞. 下一次调用可能是同一个线程或另一个线程. 单个读取或
     * 跳过这么多字节不会阻塞, 但可以读取或跳过更少的字节.
     *
     * <p>请注意, 虽然{@code InputStream}的某些实现将返回流中的总字节数, 但许
     * 多实现不会. 使用此方法的返回值来分配用于保存此流中所有数据的缓冲区是完
     * 全正确的.
     *
     * <p> 子类的实现可以选择抛出 {@link IOException} 如果输入流通过调用
     * {@link #close()}方法而被关闭.
     *
     * <p> {@code InputStream} 的 {@code available} 方法永远返回 {@code 0}.
     *
     * <p> 子类应该重写该方法.
     *
     * @return 估计可以从此输入流中无阻塞地读取（或跳过）的字节数, 或者当到
     * 达输入流末尾时{@code 0}.
     * @throws IOException 发生 I/O 错误.
     */
    public int available() throws IOException {
        return 0;
    }

    /**
     * 关闭此输入流并释放与该流关联的所有系统资源.
     *
     * <p> <code>InputStream</code> 的 <code>close</code> 方法不做任何事情.
     *
     * @throws IOException 如果发生了 I/O 错误
     */
    @Override
    public void close() throws IOException {
    }

    /**
     * 标记此输入流中的当前位置.随后对<code> reset </code>方法的调用会在最后标记
     * 的位置重新定位此流, 以便后续读取重新读取相同的字节.
     *
     * <p> <code>readlimit</code> 参数告诉了此输入流允许在标记位置失效之前读取许多字节.
     *
     * <p> <code> mark </code>的一般契约是, 如果方法<code> markSupported </code>返回
     * <code> true </code>, 则流以某种方式记住在调用<code>mark</code>之后读取的
     * 所有字节并准备好在调用方法<code> reset </code>时再次提供相同的字节.
     * 但是, 如果在调用<code> reset </code>之前从流中读取超过
     * <code> readlimit </code>字节, 则根本不需要记录任何数据流.
     *
     * <p> Marking a closed stream should not have any effect on the stream.
     *
     * <p> <code>InputStream</code> 的 <code>mark</code>方法不对流产生任何影响.
     *
     * @param readlimit 标记位置变为无效之前可读取的最大字节数限制.
     * @see java.io.InputStream#reset()
     */
    public synchronized void mark(int readlimit) {
    }

    /**
     * 将此流重新定位到上次在此输入流上调用<code> mark </code>方法时的位置.
     *
     * <p> <code> reset </code>的一般合同是:
     *
     * <ul>
     * <li> 如果 <code>markSupported</code> 方法返回 <code>true</code>, 则:
     *
     * <ul><li> 如果自创建流以来尚未调用<code> mark </code>方法, 或者自上次
     * 调用<code> mark </code>以来从流中读取的字节数大于最后一次调用
     * <code>mark</code>时的参数, 然后可能抛出<code> IOException </code>.
     *
     * <li> 如果没有抛出这样的<code> IOException </code>, 那么流将被重置为一种状态,
     * 这样自从最近一次调用<code>mark</code>以来所读取的所有字节(或者自从文件,
     * 如果<code>mark</code>尚未被调用) 将被重新提供给<code> read </code>方法
     * 的后续调用者, 后跟任何从调用 <code>reset</code> 时起将作为下一输入数据的字节.</ul>
     *
     * <li> 如果<code>markSupported</code> 方法返回 <code>false</code>, 则:
     *
     * <ul><li> 调用 <code>reset</code> 会抛出 <code>IOException</code>.
     *
     * <li> 如果未抛出<code> IOException </code>, 则将流重置为固定状态,
     * 该状态取决于输入流的特定类型及其创建方式. 将提供给<code> read </code>
     * 方法的后续调用者的字节取决于输入流的特定类型.  </ul></ul>
     *
     * <p> 类<code> InputStream </code>的<code> reset </code>方法
     * 除了抛出<code> IOException </code>外什么都不做.
     *
     * @throws IOException 如果此流尚未标记或标记已失效.
     * @see java.io.InputStream#mark(int)
     * @see java.io.IOException
     */
    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    /**
     * 测试此输入流是否支持<code>标记</code>和<code> reset </code>方法.
     * 是否支持<code> mark </code>和<code> reset </code>是特定输入流实
     * 例的不变属性. <code> InputStream </code>的<code> markSupported
     * </code>方法返回<code> false </code>.
     *
     * @return <code>true</code> 如果该输入流支持 reset 和 reset 方法; 否则 <code>false</code> .
     * @see java.io.InputStream#mark(int)
     * @see java.io.InputStream#reset()
     */
    public boolean markSupported() {
        return false;
    }

}
