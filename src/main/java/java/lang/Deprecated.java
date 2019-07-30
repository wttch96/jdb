package java.lang;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 注解 &#64;Deprecated 的程序元素是程序员不鼓励使用的程序元素, 通常是因为它很危险,
 * 或者因为存在更好的替代方法. 当在不推荐使用的代码中使用或覆盖已弃用的程序元素时,
 * 编译器会发出警告.
 *
 * @author Neal Gafter
 * @jls 9.6.3.6 @Deprecated
 * @since 1.5
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
public @interface Deprecated {
}
