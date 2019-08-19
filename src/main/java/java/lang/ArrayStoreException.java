package java.lang;

/**
 * 抛出表示尝试将错误类型的对象存储到对象数组中. 例如,
 * 以下代码生成 <code>ArrayStoreException</code>
 * <blockquote><pre>
 *     Object x[] = new String[3];
 *     x[0] = new Integer(0);
 * </pre></blockquote>
 *
 * @author unascribed
 * @since JDK1.0
 */
public
class ArrayStoreException extends RuntimeException {
    private static final long serialVersionUID = -4522193890499838241L;

    /**
     * 构造一个 <code>ArrayStoreException</code> 使用详细信息.
     */
    public ArrayStoreException() {
        super();
    }

    /**
     * 构造一个 <code>ArrayStoreException</code> 使用指定的信息.
     *
     * @param s 详细的信息.
     */
    public ArrayStoreException(String s) {
        super(s);
    }
}
