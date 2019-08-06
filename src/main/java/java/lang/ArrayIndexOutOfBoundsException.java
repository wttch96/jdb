package java.lang;

/**
 * 抛出以指示已使用非法索引访问数组. 索引为负数或大于等于数组的大小.
 *
 * @author unascribed
 * @since JDK1.0
 */
public
class ArrayIndexOutOfBoundsException extends IndexOutOfBoundsException {
    private static final long serialVersionUID = -5116101128118950844L;

    /**
     * 不使用详细信息构造 <code>ArrayIndexOutOfBoundsException</code> .
     */
    public ArrayIndexOutOfBoundsException() {
        super();
    }

    /**
     * 用一个表明非法索引的参数构造 <code>ArrayIndexOutOfBoundsException</code>.
     *
     * @param index the illegal index.
     */
    public ArrayIndexOutOfBoundsException(int index) {
        super("Array index out of range: " + index);
    }

    /**
     * 使用指定的详细消息构造 <code>ArrayIndexOutOfBoundsException</code> .
     *
     * @param s 详细消息.
     */
    public ArrayIndexOutOfBoundsException(String s) {
        super(s);
    }
}
