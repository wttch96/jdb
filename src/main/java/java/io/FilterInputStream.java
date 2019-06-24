package java.io;

/**
 * 一个 <code>FilterInputStream</code>包含了一个其他的输入流, 它使用这些基本的流作为基本的数据来源, 可能在传输过程中转换
 * 数据, 或是添加一些额外的功能. <code>FilterInputStream</code>类简单的重写了 <code>InputStream</code> 的所有方法, 其版
 * 本讲所有的请求传递给包含的输入流. <code>FilterInputStream</code> 的子类可以更多的重写它的方法, 以提供更多的额外功能或
 * 字段.
 */
public
class FilterInputStream extends InputStream {
    /**
     * 要被过滤的输入流.
     * 原子化操作: 使所有对 in 的赋值立即写入内存生效.
     */
    protected volatile InputStream in;

    /**
     * 使用指定的参数 <code>in</code> 创建一个 <code>FilterInputStream</code> 对象, 并把它存储在 <code>this.in</code>字
     * 中记住它以便稍后使用它.
     *
     * @param in 基础的输入流, 或者 <code>null</code> 当这个实例没有作为基础的输入流时.
     */
    protected FilterInputStream(InputStream in) {
        this.in = in;
    }

    /**
     * 从输入流中读取下一个字节. 字节返回一个 <code>0</code> 到 <code>255</code> 的 <code>int</code> 类型的值. 如果因为
     * 到达了流的结尾而导致了没有字节可读, 应该返回<code>-1</code>. 此方法阻塞,直到输入数据可用、检测到流的结尾或者抛出
     * 异常.
     * <p>
     * 这个方法简单的执行 <code>in.read()</code> 并且返回它的结果.
     *
     * @return 下一个字节, 或者当流到达结尾时返回 <code>-1</code>
     * @throws IOException 发生 I/O 错误.
     * @see java.io.FilterInputStream#in
     */
    @Override
    public int read() throws IOException {
        return in.read();
    }

    /**
     * 从输入流中读取一些字节并将它们保存到缓冲数组 <code>b</code> 中. 此方法阻塞知道输入流中有数据可读.
     * <p>
     * 这个方法只是简单的执行调用 <code>read(b, 0, b.length)</code> 并返回它的结果. 重要的一点: 它 <i>不</i> 执行 <code>
     * in.read(b)</code> 来代替; 实际上 <code>FilterInputStream</code> 的子类主要取决于它子类所使用的实现策略.
     *
     * @param b 读取到的数据存储的缓冲数组.
     * @return 读取到的字节长度, 或者<code>-1</code>(如果因为流到达了末端而没有更多的数据)
     * @throws IOException 发生 I/O 错误.
     * @see java.io.FilterInputStream#read(byte[], int, int)
     */
    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * 从输入流中读取 <code> len </code>个字节数据保存到一个字节数组中去. 尝试读取 <code> len </code>个字节的数量,
     * 但是可以读取较小的数字. 实际读取的字节数以整数形式返回.
     * wttch: 即可读取的字节数并没有 <code> len </code> 那么多.
     * <p>
     * 这个方法只是简单的调用 <code>in.read(b, off, len)</code> 并返回它的结果.
     *
     * @param b   字节数据读取到之后缓存的位置.
     * @param off 数据写入的数组<code> b </code>中的起始偏移量.
     * @param len 要读取的最大字节数.
     * @return 读入缓冲区的总字节数, 如果由于已到达流末尾而没有更多数据, 则为<code> -1 </ code>.
     * @throws IOException               如果由于文件结尾以外的任何原因无法读取第一个字节,
     *                                   或者输入流已关闭,或者发生其他一些 I/O 错误.
     * @throws NullPointerException      如果 <code>b</code> 是 <code>null</code>.
     * @throws IndexOutOfBoundsException 如果 <code>off</code> 是负值, <code>len</code> 是负值, 或者 <code>len</code>
     *                                   大于 <code>b.length - off</code> .
     * @see java.io.FilterInputStream#in
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    /**
     * 跳过并丢弃此输入流中的<code> n </code>字节数据. 由于各种原因, <code> skip </code>方法最终可能会跳过一些较小的字节
     * 数,可能是 <code> 0 </code>. 这可能是由许多条件造成的;在跳过<code> n </code> 字节之前到达文件末尾只有一种可能性.
     * 返回跳过的实际字节数. 如果{@code n}为负数,则类{@code InputStream}的{@code skip}方法始终返回0, 并且不会跳过任何字
     * 节. 子类可以不同地处理负值.
     * <p>
     * 这个方法只是简单的执行 <code>in.skip(n)</code>.
     *
     * @param n 要跳过的字节数
     * @return 实际上跳过的字节数
     * @throws IOException 这个流不支持 skip, 或者发生了其他的 I/O 错误.
     */
    @Override
    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    /**
     * 返回可以从此输入流中读取（或跳过）的字节数的估计值, 而不会被下一次调用此输入流的方法阻塞. 下一次调用可能是同一个
     * 线程或另一个线程. 单个读取或跳过这么多字节不会阻塞, 但可以读取或跳过更少的字节.
     * <p>
     * 这个方法只是简单的调用 {@link #in in}.available().
     *
     * @return 估计可以从此输入流中无阻塞地读取（或跳过）的字节数, 或者当到达输入流末尾时{@code 0}.
     * @throws IOException 如果发生 I/O 错误.
     */
    @Override
    public int available() throws IOException {
        return in.available();
    }

    /**
     * 关闭这个输入流并且释放任何和这个流相关的系统资源.
     * 这个方法只是简单的执行 <code>in.close()</code>.
     *
     * @throws IOException 发生 I/O 错误.
     * @see java.io.FilterInputStream#in
     */
    @Override
    public void close() throws IOException {
        in.close();
    }

    /**
     * 标记输入流当前的位置, 一个后续的 <code>reset</code> 方法调用将复位这个流到标记的位置, 以便后续读取重新读取相同的
     * 字节.
     * <p>
     * <code>readlimit</code> 参数告诉了此输入流允许在标记位置失效之前读取许多字节.
     * <p>
     * 这个方法只是简单的执行 <code>in.mark(readlimit)</code>方法.
     *
     * @param readlimit the maximum limit of bytes that can be read before
     *                  the mark position becomes invalid.
     * @see java.io.FilterInputStream#in
     * @see java.io.FilterInputStream#reset()
     */
    @Override
    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
    }

    /**
     * 复位这个流到上次调用 <code>mark</code> 方法时当时这个输入流的位置.
     * <p>
     * 这个方法只是简单的执行 <code>in.reset()</code>.
     * <p>
     * 流标记用于需要提前阅读以查看流中的内容的情况. 通常, 这最容易通过调用一些通用解析器来实现. 如果流是解析所处理的类
     * 型, 那么它就会很愉快地继续工作. 如果流不是这种类型的, 则解析器应该在流失败时抛出异常. 如果这发生在
     * <code>readlimit</code>字节内, 则允许外部代码重置流并尝试另一个解析器.
     *
     * @throws IOException 如果这个流不可被标记或者标记无效了. wttch: 无效可能是读取的字节数已经超过了 readlimit 的
     *                     限制, 此时以无法通过 <code>reset</code> 方法恢复流的状态了.
     * @see java.io.FilterInputStream#in
     * @see java.io.FilterInputStream#mark(int)
     */
    @Override
    public synchronized void reset() throws IOException {
        in.reset();
    }

    /**
     * 测试输入流是否支持 <code>mark</code>和 <code>reset</code> 方法. 这个方法只是简单的执行
     * <code>in.markSupported()</code>.
     *
     * @return <code>true</code> 如果这个流支持 <code>mark</code> 和 <code>reset</code> 方法; 否则 <code>false</code>.
     * @see java.io.FilterInputStream#in
     * @see java.io.InputStream#mark(int)
     * @see java.io.InputStream#reset()
     */
    @Override
    public boolean markSupported() {
        return in.markSupported();
    }
}
