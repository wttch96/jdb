package java.lang.annotation;

import java.lang.reflect.Method;

/**
 * 若某个注解的类型在对该注解进行编译(或序列化)后发生了更改,
 * 而程序试图访问该注解的元素时, 抛出此异常.
 * {@linkplain java.lang.reflect.AnnotatedElement API 通过反射用来读取注解}
 * 会抛出次异常.
 *
 * @author Josh Bloch
 * @see java.lang.reflect.AnnotatedElement
 * @since 1.5
 */
public class AnnotationTypeMismatchException extends RuntimeException {
    private static final long serialVersionUID = 8125925355765570191L;

    /**
     * 注解元素的 <tt>Method</tt> 对象.
     */
    private final Method element;

    /**
     * 注解中找到的(错误)数据类型. 此字符串可以(但不是必须)包含该值.
     * 字符串的确切格式未指定.
     */
    private final String foundType;

    /**
     * 使用指定的注解类型元素和发现的数据类型构造一个 AnnotationTypeMismatchException 异常.
     *
     * @param element   注解元素的 <tt>Method</tt> 对象
     * @param foundType 注解中找到的(错误)数据类型. 此字符串可以(但不是必须)包含该值.
     *                  字符串的确切格式未指定.
     */
    public AnnotationTypeMismatchException(Method element, String foundType) {
        super("Incorrectly typed data found for annotation element " + element
                + " (Found data of type " + foundType + ")");
        this.element = element;
        this.foundType = foundType;
    }

    /**
     * 返回错误类型元素的 <tt>Method</tt> 对象.
     *
     * @return 错误类型元素的 <tt>Method</tt> 对象
     */
    public Method element() {
        return this.element;
    }

    /**
     * 返回在错误类型的元素中找到的数据类型.
     * 返回的字符串可以包含该值,但不是必须. 并且字符串的确切格式未指定.
     *
     * @return 在错误类型的元素中找到的数据类型
     */
    public String foundType() {
        return this.foundType;
    }
}
