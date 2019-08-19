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
     * Compares this {@code Constructor} against the specified object.
     * Returns true if the objects are the same.  Two {@code Constructor} objects are
     * the same if they were declared by the same class and have the
     * same formal parameter types.
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
     * Returns a hashcode for this {@code Constructor}. The hashcode is
     * the same as the hashcode for the underlying constructor's
     * declaring class name.
     */
    public int hashCode() {
        return getDeclaringClass().getName().hashCode();
    }

    /**
     * Returns a string describing this {@code Constructor}.  The string is
     * formatted as the constructor access modifiers, if any,
     * followed by the fully-qualified name of the declaring class,
     * followed by a parenthesized, comma-separated list of the
     * constructor's formal parameter types.  For example:
     * <pre>
     *    public java.util.Hashtable(int,float)
     * </pre>
     *
     * <p>The only possible modifiers for constructors are the access
     * modifiers {@code public}, {@code protected} or
     * {@code private}.  Only one of these may appear, or none if the
     * constructor has default (package) access.
     *
     * @return a string describing this {@code Constructor}
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
     * Returns a string describing this {@code Constructor},
     * including type parameters.  The string is formatted as the
     * constructor access modifiers, if any, followed by an
     * angle-bracketed comma separated list of the constructor's type
     * parameters, if any, followed by the fully-qualified name of the
     * declaring class, followed by a parenthesized, comma-separated
     * list of the constructor's generic formal parameter types.
     * <p>
     * If this constructor was declared to take a variable number of
     * arguments, instead of denoting the last parameter as
     * "<tt><i>Type</i>[]</tt>", it is denoted as
     * "<tt><i>Type</i>...</tt>".
     * <p>
     * A space is used to separate access modifiers from one another
     * and from the type parameters or return type.  If there are no
     * type parameters, the type parameter list is elided; if the type
     * parameter list is present, a space separates the list from the
     * class name.  If the constructor is declared to throw
     * exceptions, the parameter list is followed by a space, followed
     * by the word "{@code throws}" followed by a
     * comma-separated list of the thrown exception types.
     *
     * <p>The only possible modifiers for constructors are the access
     * modifiers {@code public}, {@code protected} or
     * {@code private}.  Only one of these may appear, or none if the
     * constructor has default (package) access.
     *
     * @return a string describing this {@code Constructor},
     * include type parameters
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
     * Uses the constructor represented by this {@code Constructor} object to
     * create and initialize a new instance of the constructor's
     * declaring class, with the specified initialization parameters.
     * Individual parameters are automatically unwrapped to match
     * primitive formal parameters, and both primitive and reference
     * parameters are subject to method invocation conversions as necessary.
     *
     * <p>If the number of formal parameters required by the underlying constructor
     * is 0, the supplied {@code initargs} array may be of length 0 or null.
     *
     * <p>If the constructor's declaring class is an inner class in a
     * non-static context, the first argument to the constructor needs
     * to be the enclosing instance; see section 15.9.3 of
     * <cite>The Java&trade; Language Specification</cite>.
     *
     * <p>If the required access and argument checks succeed and the
     * instantiation will proceed, the constructor's declaring class
     * is initialized if it has not already been initialized.
     *
     * <p>If the constructor completes normally, returns the newly
     * created and initialized instance.
     *
     * @param initargs array of objects to be passed as arguments to
     *                 the constructor call; values of primitive types are wrapped in
     *                 a wrapper object of the appropriate type (e.g. a {@code float}
     *                 in a {@link java.lang.Float Float})
     * @return a new object created by calling the constructor
     * this object represents
     * @throws IllegalAccessException      if this {@code Constructor} object
     *                                     is enforcing Java language access control and the underlying
     *                                     constructor is inaccessible.
     * @throws IllegalArgumentException    if the number of actual
     *                                     and formal parameters differ; if an unwrapping
     *                                     conversion for primitive arguments fails; or if,
     *                                     after possible unwrapping, a parameter value
     *                                     cannot be converted to the corresponding formal
     *                                     parameter type by a method invocation conversion; if
     *                                     this constructor pertains to an enum type.
     * @throws InstantiationException      if the class that declares the
     *                                     underlying constructor represents an abstract class.
     * @throws InvocationTargetException   if the underlying constructor
     *                                     throws an exception.
     * @throws ExceptionInInitializerError if the initialization provoked
     *                                     by this method fails.
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

    // NOTE that there is no synchronization used here. It is correct
    // (though not efficient) to generate more than one
    // ConstructorAccessor for a given Constructor. However, avoiding
    // synchronization will probably make the implementation more
    // scalable.
    private ConstructorAccessor acquireConstructorAccessor() {
        // First check to see if one has been created yet, and take it
        // if so.
        ConstructorAccessor tmp = null;
        if (root != null) tmp = root.getConstructorAccessor();
        if (tmp != null) {
            constructorAccessor = tmp;
        } else {
            // Otherwise fabricate one and propagate it up to the root
            tmp = reflectionFactory.newConstructorAccessor(this);
            setConstructorAccessor(tmp);
        }

        return tmp;
    }

    // Returns ConstructorAccessor for this Constructor object, not
    // looking up the chain to the root
    ConstructorAccessor getConstructorAccessor() {
        return constructorAccessor;
    }

    // Sets the ConstructorAccessor for this Constructor object and
    // (recursively) its root
    void setConstructorAccessor(ConstructorAccessor accessor) {
        constructorAccessor = accessor;
        // Propagate up
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
        if (declaringClass.isEnum() ||
                declaringClass.isAnonymousClass() ||
                declaringClass.isLocalClass())
            return; // Can't do reliable parameter counting
        else {
            if (!declaringClass.isMemberClass() || // top-level
                    // Check for the enclosing instance parameter for
                    // non-static member classes
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
