package java.lang;

import java.lang.annotation.*;

/**
 * 指示方法声明旨在覆盖超类型中的方法声明. 如果使用此批注类型对方法进行批注,
 * 则编译器需要生成错误消息, 除非至少满足下列条件之一:
 *
 * <ul><li>
 * 该方法会覆盖或实现在超类型中声明的方法.
 * </li><li>
 * 该方法的签名覆盖等同于{@linkplain Object}中声明的任何公共方法的签名.
 * </li></ul>
 *
 * @author Peter von der Ah&eacute;
 * @author Joshua Bloch
 * @jls 9.6.1.4 @Override
 * @since 1.5
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Override {
}
