package java.lang;

import java.util.Objects;

/**
 * 堆栈跟踪中的一个元素, 由 {@link Throwable#getStackTrace()} 返回.
 * 每个元素代表一个堆栈帧. 除堆栈顶部的堆栈帧之外的所有堆栈帧表示方法调用.
 * 通常, 这是创建与堆栈跟踪相对应的 Throwable 的点.
 *
 * @author Josh Bloch
 * @since 1.4
 */
public final class StackTraceElement implements java.io.Serializable {
    // 通常由VM初始化(1.5中添加的公共构造函数)
    private String declaringClass;
    private String methodName;
    private String fileName;
    private int lineNumber;

    /**
     * 创建表示指定执行点的堆栈跟踪元素.
     *
     * @param declaringClass 包含由堆栈跟踪元素表示的执行点的类的完全限定名称
     * @param methodName     包含由堆栈跟踪元素表示的执行点的方法的名称
     * @param fileName       包含由堆栈跟踪元素表示的执行点的文件的名称, 如果此信息不可用, 则为 {@code null}
     * @param lineNumber     包含由堆栈跟踪元素表示的执行点的源行的行号, 如果此信息不可用, 则为负数.
     *                       -2 表示包含执行点的方法是本机方法
     * @throws NullPointerException 如果 {@code declaringClass} 或者 {@code methodName} 为 null
     * @since 1.5
     */
    public StackTraceElement(String declaringClass, String methodName,
                             String fileName, int lineNumber) {
        this.declaringClass = Objects.requireNonNull(declaringClass, "Declaring class is null");
        this.methodName = Objects.requireNonNull(methodName, "Method name is null");
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }

    /**
     * 返回包含此堆栈跟踪元素表示的执行点的源文件的名称. 通常, 这对应于相关 {@code class} 文件的
     * {@code SourceFile}属性(根据<i>Java虚拟机规范</i>, 第4.7.7节). 在某些系统中,
     * 名称可以指代除文件之外的某些源代码单元, 例如源存储库中的条目.
     *
     * @return 包含此堆栈跟踪元素表示的执行点的文件的名称, 如果此信息不可用, 则为 {@code null}.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 返回包含此堆栈跟踪元素表示的执行点的源代码行号. 通常, 这是从相关 {@code class} 文件的
     * {@code LineNumberTable} 属性派生的(根据<i>Java虚拟机规范</i>, 第4.7.8节).
     *
     * @return 包含此堆栈跟踪元素表示的执行点的源行的行号, 如果此信息不可用, 则为负数.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * 返回包含此堆栈跟踪元素表示的执行点的类的完全限定名称.
     *
     * @return 包含此堆栈跟踪元素表示的执行点的 {@code Class} 的完全限定名称.
     */
    public String getClassName() {
        return declaringClass;
    }

    /**
     * 返回包含此堆栈跟踪元素表示的执行点的方法的名称. 如果执行点包含在实例或类初始化程序中,
     * 则此方法将返回相应的 <i>特殊方法名称</i>, {@code <init>} 或 {@code <clinit>},
     * 如第3.9节所述 <i>Java虚拟机规范</i>.
     *
     * @return 包含此堆栈跟踪元素表示的执行点的方法的名称.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 如果包含此堆栈跟踪元素表示的执行点的方法是本机方法, 则返回true.
     *
     * @return {@code true} 如果包含此堆栈跟踪元素表示的执行点的方法是本机方法.
     */
    public boolean isNativeMethod() {
        return lineNumber == -2;
    }

    /**
     * 返回此堆栈跟踪元素的字符串表示形式.
     * 此字符串的格式取决于实现, 但以下示例可能被视为典型:
     * <ul>
     * <li>
     * {@code "MyClass.mash(MyClass.java:9)"} - 这里,
     * {@code "MyClass"} 是包含此堆栈跟踪元素表示的执行点的类的<i>完全限定名称</i>,
     * {@code "mash"} 是包含该方法执行点的名称,
     * {@code "MyClass.java"} 是包含执行点的源文件,
     * {@code "9"} 是包含执行点的源代码行号.
     * <li>
     * {@code "MyClass.mash(MyClass.java)"} - 如上所述, 但行号不可用.
     * <li>
     * {@code "MyClass.mash(Unknown Source)"} - 如上所述, 但文件名和行号都不可用.
     * <li>
     * {@code "MyClass.mash(Native Method)"} - 如上所述, 但文件名和行号都不可用, 并且包含执行点的方法已知是本机方法.
     * </ul>
     *
     * @see Throwable#printStackTrace()
     */
    public String toString() {
        // 本地方法: (Native Method)
        //     fileName 存在并且 lineNumber >= 0: (fileName:lineNumber)
        //         fileName 存在: (fileName)
        //             (Unknown Source)
        return getClassName() + "." + methodName +
                (isNativeMethod() ? "(Native Method)" :
                        (fileName != null && lineNumber >= 0 ?
                                "(" + fileName + ":" + lineNumber + ")" :
                                (fileName != null ? "(" + fileName + ")" : "(Unknown Source)")));
    }

    /**
     * 如果指定的对象是另一个表示与此实例相同的执行点的 {@code StackTraceElement} 实例, 则返回 true.
     * 当且仅当以下情况时, 两个堆栈跟踪元素 {@code a} 和 {@code b} 是相等的:
     * <pre>{@code
     *     equals(a.getFileName(), b.getFileName()) &&
     *     a.getLineNumber() == b.getLineNumber()) &&
     *     equals(a.getClassName(), b.getClassName()) &&
     *     equals(a.getMethodName(), b.getMethodName())
     * }</pre>
     * 其中 {@code equals} 的语义为 {@link java.util.Objects#equals(Object, Object)}.
     *
     * @param obj 要与此堆栈跟踪元素进行比较的对象.
     * @return 如果指定的对象是另一个表示与此实例相同的执行点的 {@code StackTraceElement} 实例,
     * 则返回true.
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof StackTraceElement))
            return false;
        StackTraceElement e = (StackTraceElement) obj;
        return e.declaringClass.equals(declaringClass) &&
                e.lineNumber == lineNumber &&
                Objects.equals(methodName, e.methodName) &&
                Objects.equals(fileName, e.fileName);
    }

    /**
     * 返回此堆栈跟踪元素的哈希值.
     * <p>
     * wttch: 使用 31 作为乘子的原因:
     * 1. 优选的质数, 也可以 37/41/43 等较小的质数, 可以预防一些 hash 冲突;
     * 2. 31 可以被 JVM 调优, 31 * i = (i << 5) - i;
     */
    public int hashCode() {
        int result = 31 * declaringClass.hashCode() + methodName.hashCode();
        result = 31 * result + Objects.hashCode(fileName);
        result = 31 * result + lineNumber;
        return result;
    }

    private static final long serialVersionUID = 6992337162326171013L;
}
