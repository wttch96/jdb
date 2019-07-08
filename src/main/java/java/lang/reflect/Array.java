package java.lang.reflect;

/**
 * {@code Array} 类提供了动态创建和访问 java 数组的静态方法.
 *
 * <p>{@code Array} 允许在 get 和 set 操作期间进行扩展转换, 但如果发生了缩小转换
 * 就会抛出 {@code IllegalArgumentException} 异常.
 *
 * @author Nakul Saraiya
 */
public final
class Array {

    /**
     * 构造器. Array 类是不可实例化的.
     */
    private Array() {
    }

    /**
     * 使用指定的组件类型和长度创建一个新的数组.
     * 执行此方法创建一个数组等同于下面的操作:
     * <blockquote>
     * <pre>
     * int[] x = {length};
     * Array.newInstance(componentType, x);
     * </pre>
     * </blockquote>
     *
     * <p>新数组的维度不得超过255.
     * <p>
     * Wttch: 只是简单的调用了本地方法 {@link #newArray(Class, int)}.
     * 只是为了变个名字么?
     *
     * @param componentType {@code Class} 表示新数组的组件类型的对象
     * @param length        新数组的长度
     * @return 此新数组
     * @throws NullPointerException       如果指定的 {@code componentType} 参数为空
     * @throws IllegalArgumentException   如果 componentType 为{@link Void#TYPE},
     *                                    或者请求的数组实例的维数超过255
     * @throws NegativeArraySizeException 如果指定的 {@code length} 是负数
     * @see #newArray(Class, int)
     */
    public static Object newInstance(Class<?> componentType, int length)
            throws NegativeArraySizeException {
        return newArray(componentType, length);
    }

    /**
     * 用指定的组件类型和维度创建一个新的数组.
     * 如果 {@code componentType} 表示的是一个非数组类 或者 接口,
     * 新数组将拥有 {@code dimensions.length} 个维度 并且 {@code componentType} 作为组件类型.
     * 如果 {@code componentType} 表示一个数组类, 新数组的维度就等于
     * {@code dimensions.length} 和 {@code componentType} 维度大小之和.
     * 在这种情况下, 组件类型是 {@code componentType} 的组件类型.
     *
     * <p>新数组的维度不得超过255.
     * <p>
     * Wttch: 只是简单的调用本地方法 {@link #multiNewArray(Class, int[])} , 并返回该方法的返回值.
     *
     * @param componentType {@code Class} 表示新数组的组件类型的对象
     * @param dimensions    一个 {@code int} 类型的数组来表示新数组的维度
     * @return 新数组
     * @throws NullPointerException       如果指定的 {@code componentType} 参数为空
     * @throws IllegalArgumentException   如果 componentType 为{@link Void#TYPE},
     *                                    或者请求的数组实例的维数超过255
     * @throws NegativeArraySizeException 如果 {@code dimensions} 数组中任意一个数值为负数
     * @see #multiNewArray(Class, int[])
     */
    public static Object newInstance(Class<?> componentType, int... dimensions)
            throws IllegalArgumentException, NegativeArraySizeException {
        return multiNewArray(componentType, dimensions);
    }

    /**
     * 返回指定数组对象的长度, {@code int} 类型.
     * <p>
     * Wttch: 本地方法
     *
     * @param array 此数组
     * @return 这个数组的长度
     * @throws IllegalArgumentException 如果参数不是一个数组
     */
    public static native int getLength(Object array)
            throws IllegalArgumentException;

    /**
     * 返回指定数组对象索引组件的值.
     * 这个值自动装箱为一个对象, 如果它是一个原始类型.
     *
     * @param array 数组对象
     * @param index 索引下标
     * @return 指定数组中索引组件的(可能装箱的)值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     */
    public static native Object get(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 返回指定数组对象索引组件的值({@code boolean} 类型).
     *
     * @param array 数组对象
     * @param index 索引下标
     * @return 指定数组中索引组件的值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为返回值的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#get
     */
    public static native boolean getBoolean(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 返回指定数组对象索引组件的值({@code byte} 类型).
     *
     * @param array 数组对象
     * @param index 索引下标
     * @return 指定数组中索引组件的值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为返回值的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#get
     */
    public static native byte getByte(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 返回指定数组对象索引组件的值({@code char} 类型).
     *
     * @param array 数组对象
     * @param index 索引下标
     * @return 指定数组中索引组件的值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为返回值的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#get
     */
    public static native char getChar(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 返回指定数组对象索引组件的值({@code short} 类型).
     *
     * @param array 数组对象
     * @param index 索引下标
     * @return 指定数组中索引组件的值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为返回值的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#get
     */
    public static native short getShort(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 返回指定数组对象索引组件的值({@code int} 类型).
     *
     * @param array 数组对象
     * @param index 索引下标
     * @return 指定数组中索引组件的值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为返回值的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#get
     */
    public static native int getInt(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 返回指定数组对象索引组件的值({@code long} 类型).
     *
     * @param array 数组对象
     * @param index 索引下标
     * @return 指定数组中索引组件的值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为返回值的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#get
     */
    public static native long getLong(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 返回指定数组对象索引组件的值({@code float} 类型).
     *
     * @param array 数组对象
     * @param index 索引下标
     * @return 指定数组中索引组件的值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为返回值的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#get
     */
    public static native float getFloat(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 返回指定数组对象索引组件的值({@code double} 类型).
     *
     * @param array 数组对象
     * @param index 索引下标
     * @return 指定数组中索引组件的值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为返回值的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#get
     */
    public static native double getDouble(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 用指定的值设置定义数组对象的索引组件的值.
     * 如果数组具有原始类型的组件, 则首先会自动拆箱.
     *
     * @param array 数组对象
     * @param index 数组中的索引下标
     * @param value 索引组件的新值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        组件类型是原始类型, 但是拆箱失败
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     */
    public static native void set(Object array, int index, Object value)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 用指定的值设置定义数组对象的索引组件的值({@code boolean} 类型).
     *
     * @param array 数组对象
     * @param index 数组中的索引下标
     * @param z     索引组件的新值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为需要的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#set
     */
    public static native void setBoolean(Object array, int index, boolean z)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 用指定的值设置定义数组对象的索引组件的值({@code byte} 类型).
     *
     * @param array 数组对象
     * @param index 数组中的索引下标
     * @param b     索引组件的新值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为需要的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#set
     */
    public static native void setByte(Object array, int index, byte b)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 用指定的值设置定义数组对象的索引组件的值({@code char} 类型).
     *
     * @param array 数组对象
     * @param index 数组中的索引下标
     * @param c     索引组件的新值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为需要的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#set
     */
    public static native void setChar(Object array, int index, char c)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 用指定的值设置定义数组对象的索引组件的值({@code short} 类型).
     *
     * @param array 数组对象
     * @param index 数组中的索引下标
     * @param s     索引组件的新值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为需要的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#set
     */
    public static native void setShort(Object array, int index, short s)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 用指定的值设置定义数组对象的索引组件的值({@code int} 类型).
     *
     * @param array 数组对象
     * @param index 数组中的索引下标
     * @param i     索引组件的新值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为需要的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#set
     */
    public static native void setInt(Object array, int index, int i)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 用指定的值设置定义数组对象的索引组件的值({@code long} 类型).
     *
     * @param array 数组对象
     * @param index 数组中的索引下标
     * @param l     索引组件的新值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为需要的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#set
     */
    public static native void setLong(Object array, int index, long l)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 用指定的值设置定义数组对象的索引组件的值({@code float} 类型).
     *
     * @param array 数组对象
     * @param index 数组中的索引下标
     * @param f     索引组件的新值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为需要的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#set
     */
    public static native void setFloat(Object array, int index, float f)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /**
     * 用指定的值设置定义数组对象的索引组件的值({@code double} 类型).
     *
     * @param array 数组对象
     * @param index 数组中的索引下标
     * @param d     索引组件的新值
     * @throws NullPointerException           指定对象为空
     * @throws IllegalArgumentException       指定对象不是一个数组对象, 或者
     *                                        无法通过恒等或者加宽转换转换为需要的类型
     * @throws ArrayIndexOutOfBoundsException 如果指定的 {@code index} 参数是负的,
     *                                        或者它大于等于指定数组的长度
     * @see Array#set
     */
    public static native void setDouble(Object array, int index, double d)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

    /*
     * Private
     */

    private static native Object newArray(Class<?> componentType, int length)
            throws NegativeArraySizeException;

    private static native Object multiNewArray(Class<?> componentType,
                                               int[] dimensions)
            throws IllegalArgumentException, NegativeArraySizeException;


}
