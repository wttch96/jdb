package java.lang;

/**
 * 断言状态指令的集合(例如, "在包p中启用断言" 或 "在类c中禁用断言"). JVM 使用此类来
 * 实现 <tt>java</tt> 命令行标志中隐含断言状态的指令 <tt>-enableassertions</tt>(<tt>-ea</tt>)
 * 和 <tt>-disableassertions</tt>(<tt>-da</tt>).
 *
 * @author Josh Bloch
 * @since 1.4
 */
class AssertionStatusDirectives {
    /**
     * 要启用或禁用断言的类. 此数组中的字符串是完全限定的类名 (例如, "com.xyz.foo.Bar").
     */
    String[] classes;

    /**
     * <tt>classes</tt> 的并行数组, 指示每个类是否启用或禁用断言. <tt>classEnable[i]</tt> 的值为
     * <tt>true</tt> 表示由 <tt>classes[i]</tt> 命名的类应该启用断言; <tt>false</tt> 表示应禁用类.
     * 此数组必须具有和 <tt>classes</tt> 相同的数量.
     *
     * <p>对于同一个类的冲突指令, 给定类的最后一个指定获胜. 换句话说, 如果字符串<tt>s</tt> 在 <tt>classes</tt>
     * 数组中出现多次, 而 <tt>i</tt> 是 <tt>classes[i].equals(s)</tt> 的最高整数, 然后 <tt>classEnabled[i]</tt>
     * 指示是否要在类 <tt>s</tt> 中启用断言.
     */
    boolean[] classEnabled;

    /**
     * 要启用或禁用断言的 package-tree. 此数组中的字符串是竞争或部分包名称(例如, "com.xyz" 或 "com.xyz.foo").
     */
    String[] packages;

    /**
     * <tt>packages</tt> 的并行数组, 指示每个 package-tree 是启用还是禁用断言.
     * <tt>packageEnabled[i]</tt> 的值 <tt>true</tt> 表示由 <tt>packages[i]</tt>
     * 命名的 package-tree 应该启用断言; 值 <tt>false</tt> 表示它应该禁用断言. 此数组必须与
     * <tt>packages</tt>具有相同数量的元素.
     * <p>
     * 在针对同一 package-tree 的冲突指令的情况下, 给定包树的最后一个指令获胜.
     * 换句话说, 如果字符串 <tt>s</tt> 在 <tt>packages</tt>数组中出现多次,
     * 而 <tt>i</tt> 是 <tt>packages[i].equals(s)</tt> 的最高整数,
     * 然后 <tt>packageEnabled[i]</tt> 指示是否要在 package-tree <tt>s</tt>中启用断言.
     */
    boolean[] packageEnabled;

    /**
     * 是否默认启用非系统类中的断言.
     */
    boolean deflt;
}
