package java.util.stream;

import java.util.Iterator;
import java.util.Spliterator;

/**
 * 流的基本接口, 流是支持串行和并行聚合操作的元素序列.
 * 以下示例说明了使用流类型{@link Stream} 和{@link IntStream}的聚合操作, 计算了红色小部件的权重之和:
 *
 * <pre>{@code
 *     int sum = widgets.stream()
 *                      .filter(w -> w.getColor() == RED)
 *                      .mapToInt(w -> w.getWeight())
 *                      .sum();
 * }</pre>
 * <p>
 * 请参阅{@link Stream}的类文档以及有关<a href="package-summary.html"> java.util.stream </a>的软件包文档有关流, 流操作, 流管道,
 * 并行性, 它控制所有流类型的行为.
 *
 * @param <T> 流元素的类型
 * @param <S> 实现{@code BaseStream}的流的类型
 * @see Stream
 * @see IntStream
 * @see LongStream
 * @see DoubleStream
 * @see <a href="package-summary.html">java.util.stream</a>
 * @since 1.8
 */
public interface BaseStream<T, S extends BaseStream<T, S>>
        extends AutoCloseable {
    /**
     * 返回此流的元素的迭代器.
     *
     * <p>这是<a href="package-summary.html#StreamOps">终端操作</a>.
     *
     * @return 此流的元素迭代器
     */
    Iterator<T> iterator();

    /**
     * 返回此流的元素的分隔器.
     *
     * <p>这是<a href="package-summary.html#StreamOps">终端操作</a>.
     *
     * @return 此流的元素分隔器
     */
    Spliterator<T> spliterator();

    /**
     * 返回此流(如果要执行终端操作)是否将并行执行.调用终端流操作方法后调用此方法可能会产生不可预测的结果.
     *
     * @return {@code true}如果执行此流是否将并行执行
     */
    boolean isParallel();

    /**
     * 返回串行的等效流.  可能会返回自身, 这是因为流已经是串行的, 或者是因为基础流状态已被修改为串行的.
     *
     * <p>这是<a href="package-summary.html#StreamOps">中间操作</a>.
     *
     * @return a sequential stream
     */
    S sequential();

    /**
     * 返回并行的等效流. 可能由于流已经是并行的, 或者因为基础流的状态被修改为并行而返回自身.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @return a parallel stream
     */
    S parallel();

    /**
     * 返回 <a href="package-summary.html#Ordering">无序</a>的等效流.
     * 可能返回本身, 可能是因为流已经无序, 或者是因为基础流状态已被修改为无序.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @return an unordered stream
     */
    S unordered();

    /**
     * 返回带有附加关闭处理程序的等效流. 当在流上调用{@link #close（）}方法时, 将运行关闭处理程序, 并按添加它们的串行执行.
     * 所有关闭处理程序都会运行，即使较早的关闭处理程序会引发异常.
     * 如果任何关闭处理程序抛出异常, 则第一个抛出的异常将被中继到{@code close()}的调用者, 其余的任何异常作为抑制的异常添加
     * 到该异常(除非其余的异常是与第一个异常相同的异常, 因为一个异常无法抑制自身.)可能返回自身.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param closeHandler 关闭流时要执行的任务
     * @return 一个带有处理程序的流，如果关闭该流，该处理程序将运行
     */
    S onClose(Runnable closeHandler);

    /**
     * 关闭此流, 导致调用此流管道的所有关闭处理程序.
     *
     * @see AutoCloseable#close()
     */
    @Override
    void close();
}
