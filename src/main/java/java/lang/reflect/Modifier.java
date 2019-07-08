package java.lang.reflect;

import sun.reflect.ReflectionFactory;

import java.security.AccessController;

/**
 * Modifier 类提供 {@code static} 方法和常量来解码类和成员的访问修饰符.
 * 修饰符集表示为具有表示不同修饰符的不同 bit 位置的整数.
 * 此常量值表示的修饰符请查看 <cite>java虚拟机规范</cite> 的 4.1, 4.4, 4.5 和 4.7 节.
 *
 * @author Nakul Saraiya
 * @author Kenneth Russell
 * @see Class#getModifiers()
 * @see Member#getModifiers()
 */
public class Modifier {

    /*
     * java.lang 和 java.lang.reflect 之间的引导协议.
     */
    static {
        sun.reflect.ReflectionFactory factory =
                AccessController.doPrivileged(
                        new ReflectionFactory.GetReflectionFactoryAction());
        factory.setLangReflectAccess(new java.lang.reflect.ReflectAccess());
    }

    /**
     * 返回 {@code true} 如果整型参数包含 {@code public} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code public} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isPublic(int mod) {
        return (mod & PUBLIC) != 0;
    }

    /**
     * 返回 {@code true} 如果整型参数包含 {@code private} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code private} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isPrivate(int mod) {
        return (mod & PRIVATE) != 0;
    }

    /**
     * 返回 {@code true} 如果整型参数包含 {@code protected} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code protected} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isProtected(int mod) {
        return (mod & PROTECTED) != 0;
    }

    /**
     * 返回 {@code true} 如果整型参数包含 {@code static} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code static} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isStatic(int mod) {
        return (mod & STATIC) != 0;
    }

    /**
     * 返回 {@code true} 如果整型参数包含 {@code final} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code final} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isFinal(int mod) {
        return (mod & FINAL) != 0;
    }


    /**
     * 返回 {@code true} 如果整型参数包含 {@code synchronized} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code synchronized} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isSynchronized(int mod) {
        return (mod & SYNCHRONIZED) != 0;
    }

    /**
     * 返回 {@code true} 如果整型参数包含 {@code volatile} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code volatile} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isVolatile(int mod) {
        return (mod & VOLATILE) != 0;
    }


    /**
     * 返回 {@code true} 如果整型参数包含 {@code transient} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code transient} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isTransient(int mod) {
        return (mod & TRANSIENT) != 0;
    }


    /**
     * 返回 {@code true} 如果整型参数包含 {@code native} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code native} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isNative(int mod) {
        return (mod & NATIVE) != 0;
    }


    /**
     * 返回 {@code true} 如果整型参数包含 {@code interface} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code interface} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isInterface(int mod) {
        return (mod & INTERFACE) != 0;
    }


    /**
     * 返回 {@code true} 如果整型参数包含 {@code abstract} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code abstract} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isAbstract(int mod) {
        return (mod & ABSTRACT) != 0;
    }

    /**
     * 返回 {@code true} 如果整型参数包含 {@code strictfp} 修饰符, 否则 {@code false}.
     *
     * @param mod 一个修饰符集合
     * @return {@code true} 如果 {@code mod} 包含 {@code strictfp} 修饰符;
     * 否则 {@code false} .
     */
    public static boolean isStrict(int mod) {
        return (mod & STRICT) != 0;
    }

    /**
     * 返回指定描述符集合中的访问描述符标志的字符串描述形式. 例如:
     * <blockquote><pre>
     *    public final synchronized strictfp
     * </pre></blockquote>
     * 描述符名字的排序按照 <cite>java 语言规范</cite>
     * 8.1.1, 8.3.1, 8.4.3, 8.8.3 和 9.1.1 节中建议的排序规则.
     * 完整的描述符排序方法是:
     * <blockquote> {@code public protected private abstract static final transient
     * volatile synchronized native strictfp interface } </blockquote>
     * 在 java 语言中{@code interface} 描述符在此类中理论上是正确的描述符
     * 并且它出现在此方法列出的所有其他修饰符之后.
     * 此方法可能返回一串修饰符, 这些修饰符不是 Java 实体的有效修饰符;
     * 换句话说, 没有检查由输入表示的修饰符组合的可能有效性.
     * <p>
     * 请注意, 要对已知类型的实体(例如构造器和方法) 执行此类检查,
     * 首先使用 {@link #constructorModifiers()} 或 {@link #methodModifiers()} 等
     * 方法中的适当掩码与 {@code toString} 参数的和.
     *
     * @param mod 一个描述符数组
     * @return 由{@code mod}表示的修饰符集的字符串表示
     */
    public static String toString(int mod) {
        StringBuilder sb = new StringBuilder();
        int len;

        if ((mod & PUBLIC) != 0) sb.append("public ");
        if ((mod & PROTECTED) != 0) sb.append("protected ");
        if ((mod & PRIVATE) != 0) sb.append("private ");

        /* Canonical order */
        if ((mod & ABSTRACT) != 0) sb.append("abstract ");
        if ((mod & STATIC) != 0) sb.append("static ");
        if ((mod & FINAL) != 0) sb.append("final ");
        if ((mod & TRANSIENT) != 0) sb.append("transient ");
        if ((mod & VOLATILE) != 0) sb.append("volatile ");
        if ((mod & SYNCHRONIZED) != 0) sb.append("synchronized ");
        if ((mod & NATIVE) != 0) sb.append("native ");
        if ((mod & STRICT) != 0) sb.append("strictfp ");
        if ((mod & INTERFACE) != 0) sb.append("interface ");

        if ((len = sb.length()) > 0)    /* 修剪尾随空间 */
            return sb.toString().substring(0, len - 1);
        return "";
    }

    /*
     * 来自 <cite>Java 语言规范</cite> 表4.1, 4.4, 4.5 和 4.7 的访问修饰符标志常量
     */
    /**
     * {@code int} 值表示 {@code public} 修饰符.
     */
    public static final int PUBLIC = 0x00000001;
    /**
     * {@code int} 值表示 {@code private} 修饰符.
     */
    public static final int PRIVATE = 0x00000002;
    /**
     * {@code int} 值表示 {@code protected} 修饰符.
     */
    public static final int PROTECTED = 0x00000004;
    /**
     * {@code int} 值表示 {@code static} 修饰符.
     */
    public static final int STATIC = 0x00000008;
    /**
     * {@code int} 值表示 {@code final} 修饰符.
     */
    public static final int FINAL = 0x00000010;
    /**
     * {@code int} 值表示 {@code synchronized} 修饰符.
     */
    public static final int SYNCHRONIZED = 0x00000020;
    /**
     * {@code int} 值表示 {@code volatile} 修饰符.
     */
    public static final int VOLATILE = 0x00000040;
    /**
     * {@code int} 值表示 {@code transient} 修饰符.
     */
    public static final int TRANSIENT = 0x00000080;
    /**
     * {@code int} 值表示 {@code native} 修饰符.
     */
    public static final int NATIVE = 0x00000100;
    /**
     * {@code int} 值表示 {@code interface} 修饰符.
     */
    public static final int INTERFACE = 0x00000200;
    /**
     * {@code int} 值表示 {@code abstract} 修饰符.
     */
    public static final int ABSTRACT = 0x00000400;
    /**
     * {@code int} 值表示 {@code strictfp} 修饰符.
     */
    public static final int STRICT = 0x00000800;

    // 未在公共 API 中公开的位, 因为它们对字段和方法有不同的含义,
    // 并且无法区分此类中的两者, 或者因为它们不是 Java 编程语言关键字
    static final int BRIDGE = 0x00000040;
    static final int VARARGS = 0x00000080;
    static final int SYNTHETIC = 0x00001000;
    static final int ANNOTATION = 0x00002000;
    static final int ENUM = 0x00004000;
    static final int MANDATED = 0x00008000;

    /**
     * 由 Java 编译器生成的成员.
     */
    static boolean isSynthetic(int mod) {
        return (mod & SYNTHETIC) != 0;
    }

    /**
     * 强制性? 强制类型转换?
     * TODO Modifier#isMandated
     */
    static boolean isMandated(int mod) {
        return (mod & MANDATED) != 0;
    }

    // 关于 FOO_MODIFIERS 字段和 fooModifiers() 方法的注意事项:
    // 不能保证修饰符集是跨时间和 Java SE 版本的常量. 因此, 将此外部接口
    // 公开给那些允许将值视为 Java-level 常量的信息是不合适的, 因为这些值
    // 可能使常量折叠并且错过修饰符集合更新. 因此, fooModifiers() 方法
    // 返回给定版本的不变值, 但是可能随时间变化的值.

    /**
     * 可以应用于类的 Java 源修饰符.
     * public, protected, private, abstract, static, final, strict
     *
     * @jls 8.1.1 Class Modifiers
     */
    private static final int CLASS_MODIFIERS =
            Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
                    Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL |
                    Modifier.STRICT;

    /**
     * 可以应用于接口的 Java 源修饰符.
     * public, protected, private, abstract, static, strict
     *
     * @jls 9.1.1 Interface Modifiers
     */
    private static final int INTERFACE_MODIFIERS =
            Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
                    Modifier.ABSTRACT | Modifier.STATIC | Modifier.STRICT;


    /**
     * 可以应用于构造函数的 Java 源修饰符.
     * public, protected, private
     *
     * @jls 8.8.3 Constructor Modifiers
     */
    private static final int CONSTRUCTOR_MODIFIERS =
            Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

    /**
     * 可以应用于方法的 Java 源修饰符.
     * public, protected, private, abstract, static, final, synchronized, native, strict
     *
     * @jls8.4.3 Method Modifiers
     */
    private static final int METHOD_MODIFIERS =
            Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
                    Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL |
                    Modifier.SYNCHRONIZED | Modifier.NATIVE | Modifier.STRICT;

    /**
     * 可以应用于字段的 Java 源修饰符.
     * public, protected, private, static, final, transient, volatile
     *
     * @jls 8.3.1  Field Modifiers
     */
    private static final int FIELD_MODIFIERS =
            Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
                    Modifier.STATIC | Modifier.FINAL | Modifier.TRANSIENT |
                    Modifier.VOLATILE;

    /**
     * 可以应用于构造器或者方法参数的 Java 源修饰符.
     * final
     *
     * @jls 8.4.1 Formal Parameters
     */
    private static final int PARAMETER_MODIFIERS =
            Modifier.FINAL;

    /**
     * 访问性修饰符
     * public, protected, private
     */
    static final int ACCESS_MODIFIERS =
            Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

    /**
     * 返回或将可应用于类的源语言的修饰符组合在一起一个 {@code int} 值.
     *
     * @return 或将可应用于类的源语言的修饰符组合在一起一个 {@code int} 值
     * @jls 8.1.1 Class Modifiers
     * @since 1.7
     */
    public static int classModifiers() {
        return CLASS_MODIFIERS;
    }

    /**
     * 返回或将可应用于接口的源语言的修饰符组合在一起的一个 {@code int} 值.
     *
     * @return 或将可应用于接口的源语言的修饰符组合在一起的一个 {@code int} 值.
     * @jls 9.1.1 Interface Modifiers
     * @since 1.7
     */
    public static int interfaceModifiers() {
        return INTERFACE_MODIFIERS;
    }

    /**
     * 返回或将可应用于构造器的源语言的修饰符组合在一起的一个 {@code int} 值.
     *
     * @return 或将可应用于构造器的源语言的修饰符组合在一起的一个 {@code int} 值
     * @jls 8.8.3 Constructor Modifiers
     * @since 1.7
     */
    public static int constructorModifiers() {
        return CONSTRUCTOR_MODIFIERS;
    }

    /**
     * 返回或将可应用于方法的源语言的修饰符组合在一起的一个 {@code int} 值.
     *
     * @return 或将可应用于方法的源语言的修饰符组合在一起的一个 {@code int} 值
     * @jls 8.4.3 Method Modifiers
     * @since 1.7
     */
    public static int methodModifiers() {
        return METHOD_MODIFIERS;
    }

    /**
     * 返回或将可应用于字段的源语言的修饰符组合在一起的一个 {@code int} 值.
     *
     * @return 或将可应用于字段的源语言的修饰符组合在一起的一个 {@code int} 值
     * @jls 8.3.1 Field Modifiers
     * @since 1.7
     */
    public static int fieldModifiers() {
        return FIELD_MODIFIERS;
    }

    /**
     * 返回或将可应用于构造器或者方法参数的源语言的修饰符组合在一起的一个 {@code int} 值.
     *
     * @return 或将可应用于构造器或者方法参数的源语言的修饰符组合在一起的一个 {@code int} 值
     * @jls 8.4.1 Formal Parameters
     * @since 1.8
     */
    public static int parameterModifiers() {
        return PARAMETER_MODIFIERS;
    }
}
