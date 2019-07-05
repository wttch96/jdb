package java.lang.annotation;

/**
 * 指示默认情况下, javadoc 和类似工具将记录带有注解的类型.
 * 此类型用于注解类型的声明, 其注解将影响客户端对带有注解元素的使用.
 * 如果使用 Documented 注解类型声明, 则其注解将成为注解元素的公共
 * api 的一部分.
 * 默认情况下, 注解信息不会包含在 Javadoc 中.
 * <p>
 * 这是一个元注解.
 * <p>
 * 将此注解用于注解 A 的声明, 则使用 A 注解标注的地方, 在 javadoc 中
 * 将看到标注的注解内容.
 *
 * @author Joshua Bloch
 * @since 1.5
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Documented {
}
