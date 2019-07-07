package java.lang.reflect;

import sun.reflect.annotation.AnnotationSupport;
import sun.reflect.annotation.AnnotationType;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 表示虚拟机中当前运行的程序中的一个带注解的元素. 这个接口允许注解可以通过反射被读取.
 * 通过这个接口的方法返回的所有注解都是不可变的并且可被序列号.
 * 通过这个接口的方法返回的数组可以被调用者修改并不影响它的返回值给其他调用者.
 *
 * <p>{@link #getAnnotationsByType(Class)} 和 {@link #getDeclaredAnnotationsByType(Class)}
 * 方法支持元素上相同类型的多个注解. 如果任一方法的参数是可重复的注解类型(JLS 9.6),
 * 那么该方法将"查看"一个容器注解(JLS 9.7), 如果存在的话就返回容器中的所有注解.
 * 容器注解可以在编译是生成以包装参数类型的多个注解.
 *
 * <p>此接口中使用术语<em>directly present 直接存在</em>, <em>indirectly present 间接存在</em>,
 * <em>present 存在</em> 和 <em>associated 关联</em> 来精确描述哪些注解由方法返回:
 *
 * <ul>
 *
 * <li>
 * 如果元素 <i>E</i> 有 {@code RuntimeVisibleAnnotations } 或者
 * {@code RuntimeVisibleParameterAnnotations} 或者 {@code RuntimeVisibleTypeAnnotations}
 * 属性, 并且这个属性包含 <i>A</i>, 则元素 <i>E</i> 上的注解被认为是
 * <em>directly present 直接存在</em>的.
 *
 * <li>如果元素 <i>E</i> 有 {@code RuntimeVisibleAnnotations} 或者
 * {@code RuntimeVisibleParameterAnnotations} 或者 {@code RuntimeVisibleTypeAnnotations}
 * 属性, <i>A</i> 的类型是可重复的, 并且这个属性包含刚好一个注解, 其值包含 <i>A</i>,
 * 其类型是包含注解类型 <i>A</i> 的类型. 则元素 <i>E</i> 上的注解被认为是
 * <em>indirectly present 间接存在</em>.
 *
 * <li> 一个元素 <i>E</i> 上的注解 <i>A</i> 如果满足以下条件之一则
 * 被认为是 <em>present 存在</em>的:
 *
 * <ul>
 *
 * <li><i>A</i> 直接存在在 <i>E</i> 上; 或者
 *
 * <li>没有 <i>A</i> 类型的注解直接存在在 <i>E</i> 上, 并且 <i>E</i> 是一个类,
 * 注解 <i>A</i> 的类型是可继承的注解类型,  <i>A</i> 存在于 <i>E</i>的超类.
 *
 * </ul>
 *
 * <li> 一个元素 <i>E</i> 上的注解 <i>A</i> 如果满足以下条件之一则
 * 被认为是 <em>associated 关联</em>的:
 *
 * <ul>
 *
 * <li><i>A</i> 直接或间接存在在 <i>E</i>上; 或者
 *
 * <li>没有 <i>A</i> 类型的注解直接或间接存在在 <i>E</i> 上, 并且 <i>E</i> 是一个类,
 * <i>A</i> 的类型是可继承的注解类型, 并且 <i>A</i> 和 <i>E</i> 的超类存在关联关系.
 *
 * </ul>
 *
 * </ul>
 *
 * <p>下面的表总结了这个接口检查中的不同方法的注解存在的类型.
 * <p>
 * <table border>
 * <caption>由 AnnotatedElement 不同方法检测到的存在类型的概述 </caption>
 * <tr><th colspan=2></th><th colspan=4>存在的类型</th>
 * <tr><th colspan=2>方法</th><th>直接存在</th><th>间接存在</th><th>存在</th><th>关联</th>
 * <tr><td align=right>{@code T}</td><td>{@link #getAnnotation(Class) getAnnotation(Class&lt;T&gt;)}
 * <td></td><td></td><td>X</td><td></td>
 * </tr>
 * <tr><td align=right>{@code Annotation[]}</td><td>{@link #getAnnotations getAnnotations()}
 * <td></td><td></td><td>X</td><td></td>
 * </tr>
 * <tr><td align=right>{@code T[]}</td><td>{@link #getAnnotationsByType(Class) getAnnotationsByType(Class&lt;T&gt;)}
 * <td></td><td></td><td></td><td>X</td>
 * </tr>
 * <tr><td align=right>{@code T}</td><td>{@link #getDeclaredAnnotation(Class) getDeclaredAnnotation(Class&lt;T&gt;)}
 * <td>X</td><td></td><td></td><td></td>
 * </tr>
 * <tr><td align=right>{@code Annotation[]}</td><td>{@link #getDeclaredAnnotations getDeclaredAnnotations()}
 * <td>X</td><td></td><td></td><td></td>
 * </tr>
 * <tr><td align=right>{@code T[]}</td><td>{@link #getDeclaredAnnotationsByType(Class) getDeclaredAnnotationsByType(Class&lt;T&gt;)}
 * <td>X</td><td>X</td><td></td><td></td>
 * </tr>
 * </table>
 *
 * <p>对于{@code get[Declared]AnnotationsByType(Class <T>)} 的调用,
 * 计算直接或间接存在于元素 <i>E</i> 上的注解的顺序, 就好像
 * <i>E</i> 上的间接存在注解直接存在于 <i>E</i> 上代替其容器注解一样,
 * 按它们出现在容器注解的value元素中的顺序.
 *
 * <p>如果注解类型 <i>T</i> 最初 <em>不</em>可重复并且稍后修改为
 * 可重复, 则需要记住几个兼容性问题.
 * <p>
 * <i>T</i> 的包含注解类型是 <i>TC</i>.
 *
 * <ul>
 *
 * <li> 在源文件和二进制文件中修改 <i>T</i> 为可重复的, 兼容 <i>T</i>
 * 的现有用途以及 <i>TC</i> 的现有用途.
 * <p>
 * 也就是说, 对于源兼容性, 注解类型为 <i>T</i> 或 <i>TC</i> 的源代码仍将编译.
 * 对于二进制兼容性, 注解类型为 <i>T</i> 或类型为 <i>TC</i> 的类文件(或类型为
 * <i>T</i> 或类型 <i>TC</i>）将链接到 <i>T</i>的修改版本, 如果它们与早期版本链接.
 * <p>
 * (在 <i>T</i> 在正式修改为可重复之前, 注解类型 <i>TC</i> 可以非正式地用在包含注解类型的
 * 动作. 或者, 当使 <i>T</i> 可重复时, 可以引入 <i>TC</i> 作为新的类型.)
 *
 * <li>如果一个元素上的 <i>TC</i> 类型注解存在, 并且 <i>T</i> 修改为可重复,
 * <i>TC</i> 作为它的注解类型的容器 :
 *
 * <ul>
 *
 * <li>对于 <i>T</i> 的更改在行为上与 {@code get[Declared]Annotation(Class<T>)}
 * (使用 <i>T</i> 或者 <i>TC</i> 做参数) 和 {@code get[Declared]Annotations()}
 * 方法相符, 因为 <i>TC</i> 变为 <i>T</i> 的包含注解类型, 方法的结果不会改变.
 *
 * <li>对于 <i>T</i> 的更改在结果上和以 <i>T</i> 作为参数调用
 * {@code get[Declared]AnnotationsByType(Class<T>)} 的结果是一致的,
 * 因为这些方法将注解类型 <i>TC</i> 视为 <i>T</i> 的容器注解类型,
 * 并将"查看"它以显示类型 <i>T</i> 的注解.
 *
 * </ul>
 *
 * <li>如果元素 <i>E</i> 上存在 <i>T</i> 类型的注解, 并且 <i>T</i> 可重复使用,
 * 将更多类型为 <i>T</i> 的注解添加到 <i>E</i> 元素上:
 *
 * <ul>
 *
 * <li> <i>T</i> 类型注解的添加即使源代码兼容又是二进制兼容的.
 *
 * <li>添加的 <i>T</i> 类型的注解改变了 {@code get[Declared]Annotation(Class<T>)}
 * 和 {@code get[Declared]Annotations()} 方法的结果, 因为这些方法只会在元素上看到
 * 容器注解类型, 而不会看到类型 <i>T</i> 的注解.
 *
 * <li>添加的 <i>T</i> 类型的注解改变了 {@code get[Declared]AnnotationsByType(Class<T>)}
 * 方法的结果, 因为他们的结果将公开类型 <i>T</i> 的附加注解, 而之前它们只是公开了
 * 类型 <i>T</i> 的单个注解.
 *
 * </ul>
 *
 * </ul>
 *
 * <p>如果此接口中的方法返回一个注解包含(直接或间接)引用此 VM 中无法访问的类的
 * {@link Class} 值成员, 则尝试通过调用相关的类返回方法来读取该类来返回注解,
 * 这将导致 {@link TypeNotPresentException} 异常.
 * wttch: 注解中的参数可能包含了 VM 中无法访问的类.
 *
 * <p>同样地, 尝试读取枚举的值成员将导致 {@link EnumConstantNotPresentException} 异常,
 * 如果注解中的枚举常量不在存在于枚举中.
 *
 * <p>如果一个注解 <i>T</i> 被 {@code @Repeatable} (元)注解,  元素的值被标识为  <i>TC</i>,
 * 但是 <i>TC</i> 没有定义 {@code value()} 返回值为 <i>T</i>{@code []} 的方法, 那么
 * 将抛出 {@link java.lang.annotation.AnnotationFormatError} 类型的异常.
 *
 * <p>最后, 尝试阅读其定义不兼容的成员将导致抛出一个
 * {@link java.lang.annotation.AnnotationTypeMismatchException} 或者
 * {@link java.lang.annotation.IncompleteAnnotationException} 异常.
 * <p>
 * // TODO 最后合并 reflect 分支和 lang(annotation) 分支时, 对 annotation 包中的这些异常进行简单说明
 *
 * @author Josh Bloch
 * @see java.lang.EnumConstantNotPresentException
 * @see java.lang.TypeNotPresentException
 * @see AnnotationFormatError
 * @see java.lang.annotation.AnnotationTypeMismatchException
 * @see java.lang.annotation.IncompleteAnnotationException
 * @since 1.5
 */
public interface AnnotatedElement {
    /**
     * 返回 true 如果元素上有指定类型的注解 <em>存在</em>, 否则 false.
     * 这个方法主要用于方便访问标记注解.
     *
     * <p>此方法返回真值等同于:
     * {@code getAnnotation(annotationClass) != null}
     *
     * <p>默认方法的主体被指定为下面的代码.
     *
     * @param annotationClass 注解类型相应的类对象
     * @return true 如果元素上有指定类型的注解 <em>存在</em>, 否则 false
     * @throws NullPointerException 如果给定的类对象为空
     * @since 1.5
     */
    default boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return getAnnotation(annotationClass) != null;
    }

    /**
     * 返回这个元素上指定类型的注解, 如果这样一个注解 <em>存在</em>, 否则返回 null.
     *
     * @param <T>             如果注解存在, 查询和返回注解的类型
     * @param annotationClass 注解类型相应的类对象
     * @return 这个元素上指定类型的注解, 如果这样一个注解 <em>存在</em>, 否则返回 null
     * @throws NullPointerException 如果给定的类对象为空
     * @since 1.5
     */
    <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    /**
     * 返回元素上 <em>存在</em> 的所有注解.
     * <p>
     * 如果元素上没有注解 <em>存在</em> , 将返回一个长度为 0 的数组.
     * <p>
     * 这个方法的调用者可以自由修改返回的数组; 它不会影响数组的返回值给其他调用者.
     *
     * @return 元素上存在的注解
     * @since 1.5
     */
    Annotation[] getAnnotations();

    /**
     * 返回这个元素上 <em>关联</em> 的注解.
     * <p>
     * 如果元素上没有注解 <em>关联</em> , 将返回一个长度为 0 的数组.
     * <p>
     * 它和 {@link #getAnnotation(Class)} 不同的是
     * 这个方法检测它的参数是一个 <em>可重复的注解</em> (JLS 9.6),
     * 如果这样, 尝试查找一个或者更多这个类型的注解通过"查看"它的包含注解类型.
     * <p>
     * 这个方法的调用者可以自由修改返回的数组; 它不会影响数组的返回值给其他调用者.
     *
     * @param <T>             如果注解存在, 查询和返回注解的类型
     * @param annotationClass 注解类型相应的类对象
     * @return 返回这个元素上所有 <em>关联</em> 的注解数组, 如果没有关联的, 则数组长度为 0
     * @throws NullPointerException 如果指定的类对象为空
     * @implSpec 默认实现先调用 {@link #getDeclaredAnnotationsByType(Class)} 通过
     * {@code annotationClass} 作为参数. 如果数组大小大于 0, 则返回这个数组. 如果返回
     * 的数组长度为 0, {@code AnnotatedElement} 是一个类并且参数类型是一个可继承的注解,
     * {@code AnnotatedElement} 的超类不为空, 则通过调用超类的 {@link Class#getAnnotationsByType(Class)}
     * 使用参数 {@code annotationClass}. 否则返回一个长度为 0 的数组.
     * @since 1.8
     */
    default <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
        /*
         * 定义关联: 直接或间接存在 或者 直接和间接都不存在 并且 元素是个 Class, 可继承类型的注解,
         * 并且这个注解类型和这个元素的超类是关联的.
         */
        T[] result = getDeclaredAnnotationsByType(annotationClass);

        if (result.length == 0 && // 没有直接或间接存在的关系
                this instanceof Class && // 元素是类 (不是字段或者方法等)
                AnnotationType.getInstance(annotationClass).isInherited()) { // 可继承的
            Class<?> superClass = ((Class<?>) this).getSuperclass();
            if (superClass != null) {
                // 确定注解是否和超类相关联
                result = superClass.getAnnotationsByType(annotationClass);
            }
        }

        return result;
    }

    /**
     * 返回这个元素上的指定类型的注解, 如果这个注解是 <em>直接存在</em>, 否则 null .
     * <p>
     * 这个方法忽略继承注解. (返回 null 如果没有注解直接存在在这个元素.)
     *
     * @param <T>             如果注解存在, 查询和返回注解的类型
     * @param annotationClass 注解类型相应的类对象
     * @return 这个元素上的指定类型的注解, 如果这个注解是 <em>直接存在</em>, 否则 null .
     * @throws NullPointerException 如果给定的类对象为空
     * @implSpec 默认实现首先执行非空检测, 之后循环遍历 {@link #getDeclaredAnnotations}
     * 的结果, 返回其注解类型与参数类型匹配的第一个注解.
     * @since 1.8
     */
    default <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
        // 非空检测
        Objects.requireNonNull(annotationClass);
        // 循环遍历直接存在的注解, 直到找到相匹配的一个
        for (Annotation annotation : getDeclaredAnnotations()) {
            if (annotationClass.equals(annotation.annotationType())) {
                // 更强大, 可以在运行时进行动态转换, 而不仅仅是编译时.
                return annotationClass.cast(annotation);
            }
        }
        return null;
    }

    /**
     * 返回元素的指定类型的注解如果这个注解是 <em>直接类型</em> 或 <em>间接类型</em>.
     * 这个方法忽略继承注解.
     * <p>
     * 如果没有指定类型的注解直接或间接存在在这个元素上, 则返回长度为 0 的数组.
     * <p>
     * 它和 {@link #getDeclaredAnnotation(Class)}  不同的是
     * 这个方法检测它的参数是一个 <em>可重复的注解</em> (JLS 9.6),
     * 如果这样, 尝试查找一个或者更多这个类型的注解通过"查看"它的包含注解类型.
     * <p>
     * 这个方法的调用者可以自由修改返回的数组; 它不会影响数组的返回值给其他调用者.
     *
     * @param <T>             如果注解存在, 查询和返回注解的类型
     * @param annotationClass 注解类型相应的类对象
     * @return 元素的指定类型的注解如果这个注解是 <em>直接类型</em> 或 <em>间接类型</em>.
     * @throws NullPointerException 如果给定的类对象为空
     * @implSpec 默认实现可能调用 {@link #getDeclaredAnnotation(Class)} 一次或多次查找
     * 直接存在的注解, 如果这个注解类型是可重复的, 查找包含注解类型. 如果注解类型 {@code annotationClass}
     * 直接或间接存在被发现, 那么{@link #getDeclaredAnnotations()} 方法将被调用以确定返回数组中元素的顺序.
     *
     * <p>或者，默认实现可以一次调用 {@link #getDeclaredAnnotations()},
     * 并检查返回的数组是否直接和间接地呈现注解. 调用{@link #getDeclaredAnnotations()}的结果
     * 假定与调用{@link #getDeclaredAnnotation(Class)} 的结果一致.
     * @since 1.8
     */
    default <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
        Objects.requireNonNull(annotationClass);
        return AnnotationSupport.
                getDirectlyAndIndirectlyPresent(Arrays.stream(getDeclaredAnnotations()).
                                collect(Collectors.toMap(Annotation::annotationType,
                                        Function.identity(),
                                        ((first, second) -> first),
                                        LinkedHashMap::new)),
                        annotationClass);
    }

    /**
     * 返回这个元素上 <em>直接存在</em> 的注解. 这个方法将忽略继承的注解.
     * <p>
     * 如果元素上没有注解 <em>直接存在</em> , 将返回一个长度为 0 的数组.
     * <p>
     * 这个方法的调用者可以自由修改返回的数组; 它不会影响数组的返回值给其他调用者.
     *
     * @return 直接存在于元素上的所有注解
     * @since 1.5
     */
    Annotation[] getDeclaredAnnotations();
}
