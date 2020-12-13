package java.util.stream;

/**
 * 一个描述流抽象的已知形状专业的枚举. 每个将对应于{@link BaseStream}的特定子接口
 * (例如, {@code REFERENCE}对应于{@code Stream}, {@code INT_VALUE}对应于{@code IntStream}).
 * 每个也可能对应于值处理抽象的专业化, 例如{@code Spliterator}, {@code Consumer}等.
 *
 * @apiNote 实现使用该枚举来确定流和操作之间的兼容性(即, 流的输出形状是否与下一个操作的输入形状兼容).
 *
 * <p>某些API要求您为输入或输出元素同时指定通用类型和流形状, 例如{@link TerminalOp},
 * 其输入类型既具有通用类型参数, 又具有输入形状的吸气剂. 当以这种方式表示原始流时,
 * 泛型类型参数应对应于该原始类型的包装器类型.
 * @since 1.8
 */
enum StreamShape {
    /**
     * 与{@code Stream}和作为对象引用的元素相对应的形状特化.
     */
    REFERENCE,
    /**
     * 对应于{@code IntStream}和{@code int}值元素的形状特化.
     */
    INT_VALUE,
    /**
     * 对应于{@code LongStream}和{@code long}值元素的形状特化.
     */
    LONG_VALUE,
    /**
     * 对应于{@code DoubleStream}和{@code double}值元素的形状特化.
     */
    DOUBLE_VALUE
}
