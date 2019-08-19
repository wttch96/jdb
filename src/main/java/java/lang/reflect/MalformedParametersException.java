package java.lang.reflect;

/**
 * 当 {@link java.lang.reflect.Executable#getParameters java.lang.reflect 包} 尝试
 * 从类文件中读取方法参数并确定一个或多个参数格式错误时抛出.
 *
 * <p>以下是抛出此异常的条件列表:
 * <ul>
 * <li> 该方法的参数数量(参数计数)是错误的.
 * <li> 常量池索引超出范围.
 * <li> 常量池索引不引用 UTF-8 条目.
 * <li> 参数名称为 "", 或者包含非法字符.
 * <li> flags 字段包含非法标志(FINAL, SYNTHETIC, 或者 MANDATED 以外的标志)
 * </ul>
 * <p>
 * See {@link java.lang.reflect.Executable#getParameters} for more
 * information.
 *
 * @see java.lang.reflect.Executable#getParameters
 * @since 1.8
 */
public class MalformedParametersException extends RuntimeException {

    /**
     * 序列化版本.
     */
    private static final long serialVersionUID = 20130919L;

    /**
     * 用空原因创建一个 {@code MalformedParametersException} .
     */
    public MalformedParametersException() {
    }

    /**
     * 创建一个 {@code MalformedParametersException}.
     *
     * @param reason 异常的原因.
     */
    public MalformedParametersException(String reason) {
        super(reason);
    }
}
