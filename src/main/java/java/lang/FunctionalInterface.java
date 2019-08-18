package java.lang;

import java.lang.annotation.*;

/**
 * 一个信息性的注解类型, 用于指示接口类型声明旨在成为 java 语言规范定义的 <i>功能接口</i>.
 * <p>
 * 从概念上讲, 功能接口只是一种抽象方法.
 * 因为 {@linkplain java.lang.reflect.Method#isDefault() 默认方法} 有一个实现, 所以它们
 * 不是抽象的. 如果一个接口判断一个抽象方法覆盖了 {@code java.lang.Object} 的一个公共
 * 方法, 那么也不会计算接口的抽象方法的数量, 因为接口的任何实现都将具有来自 {@code
 * java.lang.Object} 或其他地方的实现.
 *
 * <p>请注意, 可以使用 lambda 表达式, 方法引用或构造函数引用创建功能接口的实例.
 *
 * <p>如果使用此注解类型对类型进行标注, 则编译器会生成错误消息, 除非:
 *
 * <ul>
 * <li> 类型是接口类型, 而不是注解类型, 枚举或类.
 * <li> 带注解的类型满足功能接口的要求.
 * </ul>
 *
 * <p>但是, 无论接口声明中是否存在 {@code FunctionInterface} 注解, 编译器都会将满足功能定义的任何接口视为功能接口.
 *
 * @jls 4.3.2. The Class Object
 * @jls 9.8 Functional Interfaces
 * @jls 9.4.3 Interface Method Body
 * @since 1.8
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FunctionalInterface {
}
