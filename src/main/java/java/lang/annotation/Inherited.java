package java.lang.annotation;

/**
 * 标示自动继承注解类型. 如果注解类型声明中存在 Inherited 元注解,
 * 并且用户在类声明上查询注解类型, 并且类声明没有此类型的注解,
 * 则将自动查询类的超类以获取注解类型. 将重复此过程, 直到找到此
 * 类型的注解, 或者到达类层次结构(对象)的顶部. 如果没有超类具有
 * 此类型的注解, 则查询将标示相关类没有此类注解.
 *
 * <p>请注意, 如果使用带注解的类型来注释除类以外的任何内容, 则
 * 元注解类型不起作用. 另请注意, 此元注解仅导致注解从超类继承;
 * 已实现接口上的注解无效.
 *
 * @author Joshua Bloch
 * @jls 9.6.3.3 @Inherited
 * @since 1.5
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Inherited {
}
