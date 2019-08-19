package java.lang.reflect;

import sun.reflect.CallerSensitive;
import sun.reflect.ConstructorAccessor;
import sun.reflect.Reflection;
import sun.reflect.annotation.TypeAnnotation;
import sun.reflect.annotation.TypeAnnotationParser;
import sun.reflect.generics.repository.ConstructorRepository;
import sun.reflect.generics.factory.CoreReflectionFactory;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.scope.ConstructorScope;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;

/**
 * {@code Constructor} 提供有关类的单个构造函数的信息和访问权限.
 *
 * <p>{@code Constructor} 允许在将实际参数与 newInstance() 与底层构造函数的形参匹配时扩展转换,
 * 但如果发生缩小转换则抛出 {@code IllegalArgumentException}.
 *
 * @param <T> the class in which the constructor is declared
 * @author Kenneth Russell
 * @author Nakul Saraiya
 * @see Member
 * @see java.lang.Class
 * @see java.lang.Class#getConstructors()
 * @see java.lang.Class#getConstructor(Class[])
 * @see java.lang.Class#getDeclaredConstructors()
 */
public final class Constructor<T> extends Executable {
    private Class<T> clazz;
    /**
     * Wttch: 利用反射访问到这个私有变量发现这似乎是构造函数在定义时的排序位置 (以 0 起始).
     */
    private int slot;
    private Class<?>[] parameterTypes;
    private Class<?>[] exceptionTypes;
    private int modifiers;
    // 泛型和注解支持
    // Wttch: 通过反射查看, 这个字段是一个类似函数签名的字符串形式
    private transient String signature;
    // 泛型信息库; 延迟初始化
    private transient ConstructorRepository genericInfo;
    private byte[] annotations;
    private byte[] parameterAnnotations;

    // 泛型的基础信息
    // 工厂的访问器
    private GenericsFactory getFactory() {
        // 创造范围和工厂
        return CoreReflectionFactory.make(this, ConstructorScope.make(this));
    }

    // 泛型信息库的访问器
    @Override
    ConstructorRepository getGenericInfo() {
        // 如有必要, 延迟的初始化存储库
        if (genericInfo == null) {
            // 创建和缓存泛型信息库
            genericInfo =
                    ConstructorRepository.make(getSignature(),
                            getFactory());
        }
        return genericInfo; //返回缓存仓库
    }

    private volatile ConstructorAccessor constructorAccessor;
    // 为了共享构造函数访问器. 这个分支结构目前只有两个层次.
    // (即, 一个根构造函数和可能有许多指向它的构造函数对象.)
    //
    // 如果此分支结构包含循环, 则注解代码中可能会发生死锁.
    private Constructor<T> root;

    /**
     * 由 Executable 用于注解共享.
     */
    @Override
    Executable getRoot() {
        return root;
    }

    /**
     * ReflectAccess 使用包私有的构造函数, 通过 sun.reflect.LangReflectAccess 从
     * java.lang 包中实现 Java 代码中这些对象的实例化.
     */
    Constructor(Class<T> declaringClass,
                Class<?>[] parameterTypes,
                Class<?>[] checkedExceptions,
                int modifiers,
                int slot,
                String signature,
                byte[] annotations,
                byte[] parameterAnnotations) {
        this.clazz = declaringClass;
        this.parameterTypes = parameterTypes;
        this.exceptionTypes = checkedExceptions;
        this.modifiers = modifiers;
        this.slot = slot;
        this.signature = signature;
        this.annotations = annotations;
        this.parameterAnnotations = parameterAnnotations;
    }

    /**
     * 包私有惯例(通过 ReflectAccess 暴露给 java.lang.Class), 它返回此 Constructor 的副本.
     * 副本的 "root" 字段指向此构造函数.
     */
    Constructor<T> copy() {
        // 此惯例允许在 Constructor 对象之间共享 ConstructorAccessor 对象,
        // 这些对象引用 VM 中的相同底层方法. (由于 AccessibleObject 中的　"accessibility" 位,
        // 所有的这些扭曲都是必须的, 其中隐含的要求为 Class 对象的每个反射调用而编写新的
        // java.lang.reflect 对象.)
        // Wttch: getRoot 方法存在的原因为了共享 ConstructorAccessor.
        if (this.root != null)
            throw new IllegalArgumentException("Can not copy a non-root Constructor");

        Constructor<T> res = new Constructor<>(clazz,
                parameterTypes,
                exceptionTypes, modifiers, slot,
                signature,
                annotations,
                parameterAnnotations);
        res.root = this;
        // 如果已经存在, 也可能急切地宣传这个
        res.constructorAccessor = constructorAccessor;
        return res;
    }

    /**
     * 存在签名信息, 就表示存在泛型信息
     */
    @Override
    boolean hasGenericInformation() {
        return (getSignature() != null);
    }

    @Override
    byte[] getAnnotationBytes() {
        return annotations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getDeclaringClass() {
        return clazz;
    }

    /**
     * 以字符串形式返回此构造函数的名称. 这是构造函数声明类的二进制名称.
     */
    @Override
    public String getName() {
        return getDeclaringClass().getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getModifiers() {
        return modifiers;
    }

    /**
     * {@inheritDoc}
     *
     * @throws GenericSignatureFormatError {@inheritDoc}
     * @since 1.5
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public TypeVariable<Constructor<T>>[] getTypeParameters() {
        if (getSignature() != null) {
            return (TypeVariable<Constructor<T>>[]) getGenericInfo().getTypeParameters();
        } else
            return (TypeVariable<Constructor<T>>[]) new TypeVariable[0];
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes.clone();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.8
     */
    public int getParameterCount() {
        return parameterTypes.length;
    }

    /**
     * {@inheritDoc}
     *
     * @throws GenericSignatureFormatError         {@inheritDoc}
     * @throws TypeNotPresentException             {@inheritDoc}
     * @throws MalformedParameterizedTypeException {@inheritDoc}
     * @since 1.5
     */
    @Override
    public Type[] getGenericParameterTypes() {
        return super.getGenericParameterTypes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?>[] getExceptionTypes() {
        return exceptionTypes.clone();
    }


    /**
     * {@inheritDoc}
     *
     * @throws GenericSignatureFormatError         {@inheritDoc}
     * @throws TypeNotPresentException             {@inheritDoc}
     * @throws MalformedParameterizedTypeException {@inheritDoc}
     * @since 1.5
     */
    @Override
    public Type[] getGenericExceptionTypes() {
        return super.getGenericExceptionTypes();
    }

    /**
     * 将此 {@code Constructor} 和指定的对象进行比较. 如果对象相同则返回 true.
     * 如果两个 {@code Constructor} 对象由同一个类声明并且具有相同的形参类型, 则他们是相同的.
     */
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Constructor) {
            Constructor<?> other = (Constructor<?>) obj;
            if (getDeclaringClass() == other.getDeclaringClass()) {
                return equalParamTypes(parameterTypes, other.parameterTypes);
            }
        }
        return false;
    }

    /**
     * 返回此 {@code Constructor} 的哈希码. 哈希码与声明类名的底层构造函数的哈希码相同.
     */
    public int hashCode() {
        return getDeclaringClass().getName().hashCode();
    }

    /**
     * 返回描述此 {@code Constructor} 的字符串. 该字符串被格式化为构造函数访问修饰符(如果有),
     * 后跟声明类的王权限定名称, 后跟构造函数的形式参数类型的带括号的逗号分割列表. 例如:
     * <pre>
     *    public java.util.Hashtable(int,float)
     * </pre>
     *
     * <p>构造函数唯一可能的修饰符是访问修饰符 {@code public}, {@code protected} 或 {@code private}.
     * 如果构造函数具有默认(包)访问权限, 则只能显示其中一个, 或者不显示.
     *
     * @return 描述此 {@code Constructor} 的字符串.
     * @jls 8.8.3. Constructor Modifiers
     */
    public String toString() {
        return sharedToString(Modifier.constructorModifiers(),
                false,
                parameterTypes,
                exceptionTypes);
    }

    @Override
    void specificToStringHeader(StringBuilder sb) {
        sb.append(getDeclaringClass().getTypeName());
    }

    /**
     * 返回描述此 {@code Constructor} 的字符串, 包括类型参数. 字符串被格式化为构造函数访问修饰符(如果有),
     * 后跟一个以尖括号括起来的逗号分割的构造函数参数类型列表(如果有), 后跟声明类的完全限定名称, 后跟带括号的
     * 逗号分隔的构造函数泛型类型的参数类型列表.
     * <p>
     * 如果声明此构造函数采用可变数量的参数, 而不是将最后一个参数表示为
     * "<tt><i>Type</i>[]</tt>", 则将其表示为 "<tt><i>Type</i>...</tt>".
     * <p>
     * 空格用于将访问修饰符彼此分开, 并与类型参数或返回类型分开. 如果没有类型参数, 则省略类型参数列表;
     * 如果存在类型参数列表, 则空格将从类名称中分离列表. 如果声明构造函数抛出异常, 则参数列表后跟一个空格,
     * 后跟单词 "{@code throws}", 后跟逗号分割的抛出异常类型列表.
     *
     * <p>构造函数唯一可能的修饰符是访问修饰符 {@code public}, {@code protected} 或 {@code private}.
     * 如果构造函数具有默认(包)访问权限, 则只能显示其中一个, 或者不显示.
     *
     * @return 描述此 {@code Constructor} 的字符串, 包括类型参数.
     * @jls 8.8.3. Constructor Modifiers
     * @since 1.5
     */
    @Override
    public String toGenericString() {
        return sharedToGenericString(Modifier.constructorModifiers(), false);
    }

    @Override
    void specificToGenericStringHeader(StringBuilder sb) {
        specificToStringHeader(sb);
    }

    /**
     * 使用此 {@code Constructor} 对象表示的构造函数, 使用指定的初始化参数创建和初始化构造函数声明类的新实例.
     * 各个参数自动解包以匹配原始类型参数, 并且原始参数和参考参数都根据需要进行调整转换.
     *
     * <p>如果底层的构造函数所需的形参的数量为 0, 则提供的 {@code initargs} 数组的长度可以为 0 或者  null.
     *
     * <p>如果构造函数的声明类是非静态上下文中的内部类, 则构造函数的第一个参数需要是封闭实例;
     * 参见 <cite>The Java&trade; Language Specification</cite> 15.9.3 节.
     *
     * <p>如果所需的访问和参数检查成功并实例化将继续, 则构造函数的声明类如果尚未初始化则初始化.
     *
     * <p>如果构造函数正常完成, 则返回新创建和初始化的实例.
     *
     * @param initargs 要作为参数传递给构造函数调用的对象数组; 原始类型的值包装在适当类型的自动装箱对象中
     *                 (例如:{@code float} 装箱为 {@link java.lang.Float Float})
     * @return 通过调用此对象表示的构造函数创建的新对象
     * @throws IllegalAccessException      如果此 {@code Constructor} 对象强制执行 java 语言访问控制且
     *                                     基础构造函数不可访问.
     * @throws IllegalArgumentException    如果实际参数和类型参数的数量不同; 如果原始参数的展开转换失败; 或者,
     *                                     如果在可能的解包之后, 参数不能通过方法调用转换为相应的形式参数类型;
     *                                     如果此构造函数输入枚举类型.
     * @throws InstantiationException      如果声明底层构造函数的类是抽象类.
     * @throws InvocationTargetException   如果底层构造函数抛出了异常.
     * @throws ExceptionInInitializerError 如果此方法引发的初始化失败.
     */
    @CallerSensitive
    public T newInstance(Object... initargs)
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        if (!override) {
            if (!Reflection.quickCheckMemberAccess(clazz, modifiers)) {
                Class<?> caller = Reflection.getCallerClass();
                checkAccess(caller, clazz, null, modifiers);
            }
        }
        // 枚举类型
        if ((clazz.getModifiers() & Modifier.ENUM) != 0)
            throw new IllegalArgumentException("Cannot reflectively create enum objects");
        ConstructorAccessor ca = constructorAccessor;   // read volatile
        if (ca == null) {
            ca = acquireConstructorAccessor();
        }
        @SuppressWarnings("unchecked")
        T inst = (T) ca.newInstance(initargs);
        return inst;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.5
     */
    @Override
    public boolean isVarArgs() {
        return super.isVarArgs();
    }

    /**
     * {@inheritDoc}
     *
     * @jls 13.1 The Form of a Binary
     * @since 1.5
     */
    @Override
    public boolean isSynthetic() {
        return super.isSynthetic();
    }

    // 请注意, 此处未使用同步. 为给定的 Constructor 生成多个 ConstructorAccessor 是正确的(尽管效率不高).
    // 但是, 避免同步可能会使实现更具可伸缩性.
    private ConstructorAccessor acquireConstructorAccessor() {
        // 首先检查一下是否已经创建了一个, 如果是的话
        ConstructorAccessor tmp = null;
        // 共享 root 的 ConstructorAccessor
        if (root != null) tmp = root.getConstructorAccessor();
        if (tmp != null) {
            constructorAccessor = tmp;
        } else {
            // 否则制作一个并将其传播到根目录
            tmp = reflectionFactory.newConstructorAccessor(this);
            setConstructorAccessor(tmp);
        }

        return tmp;
    }

    // 返回此 Constructor 对象的构造函数访问器, 而不是查找到根的链
    ConstructorAccessor getConstructorAccessor() {
        return constructorAccessor;
    }

    // 为此 Constructor 对象设置 ConstructorAccessor, 并(递归地)设置其根
    void setConstructorAccessor(ConstructorAccessor accessor) {
        constructorAccessor = accessor;
        // 传播到根
        if (root != null) {
            root.setConstructorAccessor(accessor);
        }
    }

    int getSlot() {
        return slot;
    }

    String getSignature() {
        return signature;
    }

    byte[] getRawAnnotations() {
        return annotations;
    }

    byte[] getRawParameterAnnotations() {
        return parameterAnnotations;
    }


    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException {@inheritDoc}
     * @since 1.5
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return super.getAnnotation(annotationClass);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.5
     */
    public Annotation[] getDeclaredAnnotations() {
        return super.getDeclaredAnnotations();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.5
     */
    @Override
    public Annotation[][] getParameterAnnotations() {
        return sharedGetParameterAnnotations(parameterTypes, parameterAnnotations);
    }

    @Override
    void handleParameterNumberMismatch(int resultLength, int numParameters) {
        Class<?> declaringClass = getDeclaringClass();
        // 枚举, 匿名类, 局部类
        if (declaringClass.isEnum() ||
                declaringClass.isAnonymousClass() ||
                declaringClass.isLocalClass())
            return; // 不能做可靠的参数计数
        else {
            if (!declaringClass.isMemberClass() || // 顶层
                    // 检查非静态成员类的封闭实例参数
                    (declaringClass.isMemberClass() &&
                            ((declaringClass.getModifiers() & Modifier.STATIC) == 0) &&
                            resultLength + 1 != numParameters)) {
                throw new AnnotationFormatError(
                        "Parameter annotations don't match number of parameters");
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.8
     */
    @Override
    public AnnotatedType getAnnotatedReturnType() {
        return getAnnotatedReturnType0(getDeclaringClass());
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.8
     */
    @Override
    public AnnotatedType getAnnotatedReceiverType() {
        if (getDeclaringClass().getEnclosingClass() == null)
            return super.getAnnotatedReceiverType();

        return TypeAnnotationParser.buildAnnotatedType(getTypeAnnotationBytes0(),
                sun.misc.SharedSecrets.getJavaLangAccess().
                        getConstantPool(getDeclaringClass()),
                this,
                getDeclaringClass(),
                getDeclaringClass().getEnclosingClass(),
                TypeAnnotation.TypeAnnotationTarget.METHOD_RECEIVER);
    }
}
