package java.lang.instrument;

/**
 * 此类充当 <code>Instrumentation.redefineClasses</code> 方法的参数快.
 * 用于绑定需要重新定义的 <code>Class</code> 的新的类文件字节码.
 * <p>
 * Wttch:
 * 在类字节码文件层次完成类似 AOP 的功能.
 *
 * @see java.lang.instrument.Instrumentation#redefineClasses
 * @since 1.5
 */
public final class ClassDefinition {
    /**
     * 需要重新定义的类
     */
    private final Class<?> mClass;

    /**
     * 要更换的字节码
     */
    private final byte[] mClassFile;

    /**
     * 创建一个 <code>ClassDefinition</code> 使用提供的类和类文件字节进行绑定.
     * 不复制提供的缓冲区, 只捕获对它的引用.
     *
     * @param theClass     需要重新定义的 <code>Class</code>
     * @param theClassFile 新的字节码数组
     * @throws java.lang.NullPointerException 如果类或者字节数组为 <code>null</code>.
     */
    public ClassDefinition(Class<?> theClass,
                           byte[] theClassFile) {
        if (theClass == null || theClassFile == null) {
            throw new NullPointerException();
        }
        mClass = theClass;
        mClassFile = theClassFile;
    }

    /**
     * Returns the class.
     *
     * @return the <code>Class</code> object referred to.
     */
    public Class<?>
    getDefinitionClass() {
        return mClass;
    }

    /**
     * Returns the array of bytes that contains the new class file.
     *
     * @return the class file bytes.
     */
    public byte[]
    getDefinitionClassFile() {
        return mClassFile;
    }
}
