package java.lang;

/**
 * Boolean 类在对象中包装基本类型 {@code boolean} 的值.
 * 类型为 {@code Boolean} 的对象包含单个字段, 其基本类型为 {@code boolean}.
 * <p>
 * 此外, 这个类提供了许多方法, 用于将 {@code boolean} 转换为 {@code String},
 * 将 {@code String} 转换为 {@code boolean}, 以及处理 {@code boolean} 时有用的
 * 其他常量和方法.
 *
 * @author Arthur van Hoff
 * @since JDK1.0
 */
public final class Boolean implements java.io.Serializable,
        Comparable<Boolean> {
    /**
     * {@code Boolean} 对象对应原始值 {@code true}.
     */
    public static final Boolean TRUE = new Boolean(true);

    /**
     * {@code Boolean} 对象对应原始值 {@code false}.
     */
    public static final Boolean FALSE = new Boolean(false);

    /**
     * 表示基本类型 boolean 的类对象.
     *
     * @since JDK1.1
     */
    @SuppressWarnings("unchecked")
    public static final Class<Boolean> TYPE = (Class<Boolean>) Class.getPrimitiveClass("boolean");

    /**
     * Boolean 对象的值.
     *
     * @serial
     */
    private final boolean value;

    /**
     * 使用 JDK 1.0.2 中的 serialVersionUID 实现互操作性.
     */
    private static final long serialVersionUID = -3665804199014368530L;

    /**
     * 分配表示 {@code value} 参数的 {@code Boolean} 对象.
     *
     * <p><b>注意: 使用此构造函数很少合适. 除非需要 <i>new</i> 实例, 否则静态工厂
     * {@link #valueOf(boolean)} 通常是最好的选择. 它可能会产生明显更好的空间和时间性能</b>
     *
     * @param value {@code Boolean} 对象的值.
     */
    public Boolean(boolean value) {
        this.value = value;
    }

    /**
     * 如果字符串参数不是 {@code null} 并且忽略大小等于 {@code "true}, 则分配代表值 {@code true}
     * 的 {@code Boolean} 对象. 否则, 分配一个表示值 {@code false} 的 {@code Boolean} 对象.
     * 示例: <p>
     * {@code new Boolean("True")} 生成一个代表 {@code true} 的 {@code Boolean} 对象,
     * {@code new Boolean("yes")} 生成一个代表 {@code false} 的 {@code Boolean} 对象.
     *
     * @param s 要转换为 {@code Boolean} 的字符串.
     */
    public Boolean(String s) {
        this(parseBoolean(s));
    }

    /**
     * 解析字符串对象为 Boolean 对象.
     * 如果字符串参数不是 {@code null} 并且忽略大小等于 {@code "true}, 则分配代表值 {@code true}
     * 示例: <p>
     * {@code new Boolean("True")} 生成一个代表 {@code true} 的 {@code Boolean} 对象,
     * {@code new Boolean("yes")} 生成一个代表 {@code false} 的 {@code Boolean} 对象.
     *
     * @param s 包含要解析的布尔表示的 {@code String}
     * @return 字符串参数表示的布尔值
     * @since 1.5
     */
    public static boolean parseBoolean(String s) {
        return ((s != null) && s.equalsIgnoreCase("true"));
    }

    /**
     * 以基础类型 boolean 的形式返回此 {@code Boolean} 对象的值.
     *
     * @return 这个对象的原始 {@code boolean} 值.
     */
    public boolean booleanValue() {
        return value;
    }

    /**
     * 返回表示指定的 {@code boolean} 值的 {@code Boolean} 实例. 如果指定的 {@code boolean} 值为
     * {@code true}, 则该方法返回 {@code Boolean.TRUE}; 如果是 {@code false}, 则此方法返回 {@code Boolean.FALSE}.
     * 如果不需要新的 {@code Boolean} 实例, 而不是构造函数 {@link #Boolean(boolean)},
     * 因为此方法可能会产生明显更好的空间和时间性能.
     *
     * @param b 布尔值.
     * @return 表示 {@code b} 的 {@code Boolean} 实例.
     * @since 1.4
     */
    public static Boolean valueOf(boolean b) {
        return (b ? TRUE : FALSE);
    }

    /**
     * 返回 {@code Boolean} 其值由指定的字符串表示. 返回的 {@code Boolean} 表示如果字符串参数
     * 不是 {@code null} 并且和字符串 {@code "true"} 忽略大小写, 则 true 值.
     *
     * @param s 一个字符串.
     * @return 由字符串表示的 {@code Boolean} 值.
     */
    public static Boolean valueOf(String s) {
        return parseBoolean(s) ? TRUE : FALSE;
    }

    /**
     * 返回表示指定 boolean 的 {@code String} 对象. 如果指定的布尔值为 {@code true},
     * 则返回字符串 {@code "true"}, 否则则返回字符串 {@code "false"}.
     *
     * @param b 要转换的布尔值
     * @return 指定的 {@code boolean} 的字符串表示
     * @since 1.4
     */
    public static String toString(boolean b) {
        return b ? "true" : "false";
    }

    /**
     * 返回表示此布尔值的 {@code String} 对象. 如果此对象表示值 {@code true},
     * 则返回等于 {@code "true"} 的字符串. 否则, 返回等于 {@code "false"} 的字符串.
     *
     * @return 此对象的字符串表示形式.
     */
    public String toString() {
        return value ? "true" : "false";
    }

    /**
     * 返回此 {@code Boolean} 对象的哈希码.
     *
     * @return 整数 {@code 1231} 如果此对象代表 {@code true};
     * 如果此对象表示 {@code false}, 则返回整数 {@code 1237}.
     */
    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    /**
     * 返回 {@code boolean} 值的哈希码; 兼容 {@code Boolean.hashCode()}.
     *
     * @param value 要做 hash 处理的值
     * @return {@code boolean} 值的哈希码值.
     * @since 1.8
     */
    public static int hashCode(boolean value) {
        return value ? 1231 : 1237;
    }

    /**
     * 当且仅当参数不是 {@code null} 并且是 {@code Boolean} 对象时, 返回{@code true},
     * 该对象表示与此对象相同的 {@code boolean} 值.
     *
     * @param obj 要与之比较的对象.
     * @return 如果布尔对象表示相同的值则返回 {@code true}; 否则返回 {@code false}.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Boolean) {
            return value == ((Boolean) obj).booleanValue();
        }
        return false;
    }

    /**
     * 当且仅当参数指定的系统属性存在且等于且等于字符串 {@code "true"} 时, 才返回 {@code true}.
     * (从Java <small><sup> TM </sup></small> 平台的 1.0.2 版开始, 此字符串的测试不区分大小写.)
     * 系统属性可通过 {@code System} 类中定义的 {@code getProperty} 方法中获取.
     * <p>
     * 如果没有具有指定名称的属性, 或者指定的名称为空或 null, 则返回 {@code false}.
     *
     * @param name 系统属性名称.
     * @return 系统属性的 {@code boolean} 值.
     * @throws SecurityException 处于和 {@link System#getProperty(String) System.getProperty}
     *                           同样的原因
     * @see java.lang.System#getProperty(java.lang.String)
     * @see java.lang.System#getProperty(java.lang.String, java.lang.String)
     */
    public static boolean getBoolean(String name) {
        boolean result = false;
        try {
            result = parseBoolean(System.getProperty(name));
        } catch (IllegalArgumentException | NullPointerException e) {
        }
        return result;
    }

    /**
     * 将此 {@code Boolean} 实例与另一个实例进行比较.
     *
     * @param b 要比较的 {@code Boolean} 实例
     * @return 如果此对象表示与参数相同的布尔值, 则返回零; 如果此对象表示 true 且参数表示 false, 则为正值;
     * 如果此对象表示 false 且参数表示 true, 则为负值.Wttch: 即 true > false ?
     * @throws NullPointerException 如果参数是 {@code null}
     * @see Comparable
     * @since 1.5
     */
    public int compareTo(Boolean b) {
        return compare(this.value, b.value);
    }

    /**
     * 比较两个 {@code boolean} 值. 返回的值与返回的值相同:
     * <pre>
     *    Boolean.valueOf(x).compareTo(Boolean.valueOf(y))
     * </pre>
     *
     * @param x 要比较的第一个 {@code boolean}
     * @param y 要比较的第二个 {@code boolean}
     * @return 如果 {@code x == y}, 则值为 {@code 0}; 如果 {@code !x && y}, 则值小于 {@code 0};
     * 如果 {@code x &&!y}, 则值大于 {@code 0}
     * @since 1.7
     */
    public static int compare(boolean x, boolean y) {
        return (x == y) ? 0 : (x ? 1 : -1);
    }

    /**
     * 返回将逻辑 AND 运算符应用于指定的 {@code boolean} 操作数的结果.
     *
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @return {@code a} 和 {@code b} 的逻辑 AND 结果.
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static boolean logicalAnd(boolean a, boolean b) {
        return a && b;
    }


    /**
     * 返回将逻辑 OR 运算符应用于指定的 {@code boolean} 操作数的结果.
     *
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @return {@code a} 和 {@code b} 的逻辑 OR 结果.
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static boolean logicalOr(boolean a, boolean b) {
        return a || b;
    }


    /**
     * 返回将逻辑 XOR 运算符应用于指定的 {@code boolean} 操作数的结果.
     *
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @return {@code a} 和 {@code b} 的逻辑 XOR 结果.
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static boolean logicalXor(boolean a, boolean b) {
        return a ^ b;
    }
}
