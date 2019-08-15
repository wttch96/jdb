package java.lang;

import java.io.*;
import java.util.*;

/**
 * {@code Throwable} 是 Java 语言中所有错误和异常的超类.
 * 只有作为此类(或其子类之一)的实例对象才能被 JVM 抛出, 或者可被 Java {@code throw} 语句抛出.
 * 同样, 只有作为此类(或器子类之一)才可以作为 {@code catch} 的参数类型.
 * <p>
 * 出于编译时检查异常的目的, {@code Throwable} 和任何 {@code Throwable} 的子类
 * (但不是 {@link RuntimeException} 或 {@link Error} 的子类) 都被视为受检异常.
 *
 * <p>{@link java.lang.Error} 和 {@link java.lang.Exception} 两者之一的子类,
 * 通常用于表示发生了特殊情况. 通常, 这些事例是在特殊情况下新创建的, 以便包含相关信息
 * (例如, 堆栈跟踪数据).
 *
 * <p>Throwable 包含其创建时线程执行堆栈的快照. 它也可以包含一条关于错误信息的字符串.
 * 随着时间的推移, Throwable 可以{@linkplain Throwable#addSuppressed(Throwable)}抑制
 * 其他 Throwable 的传播. 最后, Throwable 还可以包含 <i>cause</i>: 另一个 Throwable 导致
 * 构造了这个 Throwable. 这种因果信息的记录被称为 <i>链式异常</i>, 因为原因本身可能有原因等等,
 * 导致一个异常链, 每一个异常都有另一个引起.
 *
 * <p>Throwable 被抛出的一个原因是抛出它的类是在较低的分层抽象上构建的, 并且由于下层的故障而导致上层的操作失败.
 * 让下层抛出的 Throwable 向外传播是不好的设计, 因为它通常与上层提供的抽象无关.
 * 此外, 假设下层的异常是一个受检异常, 这样做会将上层的 API 与其实现的细节联系起来.
 * 抛出"包装异常"(即包含原因的异常)允许上层将故障的细节传达给其调用者, 而不会产生这些缺点中的任何一个.
 * 它保留了更改上层实现的灵活性, 而无需更改其API(特别是方法抛出的异常集).
 *
 * <p>Throwable 被抛出的第二个原因是抛出它的方法必须符合通用接口, 该接口不允许该方法直接抛出异常的原因.
 * 例如, 假设持久化集合符合 {@link java.util.Collection Collection} 接口, 并且其持久性在
 * {@code java.io} 上实现. 假设 {@code add} 方法的内部可以抛出 {@link java.io.IOException IOException}.
 * 通过将 {@code IOException} 包装在适当的未经检查的异常中, 实现可以将 {@code IOException} 的详细信息
 * 传递给其调用者, 同时符合 {@code Collection} 接口. (持久化集合的操作规范应该表明它能够抛出这样的例外.)
 *
 * <p>原因可以通过两种方式与 Throwable 相关联: 通过将原因作为参数的构造函数, 或通过
 * {@link #initCause(Throwable)} 方法. 希望允许原因和它们相关联的新的可抛出类应该提供构造函数,
 * 这些构造函数将一个原因和委托(可能是间接的)带到一个 {@code Throwable} 构造函数中.
 * <p>
 * 因为 {@code initCause} 方法是公共的, 所以它允许一个原因与任何 Throwable 相关联, 甚至是一个
 * "遗产 Throwable", 其实早于 {@code Throwable} 添加异常链机制.
 *
 * <p>按照惯例, 类 {@code Throwable} 及其子类有两个构造函数, 一个不带参数,
 * 另一个带有 {@code String} 参数, 可用于生成详细消息.
 * 此外, 那些可能与它们相关的原因的子类应该有两个构造函数, 一个带有 {@code Throwable}(原因),
 * 另一个带有 {@code String}(详细消息)和 {@code Throwable}(原因).
 *
 * @author unascribed
 * @author Josh Bloch (Added exception chaining and programmatic access to
 * stack trace in 1.4.)
 * @jls 11.2 Compile-Time Checking of Exceptions
 * @since JDK1.0
 */
public class Throwable implements Serializable {
    /**
     * 使用 JDK1.0.2 中的 serialVersionUID 实现互操作性.
     */
    private static final long serialVersionUID = -3042686055658047285L;

    /**
     * 本机代码在此插槽中保存了堆栈回溯的一些指示.
     */
    private transient Object backtrace;

    /**
     * 关于 Throwable 的具体细节.  例如, 对于 {@code FileNotFoundException},
     * 它包含无法找到的文件的名称.
     *
     * @serial
     */
    private String detailMessage;


    /**
     * Holder 类推迟初始化仅用于序列化的哨兵(标记)对象.
     */
    private static class SentinelHolder {
        /**
         * {@linkplain #setStackTrace(StackTraceElement[]) 设置堆栈跟踪}
         * 到包含此哨兵(标记)值的单元素数组表示将忽略将来设置堆栈跟踪的尝试.
         * 哨兵(标记)等于调用 {@code new StackTraceElement("", "", null, Integer.MIN_VALUE)} 的结果.
         */
        public static final StackTraceElement STACK_TRACE_ELEMENT_SENTINEL =
                new StackTraceElement("", "", null, Integer.MIN_VALUE);

        /**
         * 用于表示不可变堆栈跟踪的串行形式的哨兵(标记).
         */
        public static final StackTraceElement[] STACK_TRACE_SENTINEL =
                new StackTraceElement[]{STACK_TRACE_ELEMENT_SENTINEL};
    }

    /**
     * 空堆栈的共享值.
     */
    private static final StackTraceElement[] UNASSIGNED_STACK = new StackTraceElement[0];

    /*
     * 为了允许 JVM 使 Throwable 对象不可变并安全地重用, 例如: OutOfMemoryErrors,
     * 可响应用户操作而写入的 Throwable 字段, cause, stackTrace 和 suppressExceptions
     * 遵循以下协议:
     *
     * 1) 字段初始化为 non-null 的哨兵(标记)值, 表示逻辑上未设置该值.
     *
     * 2) 向该字段写入 null 表示禁止进一步写入.
     *
     * 3) 哨兵(标记)值可以用另一个非 null 值替换.
     *
     * 例如, HotSpot JVM 的实现具有预分配的 OutOfMemoryError 对象,
     * 以提供对该情况的更好的可诊断性. 创建这些对象时不调用该类的构造函数,
     * 并将有问题的字段初始化为 null. 要支持此功能, 添加到 Throwable 的任何需要初始化
     * 为非空值的新字段都需要协调 JVM 更改.
     */

    /**
     * 导致抛出此 Throwable 的 Throwable 对象, 如果抛出的原因未知,
     * 则抛出此 Throwable 的 Throwable 为 null.
     *
     * @serial
     * @since 1.4
     */
    private Throwable cause = this;

    /**
     * 堆栈跟踪, 作为 {@link #getStackTrace()} 的跟踪.
     * <p>
     * 这个字段初始化为一个长度为 0 的数组.  此字段的 {@code null} 值表示后续调用
     * {@link #setStackTrace(StackTraceElement[])} 和 {@link #fillInStackTrace()}
     * 将是不允许的操作.
     *
     * @serial
     * @since 1.4
     */
    private StackTraceElement[] stackTrace = UNASSIGNED_STACK;

    // 设置此静态字段会在一些 java.util 类上引入可接受的初始化依赖项.
    private static final List<Throwable> SUPPRESSED_SENTINEL =
            Collections.unmodifiableList(new ArrayList<Throwable>(0));

    /**
     * 被抑制的异常列表, 作为 {@link #getSuppressed()} 的返回值.
     * 该列表初始化为零元素不可修改的哨兵(标记)列表. 读入序列化的 Throwable 时,
     * 如果 {@code suppressExceptions} 字段指向零元素列表, 该字段将重置为哨兵(标记)值.
     *
     * @serial
     * @since 1.7
     */
    private List<Throwable> suppressedExceptions = SUPPRESSED_SENTINEL;

    /**
     * 尝试抑制空异常的消息.
     */
    private static final String NULL_CAUSE_MESSAGE = "Cannot suppress a null exception.";

    /**
     * 尝试抑制自己的消息.
     */
    private static final String SELF_SUPPRESSION_MESSAGE = "Self-suppression not permitted";

    /**
     * 用于标记原因异常堆栈跟踪的标题
     */
    private static final String CAUSE_CAPTION = "Caused by: ";

    /**
     * 用于标记抑制异常堆栈跟踪的标题
     */
    private static final String SUPPRESSED_CAPTION = "Suppressed: ";

    /**
     * 构造一个新的 Throwable 使用 {@code null} 作为它的详细信息.
     * 原因未初始化, 可能随后通过调用 {@link #initCause(Throwable)} 进行初始化.
     *
     * <p>调用 {@link #fillInStackTrace()} 方法来初始化新创建的 Throwable 中的堆栈跟踪信息.
     */
    public Throwable() {
        fillInStackTrace();
    }

    /**
     * 构造一个新的 Throwable 使用指定的详细信息.
     * 原因未初始化, 可能随后通过调用 {@link #initCause(Throwable)} 进行初始化.
     *
     * <p>调用 {@link #fillInStackTrace()} 方法来初始化新创建的 Throwable 中的堆栈跟踪信息.
     *
     * @param message 详细消息. 详细消息被保存, 稍候可以通过 {@link #getMessage()} 方法恢复.
     */
    public Throwable(String message) {
        fillInStackTrace();
        detailMessage = message;
    }

    /**
     * 构造一个新的 Throwable 使用指定的信息和原因. <p> 注意, 详细信息与 {@code cause} 关联的
     * <i>不</i>自动合并到此 Throwable 的详细消息中.
     *
     * <p>调用 {@link #fillInStackTrace()} 方法来初始化新创建的 Throwable 中的堆栈跟踪数据.
     *
     * @param message 详细消息. 详细消息被保存, 稍候可以通过 {@link #getMessage()} 方法恢复.
     * @param cause   原因 (将被保存, 稍候可以通过 {@link #getCause()} 方法恢复).
     *                (允许 {@code null} 值, 表示原因不存在或未知.)
     * @since 1.4
     */
    public Throwable(String message, Throwable cause) {
        fillInStackTrace();
        detailMessage = message;
        this.cause = cause;
    }

    /**
     * 构造一个新的 Throwable 使用指定的原因和详细消息
     * ({@code (cause==null ? null : cause.toString())}
     * (通常包含 {@code cause} 的类和详细消息)).
     * 这个构造函数对 throwables 包装其他 throwables 是有用的.
     * (例如, {@link java.security.PrivilegedActionException}).
     *
     * <p>调用 {@link #fillInStackTrace()} 方法来初始化新创建的 Throwable 中的堆栈跟踪数据.
     *
     * @param cause 原因 (将被保存, 稍候可以通过 {@link #getCause()} 方法恢复).
     *              (允许 {@code null} 值, 表示原因不存在或未知.)
     * @since 1.4
     */
    public Throwable(Throwable cause) {
        fillInStackTrace();
        detailMessage = (cause == null ? null : cause.toString());
        this.cause = cause;
    }

    /**
     * 构造一个新的 Throwable 使用指定的详细信息, 原因,
     * {@linkplain #addSuppressed(Throwable) 抑制} 是否启用,
     * 堆栈跟踪是否可写. 如果抑制禁止, 这个对象的 {@link #getSuppressed}
     * 将会返回一个零元素数组, 并且调用 {@link #addSuppressed} 对被抑制列表附加异常将无效.
     * 如果堆栈跟踪不可写, 这个构造函数将不会调用 {@link #fillInStackTrace()},
     * {@code null} 将写入 {@code stackTrace} 字段, 并且随后调用 {@code fillInStackTrace}
     * 和 {@link #setStackTrace(StackTraceElement[])} 将不会设置堆栈跟踪.
     * 如果堆栈跟踪不可写, {@link #getStackTrace}将返回零元素数组.
     *
     * <p> 注意, {@code Throwable} 别的构造函数将抑制视为启用, 将堆栈跟踪设为可写.
     * {@code Throwable} 的子类应该记录禁用抑制的任何条件和记录堆栈跟踪不可写的条件.
     * 禁用抑制仅应在存在特殊要求的特殊情况下发生, 例如虚拟机在低内存情况下重用异常对象.
     * 重新捕获和重新生成给定异常对象的情况, 例如在两个子系统之间实现控制流,
     * 另一种情况, 不可变的 Throwable 对象是合适的.
     *
     * @param message            详细消息.
     * @param cause              原因.  (允许 {@code null} 值, 表示原因不存在或未知.)
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 堆栈跟踪是否可写
     * @see OutOfMemoryError
     * @see NullPointerException
     * @see ArithmeticException
     * @since 1.7
     */
    protected Throwable(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        if (writableStackTrace) {
            fillInStackTrace();
        } else {
            stackTrace = null;
        }
        detailMessage = message;
        this.cause = cause;
        if (!enableSuppression)
            suppressedExceptions = null;
    }

    /**
     * 返回此 Throwable 的详细消息字符串.
     *
     * @return 这个 {@code Throwable} 实例的详细消息字符串(可能为 {@code null}).
     */
    public String getMessage() {
        return detailMessage;
    }

    /**
     * 为这个 Throwable 创建一个本地化描述. 子类可以重写此方法以生成特定于语言环境的消息.
     * 对于不重写此方法的子类, 默认的实现与 {@code getMessage()} 返回相同的结果.
     *
     * @return 这个 Throwable 创建一个本地化描述.
     * @since JDK1.1
     */
    public String getLocalizedMessage() {
        return getMessage();
    }

    /**
     * 返回此 Throwable 的原因, 如果原因不存在或者未知, 则返回 null.
     * (原因是一个 Throwable 导致抛出这个 Throwable.)
     *
     * <p>此实现返回通过需要 {@code Throwable} 的构造函数提供的原因, 或者在创建后
     * 使用 {@link #initCause(Throwable)} 方法设置的原因. 虽然通常不必重写此方法,
     * 但子类可以覆盖它以通过其他方式返回原因集. 这适用在 {@code Throwable} 添加链式异常之前的
     * "遗留链式 Throwable". 请注意, <i>不</i>必须覆盖任何 {@code PrintStackTrace} 方法,
     * 所有这些方法都会调用 {@code getCause} 方法来确定 Throwable 的原因.
     *
     * @return Throwable 的原因, 如果原因不存在或者未知, 则返回 null.
     * @since 1.4
     */
    public synchronized Throwable getCause() {
        return (cause == this ? null : cause);
    }

    /**
     * 使用指定的 <i>cause</i> 初始化这个 Throwable 的原因. (原因是一个 Throwable 导致抛出这个 Throwable.)
     *
     * <p>此方法最多调用一次. 它通常在构造函数内调用, 或者在创建 Throwable 之后立即调用. 如果是使用 {@link #Throwable(Throwable)}
     * 或者 {@link #Throwable(String, Throwable)}, 则此方法甚至不能被调用一次. wttch: 在此方法内会做一些判断
     *
     * <p>在没有其他支持来设置原因的情况下在旧的 Throwable 类型上使用此方法的示例是:
     *
     * <pre>
     * try {
     *     lowLevelOp();
     * } catch (LowLevelException le) {
     *     throw (HighLevelException)
     *           new HighLevelException().initCause(le); // Legacy constructor
     * }
     * </pre>
     *
     * @param cause 原因 (保存以供以后 {@link #getCause()} 方法检索). (允许 {@code null} 值,
     *              表示原因不存在或者未知.)
     * @return 对此 {@code Throwable} 实例的引用.
     * @throws IllegalArgumentException 如果 {@code cause} 是这个 Throwable. ( Throwable 不可能是它自己的原因.)
     * @throws IllegalStateException    如果这个 Throwable 是通过 {@link #Throwable(Throwable)} 或者
     *                                  {@link #Throwable(String, Throwable)} 构造函数创建的,
     *                                  或者这个方法已经被调用过了.
     * @since 1.4
     */
    public synchronized Throwable initCause(Throwable cause) {
        if (this.cause != this)
            // wttch: 不能覆盖原因,默认的 cause 是 this
            throw new IllegalStateException("Can't overwrite cause with " +
                    Objects.toString(cause, "a null"), this);
        if (cause == this)
            // wttch: 不能将自己添加到导致自己的原因去
            throw new IllegalArgumentException("Self-causation not permitted", this);
        this.cause = cause;
        return this;
    }

    /**
     * 返回这个 Throwable 的简单描述. 结果是连接以下信息:
     * <ul>
     * <li> 这个对象的类的 {@linkplain Class#getName() 名字}
     * <li> ": " (一个冒号和一个空格)
     * <li> 调用这个对象 {@link #getLocalizedMessage} 方法返回的结果
     * </ul>
     * 如果 {@code getLocalizedMessage} 返回 {@code null}, 则仅返回类名.
     *
     * @return 这个 Throwable 的字符串描述.
     */
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + ": " + message) : s;
    }

    /**
     * 将此 Throwable 及其 backtrace 打印到标准错误流. 此方法在错误输出流上打印
     * 此 {@code Throwable} 对象的堆栈跟踪, 该错误输出流默认是 {@code System.err} 字段的值.
     * 第一行输出包含此对象的 {@link #toString()} 方法的结果. 剩余行表示先前由方法 {@link #fillInStackTrace()}
     * 记录的数据. 此信息的格式取决于实现, 但以下示例可被视为典型:
     * <blockquote><pre>
     * java.lang.NullPointerException
     *         at MyClass.mash(MyClass.java:9)
     *         at MyClass.crunch(MyClass.java:6)
     *         at MyClass.main(MyClass.java:3)
     * </pre></blockquote>
     * 这个例子是通过运行以下程序产生的:
     * <pre>
     * class MyClass {
     *     public static void main(String[] args) {
     *         crunch(null);
     *     }
     *     static void crunch(int[] a) {
     *         mash(a);
     *     }
     *     static void mash(int[] b) {
     *         System.out.println(b[0]);
     *     }
     * }
     * </pre>
     * 具有初始化非 null 原因的 Throwable 的 backtrace, 通常包含原因的 backtrace.
     * 此信息的格式取决于实现, 但以下示例可被视为典型:
     * <pre>
     * HighLevelException: MidLevelException: LowLevelException
     *         at Junk.a(Junk.java:13)
     *         at Junk.main(Junk.java:4)
     * Caused by: MidLevelException: LowLevelException
     *         at Junk.c(Junk.java:23)
     *         at Junk.b(Junk.java:17)
     *         at Junk.a(Junk.java:11)
     *         ... 1 more
     * Caused by: LowLevelException
     *         at Junk.e(Junk.java:30)
     *         at Junk.d(Junk.java:27)
     *         at Junk.c(Junk.java:21)
     *         ... 3 more
     * </pre>
     * 请注意包含字符 {@code "..."} 的行的存在. 这些行表示此异常的堆栈跟踪的剩余部分
     * 匹配由此异常("包含"异常)引起的异常的跟踪堆栈底部的指示帧数. 这种简写可以大大减少
     * 输出的长度, 在这种情况下, 从捕获"致使异常"的同一方法抛出包装异常.
     * wttch: ... n more 紧挨的异常行就是 try catch 块中出异常的地方,
     * ... n more 表示这个出现异常的地方调用了其他函数.
     * 实际上是部分异常在此之前已经打印了, 为了节省资源防止重复打印.
     * 像例子中 try catch 语句块, 新的包装 Throwable 抛出之前, 其实大部分 Throwable 的跟踪
     * 是重复的, 像下列例子 c() 和 c() 后面的 e.printStackTrace(), 除了这个位置的跟踪不一样,
     * 向上的 stackTrace 都是一样的.
     * 上面的例子通过以下程序产生:
     * <pre>
     * public class Junk {
     *     public static void main(String args[]) {
     *         try {
     *             a();
     *         } catch(HighLevelException e) {
     *             e.printStackTrace();
     *         }
     *     }
     *     static void a() throws HighLevelException {
     *         try {
     *             b();
     *         } catch(MidLevelException e) {
     *             throw new HighLevelException(e);
     *         }
     *     }
     *     static void b() throws MidLevelException {
     *         c();
     *     }
     *     static void c() throws MidLevelException {
     *         try {
     *             d();
     *         } catch(LowLevelException e) {
     *             throw new MidLevelException(e);
     *         }
     *     }
     *     static void d() throws LowLevelException {
     *        e();
     *     }
     *     static void e() throws LowLevelException {
     *         throw new LowLevelException();
     *     }
     * }
     *
     * class HighLevelException extends Exception {
     *     HighLevelException(Throwable cause) { super(cause); }
     * }
     *
     * class MidLevelException extends Exception {
     *     MidLevelException(Throwable cause)  { super(cause); }
     * }
     *
     * class LowLevelException extends Exception {
     * }
     * </pre>
     * 从版本 7 开始, 该平台支持<i>抑制异常</i>的概念(与 {@code try}-with-resources 声明一起).
     * 在堆栈跟踪下方打印出为了传递异常而被抑制的任何异常. 此信息的格式取决于实现,
     * 但以下示例可能被视为典型:
     *
     * <pre>
     * Exception in thread "main" java.lang.Exception: Something happened
     *  at Foo.bar(Foo.java:10)
     *  at Foo.main(Foo.java:5)
     *  Suppressed: Resource$CloseFailException: Resource ID = 0
     *          at Resource.close(Resource.java:26)
     *          at Foo.bar(Foo.java:9)
     *          ... 1 more
     * </pre>
     * 请注意, "... n more" 字符用于抑制异常, 仅用于原因. 与原因不同, 被抑制的异常会缩进
     * 超出其 "包含异常" 的范围.
     *
     * <p>异常可以同时具有原因和一个或多个抑制异常:
     * <pre>
     * Exception in thread "main" java.lang.Exception: Main block
     *  at Foo3.main(Foo3.java:7)
     *  Suppressed: Resource$CloseFailException: Resource ID = 2
     *          at Resource.close(Resource.java:26)
     *          at Foo3.main(Foo3.java:5)
     *  Suppressed: Resource$CloseFailException: Resource ID = 1
     *          at Resource.close(Resource.java:26)
     *          at Foo3.main(Foo3.java:5)
     * Caused by: java.lang.Exception: I did it
     *  at Foo3.main(Foo3.java:8)
     * </pre>
     * 同样, 被抑制的异常可能有原因:
     * <pre>
     * Exception in thread "main" java.lang.Exception: Main block
     *  at Foo4.main(Foo4.java:6)
     *  Suppressed: Resource2$CloseFailException: Resource ID = 1
     *          at Resource2.close(Resource2.java:20)
     *          at Foo4.main(Foo4.java:5)
     *  Caused by: java.lang.Exception: Rats, you caught me
     *          at Resource2$CloseFailException.&lt;init&gt;(Resource2.java:45)
     *          ... 2 more
     * </pre>
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * 使用指定的流打印这个 Throwable 和它的 backtrace.
     *
     * @param s 输出流使用的 {@code PrintStream}
     */
    public void printStackTrace(PrintStream s) {
        printStackTrace(new WrappedPrintStream(s));
    }

    private void printStackTrace(PrintStreamOrWriter s) {
        // 通过使用具有标示等同语义的 Set 来防止 Throwable.equals 的恶意覆盖.
        Set<Throwable> dejaVu =
                Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>());
        dejaVu.add(this);

        synchronized (s.lock()) {
            // 打印我们的堆栈跟踪
            s.println(this);
            StackTraceElement[] trace = getOurStackTrace();
            // 打印自身的堆栈跟踪
            for (StackTraceElement traceElement : trace)
                s.println("\tat " + traceElement);

            // 打印抑制的异常(如果有)
            for (Throwable se : getSuppressed())
                se.printEnclosedStackTrace(s, trace, SUPPRESSED_CAPTION, "\t", dejaVu);

            // 打印原因, 如果有的话
            Throwable ourCause = getCause();
            if (ourCause != null)
                ourCause.printEnclosedStackTrace(s, trace, CAUSE_CAPTION, "", dejaVu);
        }
    }

    /**
     * 打印我们的堆栈跟踪作为指定堆栈跟踪的包含 Throwable.
     *
     * @param s              打印使用的 {@code PrintStreamOrWriter}
     * @param enclosingTrace 要打印的所包含的 Throwable
     * @param caption        题目
     * @param prefix         前缀
     * @param dejaVu         已经打印的 Throwable 集合
     */
    private void printEnclosedStackTrace(PrintStreamOrWriter s,
                                         StackTraceElement[] enclosingTrace,
                                         String caption,
                                         String prefix,
                                         Set<Throwable> dejaVu) {
        // 检查是否拥有锁
        assert Thread.holdsLock(s.lock());
        if (dejaVu.contains(this)) {
            // 重复的 Throwable 引用
            s.println("\t[CIRCULAR REFERENCE:" + this + "]");
        } else {
            dejaVu.add(this);
            // 计算此 stackTrace 和 enclosingTrace 之间共同的帧数
            StackTraceElement[] trace = getOurStackTrace();
            int m = trace.length - 1;
            int n = enclosingTrace.length - 1;
            // 只有最深处(最外层的函数调用)才可能产生同样的跟踪帧
            while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
                m--;
                n--;
            }
            int framesInCommon = trace.length - 1 - m;

            // 打印我们的堆栈跟踪: 前缀 题目 this
            s.println(prefix + caption + this);
            // 打印非同样的跟踪帧
            for (int i = 0; i <= m; i++)
                s.println(prefix + "\tat " + trace[i]);
            // 相同的跟踪帧数
            if (framesInCommon != 0)
                s.println(prefix + "\t... " + framesInCommon + " more");

            // 如果存在任何的抑制的 Throwable, 打印它们
            for (Throwable se : getSuppressed())
                se.printEnclosedStackTrace(s, trace, SUPPRESSED_CAPTION,
                        prefix + "\t", dejaVu);

            // 打印原因, 如果有的话
            Throwable ourCause = getCause();
            if (ourCause != null)
                ourCause.printEnclosedStackTrace(s, trace, CAUSE_CAPTION, prefix, dejaVu);
        }
    }

    /**
     * 将此 throwable 及其 backtrace 打印到指定的 print writer.
     *
     * @param s 输出使用的 {@code PrintWriter}
     * @since JDK1.1
     */
    public void printStackTrace(PrintWriter s) {
        printStackTrace(new WrappedPrintWriter(s));
    }

    /**
     * PrintStream 和 PrintWriter 的包装类, 用于实现 printStackTrace 的单个实现.
     */
    private abstract static class PrintStreamOrWriter {
        /**
         * 使用此 StreamOrWriter 时返回要锁定的对象.
         */
        abstract Object lock();

        /**
         * 在此 StreamOrWriter 上将指定的字符串打印为一行.
         */
        abstract void println(Object o);
    }

    /**
     * 使用 PrintStream 构造 PrintStreamOrWriter
     */
    private static class WrappedPrintStream extends PrintStreamOrWriter {
        private final PrintStream printStream;

        WrappedPrintStream(PrintStream printStream) {
            this.printStream = printStream;
        }

        Object lock() {
            return printStream;
        }

        void println(Object o) {
            printStream.println(o);
        }
    }

    /**
     * 使用 PrintWriter 构造 PrintStreamOrWriter
     */
    private static class WrappedPrintWriter extends PrintStreamOrWriter {
        private final PrintWriter printWriter;

        WrappedPrintWriter(PrintWriter printWriter) {
            this.printWriter = printWriter;
        }

        Object lock() {
            return printWriter;
        }

        void println(Object o) {
            printWriter.println(o);
        }
    }

    /**
     * 填写执行堆栈跟踪. 此方法在此 {@code Throwable} 对象中记录
     * 有关当前线程的堆栈帧的当前状态的信息.
     *
     * <p>如果此 {@code Throwable} {@linkplain
     * Throwable#Throwable(String, Throwable, boolean, boolean) 不可写},
     * 调用这个方法没有影响.
     *
     * @return 对此 {@code Throwable} 实例的引用.
     * @see java.lang.Throwable#printStackTrace()
     */
    public synchronized Throwable fillInStackTrace() {
        if (stackTrace != null ||
                backtrace != null /* 超出协议状态 */) {
            fillInStackTrace(0);
            stackTrace = UNASSIGNED_STACK;
        }
        return this;
    }

    private native Throwable fillInStackTrace(int dummy);

    /**
     * 提供对打印的堆栈跟踪信息的编程访问 {@link #printStackTrace()} 时会调用.
     * 返回堆栈跟踪元素的数组, 每个代表一个堆栈帧. 数组的第 0 个元素(假设数组长度不为 0)
     * 表示栈顶, 这是序列中的最后一个方法调用. 通常情况下, 这就是这个 Throwable 被被创建和抛出的点.
     * 数组的最后一个元素(假设数组长度不为 0)表示堆栈的底部, 这是第一个方法调用.
     *
     * <p> 在某些情况下, 某些虚拟机可能会省略一个虚拟机堆栈跟踪中的更多的堆栈帧. 在极端情况下,
     * 没有堆栈跟踪信息的虚拟机允许此 Throwable 从此返回 0 长度数组方法. 一般来说, 这个方法返回的数组会
     * 为每个要打印 {@code printStackTrace} 的帧包含一个元素. 写入返回的数组不会影响将来调用此方法.
     *
     * @return 一个堆栈跟踪元素的数组, 表示与此 Throwable 相关的堆栈跟踪.
     * @since 1.4
     */
    public StackTraceElement[] getStackTrace() {
        return getOurStackTrace().clone();
    }

    private synchronized StackTraceElement[] getOurStackTrace() {
        // 如果这是对此方法的第一次调用, 则使用来自 backtrace 的信息初始化堆栈跟踪字段.
        // 空堆栈 || ( 空stackTrace && 非空 backtrace)
        if (stackTrace == UNASSIGNED_STACK ||
                (stackTrace == null && backtrace != null) /* 超出协议状态 */) {
            // 堆栈深度, 如果无法跟踪堆栈, 则为 0
            int depth = getStackTraceDepth();
            stackTrace = new StackTraceElement[depth];
            for (int i = 0; i < depth; i++)
                stackTrace[i] = getStackTraceElement(i);
        } else if (stackTrace == null) {
            return UNASSIGNED_STACK;
        }
        return stackTrace;
    }

    /**
     * Sets the stack trace elements that will be returned by
     * {@link #getStackTrace()} and printed by {@link #printStackTrace()}
     * and related methods.
     * <p>
     * This method, which is designed for use by RPC frameworks and other
     * advanced systems, allows the client to override the default
     * stack trace that is either generated by {@link #fillInStackTrace()}
     * when a throwable is constructed or deserialized when a throwable is
     * read from a serialization stream.
     *
     * <p>If the stack trace of this {@code Throwable} {@linkplain
     * Throwable#Throwable(String, Throwable, boolean, boolean) is not
     * writable}, calling this method has no effect other than
     * validating its argument.
     *
     * @param stackTrace the stack trace elements to be associated with
     *                   this {@code Throwable}.  The specified array is copied by this
     *                   call; changes in the specified array after the method invocation
     *                   returns will have no affect on this {@code Throwable}'s stack
     *                   trace.
     * @throws NullPointerException if {@code stackTrace} is
     *                              {@code null} or if any of the elements of
     *                              {@code stackTrace} are {@code null}
     * @since 1.4
     */
    public void setStackTrace(StackTraceElement[] stackTrace) {
        // Validate argument
        StackTraceElement[] defensiveCopy = stackTrace.clone();
        for (int i = 0; i < defensiveCopy.length; i++) {
            if (defensiveCopy[i] == null)
                throw new NullPointerException("stackTrace[" + i + "]");
        }

        synchronized (this) {
            if (this.stackTrace == null && // Immutable stack
                    backtrace == null) // Test for out of protocol state
                return;
            this.stackTrace = defensiveCopy;
        }
    }

    /**
     * 返回堆栈跟踪中的元素数(如果堆栈跟踪不可用, 则返回 0).
     * <p>
     * 包保护, 供 SharedSecrets 使用.
     */
    native int getStackTraceDepth();

    /**
     * 返回堆栈跟踪的指定元素.
     * <p>
     * 包保护, 供 SharedSecrets 使用.
     *
     * @param index 返回的元素的索引.
     * @throws IndexOutOfBoundsException 如果 {@code index < 0 || index >= getStackTraceDepth() }
     */
    native StackTraceElement getStackTraceElement(int index);

    /**
     * Reads a {@code Throwable} from a stream, enforcing
     * well-formedness constraints on fields.  Null entries and
     * self-pointers are not allowed in the list of {@code
     * suppressedExceptions}.  Null entries are not allowed for stack
     * trace elements.  A null stack trace in the serial form results
     * in a zero-length stack element array. A single-element stack
     * trace whose entry is equal to {@code new StackTraceElement("",
     * "", null, Integer.MIN_VALUE)} results in a {@code null} {@code
     * stackTrace} field.
     * <p>
     * Note that there are no constraints on the value the {@code
     * cause} field can hold; both {@code null} and {@code this} are
     * valid values for the field.
     */
    private void readObject(ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();     // read in all fields
        if (suppressedExceptions != null) {
            List<Throwable> suppressed = null;
            if (suppressedExceptions.isEmpty()) {
                // Use the sentinel for a zero-length list
                suppressed = SUPPRESSED_SENTINEL;
            } else { // Copy Throwables to new list
                suppressed = new ArrayList<>(1);
                for (Throwable t : suppressedExceptions) {
                    // Enforce constraints on suppressed exceptions in
                    // case of corrupt or malicious stream.
                    if (t == null)
                        throw new NullPointerException(NULL_CAUSE_MESSAGE);
                    if (t == this)
                        throw new IllegalArgumentException(SELF_SUPPRESSION_MESSAGE);
                    suppressed.add(t);
                }
            }
            suppressedExceptions = suppressed;
        } // else a null suppressedExceptions field remains null

        /*
         * For zero-length stack traces, use a clone of
         * UNASSIGNED_STACK rather than UNASSIGNED_STACK itself to
         * allow identity comparison against UNASSIGNED_STACK in
         * getOurStackTrace.  The identity of UNASSIGNED_STACK in
         * stackTrace indicates to the getOurStackTrace method that
         * the stackTrace needs to be constructed from the information
         * in backtrace.
         */
        if (stackTrace != null) {
            if (stackTrace.length == 0) {
                stackTrace = UNASSIGNED_STACK.clone();
            } else if (stackTrace.length == 1 &&
                    // Check for the marker of an immutable stack trace
                    SentinelHolder.STACK_TRACE_ELEMENT_SENTINEL.equals(stackTrace[0])) {
                stackTrace = null;
            } else { // Verify stack trace elements are non-null.
                for (StackTraceElement ste : stackTrace) {
                    if (ste == null)
                        throw new NullPointerException("null StackTraceElement in serial stream. ");
                }
            }
        } else {
            // A null stackTrace field in the serial form can result
            // from an exception serialized without that field in
            // older JDK releases; treat such exceptions as having
            // empty stack traces.
            stackTrace = UNASSIGNED_STACK.clone();
        }
    }

    /**
     * Write a {@code Throwable} object to a stream.
     * <p>
     * A {@code null} stack trace field is represented in the serial
     * form as a one-element array whose element is equal to {@code
     * new StackTraceElement("", "", null, Integer.MIN_VALUE)}.
     */
    private synchronized void writeObject(ObjectOutputStream s)
            throws IOException {
        // Ensure that the stackTrace field is initialized to a
        // non-null value, if appropriate.  As of JDK 7, a null stack
        // trace field is a valid value indicating the stack trace
        // should not be set.
        getOurStackTrace();

        StackTraceElement[] oldStackTrace = stackTrace;
        try {
            if (stackTrace == null)
                stackTrace = SentinelHolder.STACK_TRACE_SENTINEL;
            s.defaultWriteObject();
        } finally {
            stackTrace = oldStackTrace;
        }
    }

    /**
     * Appends the specified exception to the exceptions that were
     * suppressed in order to deliver this exception. This method is
     * thread-safe and typically called (automatically and implicitly)
     * by the {@code try}-with-resources statement.
     *
     * <p>The suppression behavior is enabled <em>unless</em> disabled
     * {@linkplain #Throwable(String, Throwable, boolean, boolean) via
     * a constructor}.  When suppression is disabled, this method does
     * nothing other than to validate its argument.
     *
     * <p>Note that when one exception {@linkplain
     * #initCause(Throwable) causes} another exception, the first
     * exception is usually caught and then the second exception is
     * thrown in response.  In other words, there is a causal
     * connection between the two exceptions.
     * <p>
     * In contrast, there are situations where two independent
     * exceptions can be thrown in sibling code blocks, in particular
     * in the {@code try} block of a {@code try}-with-resources
     * statement and the compiler-generated {@code finally} block
     * which closes the resource.
     * <p>
     * In these situations, only one of the thrown exceptions can be
     * propagated.  In the {@code try}-with-resources statement, when
     * there are two such exceptions, the exception originating from
     * the {@code try} block is propagated and the exception from the
     * {@code finally} block is added to the list of exceptions
     * suppressed by the exception from the {@code try} block.  As an
     * exception unwinds the stack, it can accumulate multiple
     * suppressed exceptions.
     *
     * <p>An exception may have suppressed exceptions while also being
     * caused by another exception.  Whether or not an exception has a
     * cause is semantically known at the time of its creation, unlike
     * whether or not an exception will suppress other exceptions
     * which is typically only determined after an exception is
     * thrown.
     *
     * <p>Note that programmer written code is also able to take
     * advantage of calling this method in situations where there are
     * multiple sibling exceptions and only one can be propagated.
     *
     * @param exception the exception to be added to the list of
     *                  suppressed exceptions
     * @throws IllegalArgumentException if {@code exception} is this
     *                                  throwable; a throwable cannot suppress itself.
     * @throws NullPointerException     if {@code exception} is {@code null}
     * @since 1.7
     */
    public final synchronized void addSuppressed(Throwable exception) {
        if (exception == this)
            throw new IllegalArgumentException(SELF_SUPPRESSION_MESSAGE, exception);

        if (exception == null)
            throw new NullPointerException(NULL_CAUSE_MESSAGE);

        if (suppressedExceptions == null) // Suppressed exceptions not recorded
            return;

        if (suppressedExceptions == SUPPRESSED_SENTINEL)
            suppressedExceptions = new ArrayList<>(1);

        suppressedExceptions.add(exception);
    }

    private static final Throwable[] EMPTY_THROWABLE_ARRAY = new Throwable[0];

    /**
     * 返回一个数组, 其中包含通常由 {@code try}-with-resources 语句抑制的所有异常, 以便传递此异常.
     * <p>
     * 如果没有禁止异或 {@linkplain #Throwable(String, Throwable, boolean, boolean) 禁用异常抑制},
     * 则返回一个空数组. 此方法是线程安全的. 写入返回的数组不会影响将来对此方法的调用.
     *
     * @return 包含为了传递此异常而被抑制的所有异常的数组.
     * @since 1.7
     */
    public final synchronized Throwable[] getSuppressed() {
        if (suppressedExceptions == SUPPRESSED_SENTINEL ||
                suppressedExceptions == null)
            return EMPTY_THROWABLE_ARRAY;
        else
            return suppressedExceptions.toArray(EMPTY_THROWABLE_ARRAY);
    }
}
