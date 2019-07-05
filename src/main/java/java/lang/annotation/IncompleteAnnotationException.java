package java.lang.annotation;

/**
 * 抛出以指示程序在尝试访问一个编译(或序列化)之后添加到注解类型声明
 * 的注解类型元素. {@linkplain java.lang.reflect.AnnotatedElement
 * API 通过反射用来读取注解}会抛出此异常.
 *
 * @author Josh Bloch
 * @see java.lang.reflect.AnnotatedElement
 * @since 1.5
 */
public class IncompleteAnnotationException extends RuntimeException {
    private static final long serialVersionUID = 8445097402741811912L;

    private Class<? extends Annotation> annotationType;
    private String elementName;

    /**
     * 构造一个 IncompleteAnnotationException 以指示指定的注释类型中缺少指定的元素.
     *
     * @param annotationType 注释类型的Class对象
     * @param elementName    缺失元素的名字
     * @throws NullPointerException 任一参数为 {@code null}
     */
    public IncompleteAnnotationException(
            Class<? extends Annotation> annotationType,
            String elementName) {
        super(annotationType.getName() + " missing element " +
                elementName);

        this.annotationType = annotationType;
        this.elementName = elementName;
    }

    /**
     * 返回具有缺少元素的注释类型的Class对象.
     *
     * @return 具有缺少元素的注释类型的Class对象
     */
    public Class<? extends Annotation> annotationType() {
        return annotationType;
    }

    /**
     * 返回缺失元素的名字.
     *
     * @return 缺失元素的名字
     */
    public String elementName() {
        return elementName;
    }
}
