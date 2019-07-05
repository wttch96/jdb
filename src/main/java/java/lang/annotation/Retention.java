package java.lang.annotation;

/**
 * 指示要保留带注解类型的注解的时间长度. 如果注解类型声明中
 * 不存在 Retention 元注解, 这保留策略默认为 {@code RetentionPolicy.CLASS}.
 *
 * <p>仅当元注解类型用于注解时, Retention 元注解才有效.
 * 如果元注解类型在另一个注解类型中用作成员类型, 则无效.
 *
 * @author Joshua Bloch
 * @jls 9.6.3.2 @Retention
 * @since 1.5
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Retention {
    /**
     * 返回保留策略.
     *
     * @return 保留策略
     */
    RetentionPolicy value();
}
