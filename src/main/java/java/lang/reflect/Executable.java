package java.lang.reflect;

import java.lang.annotation.*;
import java.util.Map;
import java.util.Objects;

import sun.reflect.annotation.AnnotationParser;
import sun.reflect.annotation.AnnotationSupport;
import sun.reflect.annotation.TypeAnnotationParser;
import sun.reflect.annotation.TypeAnnotation;
import sun.reflect.generics.repository.ConstructorRepository;

/**
 * {@link Method} 和 {@link Constructor} 共同功能的共享超类.
 *
 * @since 1.8
 */
public abstract class Executable extends AccessibleObject
        implements Member, GenericDeclaration {
    /*
     * 仅授予构造函数的包可见性.
     */
    Executable() {
    }

    /**
     * 访问器方法允许代码共享
     */
    abstract byte[] getAnnotationBytes();

    /**
     * 访问器方法允许代码共享
     */
    abstract Executable getRoot();

    /**
     * Executable 是否拥有通用的信息.
     */
    abstract boolean hasGenericInformation();

    /**
     * 获取通用信息.
     */
    abstract ConstructorRepository getGenericInfo();

    /**
     * 判断给定的两个参数数组是否完全相等.
     */
    boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
        /* 避免不必要的克隆 */
        if (params1.length == params2.length) {
            for (int i = 0; i < params1.length; i++) {
                if (params1[i] != params2[i])
                    return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 解析参数化注解类型.
     */
    Annotation[][] parseParameterAnnotations(byte[] parameterAnnotations) {
        return AnnotationParser.parseParameterAnnotations(
                parameterAnnotations,
                sun.misc.SharedSecrets.getJavaLangAccess().
                        getConstantPool(getDeclaringClass()),
                getDeclaringClass());
    }

    /**
     * 用逗号把给定的 Class 对象数组中的元素的类型名称分开.
     * 使用它们的 {@link Class#getTypeName()} .
     */
    void separateWithCommas(Class<?>[] types, StringBuilder sb) {
        for (int j = 0; j < types.length; j++) {
            sb.append(types[j].getTypeName());
            if (j < (types.length - 1))
                sb.append(",");
        }

    }

    /**
     * 打印修饰符到 {@code sb} 中, 如果修饰符存在
     */
    void printModifiersIfNonzero(StringBuilder sb, int mask, boolean isDefault) {
        // 判断是否包含 mask 类型的修饰符
        int mod = getModifiers() & mask;

        if (mod != 0 && !isDefault) {
            sb.append(Modifier.toString(mod)).append(' ');
        } else {
            // mod == 0 OR isDefault
            // 访问修饰符
            int access_mod = mod & Modifier.ACCESS_MODIFIERS;
            if (access_mod != 0)
                sb.append(Modifier.toString(access_mod)).append(' ');
            if (isDefault)
                sb.append("default ");
            mod = (mod & ~Modifier.ACCESS_MODIFIERS);
            if (mod != 0)
                sb.append(Modifier.toString(mod)).append(' ');
        }
    }

    /**
     * 返回方法或者构造器的字符串描述形式.
     * <p>
     * {@link Constructor} 和 {@link Method} 的 {@code toString()} 方法
     * 是通过直接调用该函数生成的.
     *
     * @see Constructor#toString()
     * @see Method#toString()
     */
    String sharedToString(int modifierMask,
                          boolean isDefault,
                          Class<?>[] parameterTypes,
                          Class<?>[] exceptionTypes) {
        try {
            StringBuilder sb = new StringBuilder();

            // 打印修饰符到 {@code sb} 中
            printModifiersIfNonzero(sb, modifierMask, isDefault);
            specificToStringHeader(sb);

            // 打印参数类型
            sb.append('(');
            separateWithCommas(parameterTypes, sb);
            sb.append(')');
            // 打印异常信息
            if (exceptionTypes.length > 0) {
                sb.append(" throws ");
                separateWithCommas(exceptionTypes, sb);
            }
            return sb.toString();
        } catch (Exception e) {
            // 如果发生异常, 则直接返回该异常
            return "<" + e + ">";
        }
    }

    /**
     * 生成特定于方法或构造函数的 toString 标头信息, (类名?).
     */
    abstract void specificToStringHeader(StringBuilder sb);

    /**
     * 返回方法或者构造器的字符串描述形式, 包含类型化参数.
     */
    String sharedToGenericString(int modifierMask, boolean isDefault) {
        try {
            StringBuilder sb = new StringBuilder();

            printModifiersIfNonzero(sb, modifierMask, isDefault);

            TypeVariable<?>[] typeparms = getTypeParameters();
            if (typeparms.length > 0) {
                boolean first = true;
                sb.append('<');
                for (TypeVariable<?> typeparm : typeparms) {
                    if (!first)
                        sb.append(',');
                    // 这里不能出现类对象; 无需测试并调用 Class.getName().
                    sb.append(typeparm.toString());
                    first = false;
                }
                sb.append("> ");
            }

            specificToGenericStringHeader(sb);

            // 参数体
            sb.append('(');
            Type[] params = getGenericParameterTypes();
            for (int j = 0; j < params.length; j++) {
                String param = params[j].getTypeName();
                if (isVarArgs() && (j == params.length - 1))
                    // 将最后的 T[] 替换为 T...
                    param = param.replaceFirst("\\[\\]$", "...");
                sb.append(param);
                if (j < (params.length - 1))
                    sb.append(',');
            }
            sb.append(')');
            // 异常
            Type[] exceptions = getGenericExceptionTypes();
            if (exceptions.length > 0) {
                sb.append(" throws ");
                for (int k = 0; k < exceptions.length; k++) {
                    sb.append((exceptions[k] instanceof Class) ?
                            ((Class) exceptions[k]).getName() :
                            exceptions[k].toString());
                    if (k < (exceptions.length - 1))
                        sb.append(',');
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return "<" + e + ">";
        }
    }

    /**
     * 生成特定于方法或构造函数的 toGenericString 标头信息.
     */
    abstract void specificToGenericStringHeader(StringBuilder sb);

    /**
     * 返回表示声明此对象表示的可执行成员的类或接口的{@code Class}对象.
     */
    public abstract Class<?> getDeclaringClass();

    /**
     * 返回此对象表示的可执行成员的名称.
     */
    public abstract String getName();

    /**
     * 返回此对象表示的可执行成员的修饰符 {@linkplain Modifier modifiers}.
     */
    public abstract int getModifiers();

    /**
     * 返回 {@code TypeVariable} 对象的数组， 这些对象表示由此 {@code GenericDeclaration} 对象
     * 表示的泛型声明声明的类型变量, 按声明顺序. 如果底层泛型声明未声明类型变量, 则返回长度为 0 的数组.
     *
     * @return {@code TypeVariable} 对象的数组， 这些对象表示由此 {@code GenericDeclaration} 对象
     * 表示的泛型声明声明的类型变量
     * @throws GenericSignatureFormatError 如果此通用声明的通用签名不符合
     *                                     <cite>The Java&trade; Virtual Machine Specification</cite>中指定的格式
     */
    public abstract TypeVariable<?>[] getTypeParameters();

    /**
     * 返回一个 {@code Class} 对象数组代表该可执行成员的正式的按声明顺序的参数类型,
     * 如果底层可执行成员不带参数, 则返回长度为 0 的数组.
     *
     * @return 此可执行成员代表的参数类型的列表
     */
    public abstract Class<?>[] getParameterTypes();

    /**
     * 返回此对象表示的可执行成员的形式参数的数量(无论是显式声明还是隐式声明, 或两者都没有).
     * <p>
     * 默认实现直接抛出 {@link AbstractMethodError} 错误.
     *
     * @return 此对象表示的可执行成员的形式参数的数量.
     */
    public int getParameterCount() {
        throw new AbstractMethodError();
    }

    /**
     * 返回一个 {@code Type} 对象数组代表该可执行成员的正式的按声明顺序的参数类型,
     * 如果底层可执行成员不带参数, 则返回长度为 0 的数组.
     *
     * <p>If a formal parameter type is a parameterized type,
     * the {@code Type} object returned for it must accurately reflect
     * the actual type parameters used in the source code.
     *
     * <p>If a formal parameter type is a type variable or a parameterized
     * type, it is created. Otherwise, it is resolved.
     *
     * @return an array of {@code Type}s that represent the formal
     * parameter types of the underlying executable, in declaration order
     * @throws GenericSignatureFormatError         if the generic method signature does not conform to the format
     *                                             specified in
     *                                             <cite>The Java&trade; Virtual Machine Specification</cite>
     * @throws TypeNotPresentException             if any of the parameter
     *                                             types of the underlying executable refers to a non-existent type
     *                                             declaration
     * @throws MalformedParameterizedTypeException if any of
     *                                             the underlying executable's parameter types refer to a parameterized
     *                                             type that cannot be instantiated for any reason
     */
    public Type[] getGenericParameterTypes() {
        if (hasGenericInformation())
            return getGenericInfo().getParameterTypes();
        else
            return getParameterTypes();
    }

    /**
     * Behaves like {@code getGenericParameterTypes}, but returns type
     * information for all parameters, including synthetic parameters.
     */
    Type[] getAllGenericParameterTypes() {
        final boolean genericInfo = hasGenericInformation();

        // Easy case: we don't have generic parameter information.  In
        // this case, we just return the result of
        // getParameterTypes().
        if (!genericInfo) {
            return getParameterTypes();
        } else {
            final boolean realParamData = hasRealParameterData();
            final Type[] genericParamTypes = getGenericParameterTypes();
            final Type[] nonGenericParamTypes = getParameterTypes();
            final Type[] out = new Type[nonGenericParamTypes.length];
            final Parameter[] params = getParameters();
            int fromidx = 0;
            // If we have real parameter data, then we use the
            // synthetic and mandate flags to our advantage.
            if (realParamData) {
                for (int i = 0; i < out.length; i++) {
                    final Parameter param = params[i];
                    if (param.isSynthetic() || param.isImplicit()) {
                        // If we hit a synthetic or mandated parameter,
                        // use the non generic parameter info.
                        out[i] = nonGenericParamTypes[i];
                    } else {
                        // Otherwise, use the generic parameter info.
                        out[i] = genericParamTypes[fromidx];
                        fromidx++;
                    }
                }
            } else {
                // Otherwise, use the non-generic parameter data.
                // Without method parameter reflection data, we have
                // no way to figure out which parameters are
                // synthetic/mandated, thus, no way to match up the
                // indexes.
                return genericParamTypes.length == nonGenericParamTypes.length ?
                        genericParamTypes : nonGenericParamTypes;
            }
            return out;
        }
    }

    /**
     * Returns an array of {@code Parameter} objects that represent
     * all the parameters to the underlying executable represented by
     * this object.  Returns an array of length 0 if the executable
     * has no parameters.
     *
     * <p>The parameters of the underlying executable do not necessarily
     * have unique names, or names that are legal identifiers in the
     * Java programming language (JLS 3.8).
     *
     * @return an array of {@code Parameter} objects representing all
     * the parameters to the executable this object represents.
     * @throws MalformedParametersException if the class file contains
     *                                      a MethodParameters attribute that is improperly formatted.
     */
    public Parameter[] getParameters() {
        // TODO: This may eventually need to be guarded by security
        // mechanisms similar to those in Field, Method, etc.
        //
        // Need to copy the cached array to prevent users from messing
        // with it.  Since parameters are immutable, we can
        // shallow-copy.
        return privateGetParameters().clone();
    }

    private Parameter[] synthesizeAllParams() {
        final int realparams = getParameterCount();
        final Parameter[] out = new Parameter[realparams];
        for (int i = 0; i < realparams; i++)
            // TODO: is there a way to synthetically derive the
            // modifiers?  Probably not in the general case, since
            // we'd have no way of knowing about them, but there
            // may be specific cases.
            out[i] = new Parameter("arg" + i, 0, this, i);
        return out;
    }

    private void verifyParameters(final Parameter[] parameters) {
        final int mask = Modifier.FINAL | Modifier.SYNTHETIC | Modifier.MANDATED;

        if (getParameterTypes().length != parameters.length)
            throw new MalformedParametersException("Wrong number of parameters in MethodParameters attribute");

        for (Parameter parameter : parameters) {
            final String name = parameter.getRealName();
            final int mods = parameter.getModifiers();

            if (name != null) {
                if (name.isEmpty() || name.indexOf('.') != -1 ||
                        name.indexOf(';') != -1 || name.indexOf('[') != -1 ||
                        name.indexOf('/') != -1) {
                    throw new MalformedParametersException("Invalid parameter name \"" + name + "\"");
                }
            }

            if (mods != (mods & mask)) {
                throw new MalformedParametersException("Invalid parameter modifiers");
            }
        }
    }

    private Parameter[] privateGetParameters() {
        // Use tmp to avoid multiple writes to a volatile.
        Parameter[] tmp = parameters;

        if (tmp == null) {

            // Otherwise, go to the JVM to get them
            try {
                tmp = getParameters0();
            } catch (IllegalArgumentException e) {
                // Rethrow ClassFormatErrors
                throw new MalformedParametersException("Invalid constant pool index");
            }

            // If we get back nothing, then synthesize parameters
            if (tmp == null) {
                hasRealParameterData = false;
                tmp = synthesizeAllParams();
            } else {
                hasRealParameterData = true;
                verifyParameters(tmp);
            }

            parameters = tmp;
        }

        return tmp;
    }

    boolean hasRealParameterData() {
        // If this somehow gets called before parameters gets
        // initialized, force it into existence.
        if (parameters == null) {
            privateGetParameters();
        }
        return hasRealParameterData;
    }

    private transient volatile boolean hasRealParameterData;
    private transient volatile Parameter[] parameters;

    private native Parameter[] getParameters0();

    native byte[] getTypeAnnotationBytes0();

    // Needed by reflectaccess
    byte[] getTypeAnnotationBytes() {
        return getTypeAnnotationBytes0();
    }

    /**
     * Returns an array of {@code Class} objects that represent the
     * types of exceptions declared to be thrown by the underlying
     * executable represented by this object.  Returns an array of
     * length 0 if the executable declares no exceptions in its {@code
     * throws} clause.
     *
     * @return the exception types declared as being thrown by the
     * executable this object represents
     */
    public abstract Class<?>[] getExceptionTypes();

    /**
     * Returns an array of {@code Type} objects that represent the
     * exceptions declared to be thrown by this executable object.
     * Returns an array of length 0 if the underlying executable declares
     * no exceptions in its {@code throws} clause.
     *
     * <p>If an exception type is a type variable or a parameterized
     * type, it is created. Otherwise, it is resolved.
     *
     * @return an array of Types that represent the exception types
     * thrown by the underlying executable
     * @throws GenericSignatureFormatError         if the generic method signature does not conform to the format
     *                                             specified in
     *                                             <cite>The Java&trade; Virtual Machine Specification</cite>
     * @throws TypeNotPresentException             if the underlying executable's
     *                                             {@code throws} clause refers to a non-existent type declaration
     * @throws MalformedParameterizedTypeException if
     *                                             the underlying executable's {@code throws} clause refers to a
     *                                             parameterized type that cannot be instantiated for any reason
     */
    public Type[] getGenericExceptionTypes() {
        Type[] result;
        if (hasGenericInformation() &&
                ((result = getGenericInfo().getExceptionTypes()).length > 0))
            return result;
        else
            return getExceptionTypes();
    }

    /**
     * Returns a string describing this {@code Executable}, including
     * any type parameters.
     *
     * @return a string describing this {@code Executable}, including
     * any type parameters
     */
    public abstract String toGenericString();

    /**
     * Returns {@code true} if this executable was declared to take a
     * variable number of arguments; returns {@code false} otherwise.
     *
     * @return {@code true} if an only if this executable was declared
     * to take a variable number of arguments.
     */
    public boolean isVarArgs() {
        return (getModifiers() & Modifier.VARARGS) != 0;
    }

    /**
     * Returns {@code true} if this executable is a synthetic
     * construct; returns {@code false} otherwise.
     *
     * @return true if and only if this executable is a synthetic
     * construct as defined by
     * <cite>The Java&trade; Language Specification</cite>.
     * @jls 13.1 The Form of a Binary
     */
    public boolean isSynthetic() {
        return Modifier.isSynthetic(getModifiers());
    }

    /**
     * Returns an array of arrays of {@code Annotation}s that
     * represent the annotations on the formal parameters, in
     * declaration order, of the {@code Executable} represented by
     * this object.  Synthetic and mandated parameters (see
     * explanation below), such as the outer "this" parameter to an
     * inner class constructor will be represented in the returned
     * array.  If the executable has no parameters (meaning no formal,
     * no synthetic, and no mandated parameters), a zero-length array
     * will be returned.  If the {@code Executable} has one or more
     * parameters, a nested array of length zero is returned for each
     * parameter with no annotations. The annotation objects contained
     * in the returned arrays are serializable.  The caller of this
     * method is free to modify the returned arrays; it will have no
     * effect on the arrays returned to other callers.
     * <p>
     * A compiler may add extra parameters that are implicitly
     * declared in source ("mandated"), as well as parameters that
     * are neither implicitly nor explicitly declared in source
     * ("synthetic") to the parameter list for a method.  See {@link
     * java.lang.reflect.Parameter} for more information.
     *
     * @return an array of arrays that represent the annotations on
     * the formal and implicit parameters, in declaration order, of
     * the executable represented by this object
     * @see java.lang.reflect.Parameter
     * @see java.lang.reflect.Parameter#getAnnotations
     */
    public abstract Annotation[][] getParameterAnnotations();

    Annotation[][] sharedGetParameterAnnotations(Class<?>[] parameterTypes,
                                                 byte[] parameterAnnotations) {
        int numParameters = parameterTypes.length;
        if (parameterAnnotations == null)
            return new Annotation[numParameters][0];

        Annotation[][] result = parseParameterAnnotations(parameterAnnotations);

        if (result.length != numParameters)
            handleParameterNumberMismatch(result.length, numParameters);
        return result;
    }

    abstract void handleParameterNumberMismatch(int resultLength, int numParameters);

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException {@inheritDoc}
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        Objects.requireNonNull(annotationClass);
        return annotationClass.cast(declaredAnnotations().get(annotationClass));
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
        Objects.requireNonNull(annotationClass);

        return AnnotationSupport.getDirectlyAndIndirectlyPresent(declaredAnnotations(), annotationClass);
    }

    /**
     * {@inheritDoc}
     */
    public Annotation[] getDeclaredAnnotations() {
        return AnnotationParser.toArray(declaredAnnotations());
    }

    private transient Map<Class<? extends Annotation>, Annotation> declaredAnnotations;

    private synchronized Map<Class<? extends Annotation>, Annotation> declaredAnnotations() {
        if (declaredAnnotations == null) {
            Executable root = getRoot();
            if (root != null) {
                declaredAnnotations = root.declaredAnnotations();
            } else {
                declaredAnnotations = AnnotationParser.parseAnnotations(
                        getAnnotationBytes(),
                        sun.misc.SharedSecrets.getJavaLangAccess().
                                getConstantPool(getDeclaringClass()),
                        getDeclaringClass());
            }
        }
        return declaredAnnotations;
    }

    /**
     * Returns an {@code AnnotatedType} object that represents the use of a type to
     * specify the return type of the method/constructor represented by this
     * Executable.
     * <p>
     * If this {@code Executable} object represents a constructor, the {@code
     * AnnotatedType} object represents the type of the constructed object.
     * <p>
     * If this {@code Executable} object represents a method, the {@code
     * AnnotatedType} object represents the use of a type to specify the return
     * type of the method.
     *
     * @return an object representing the return type of the method
     * or constructor represented by this {@code Executable}
     */
    public abstract AnnotatedType getAnnotatedReturnType();

    /* Helper for subclasses of Executable.
     *
     * Returns an AnnotatedType object that represents the use of a type to
     * specify the return type of the method/constructor represented by this
     * Executable.
     */
    AnnotatedType getAnnotatedReturnType0(Type returnType) {
        return TypeAnnotationParser.buildAnnotatedType(getTypeAnnotationBytes0(),
                sun.misc.SharedSecrets.getJavaLangAccess().
                        getConstantPool(getDeclaringClass()),
                this,
                getDeclaringClass(),
                returnType,
                TypeAnnotation.TypeAnnotationTarget.METHOD_RETURN);
    }

    /**
     * Returns an {@code AnnotatedType} object that represents the use of a
     * type to specify the receiver type of the method/constructor represented
     * by this Executable object. The receiver type of a method/constructor is
     * available only if the method/constructor has a <em>receiver
     * parameter</em> (JLS 8.4.1).
     * <p>
     * If this {@code Executable} object represents a constructor or instance
     * method that does not have a receiver parameter, or has a receiver
     * parameter with no annotations on its type, then the return value is an
     * {@code AnnotatedType} object representing an element with no
     * annotations.
     * <p>
     * If this {@code Executable} object represents a static method, then the
     * return value is null.
     *
     * @return an object representing the receiver type of the method or
     * constructor represented by this {@code Executable}
     */
    public AnnotatedType getAnnotatedReceiverType() {
        if (Modifier.isStatic(this.getModifiers()))
            return null;
        return TypeAnnotationParser.buildAnnotatedType(getTypeAnnotationBytes0(),
                sun.misc.SharedSecrets.getJavaLangAccess().
                        getConstantPool(getDeclaringClass()),
                this,
                getDeclaringClass(),
                getDeclaringClass(),
                TypeAnnotation.TypeAnnotationTarget.METHOD_RECEIVER);
    }

    /**
     * Returns an array of {@code AnnotatedType} objects that represent the use
     * of types to specify formal parameter types of the method/constructor
     * represented by this Executable. The order of the objects in the array
     * corresponds to the order of the formal parameter types in the
     * declaration of the method/constructor.
     * <p>
     * Returns an array of length 0 if the method/constructor declares no
     * parameters.
     *
     * @return an array of objects representing the types of the
     * formal parameters of the method or constructor represented by this
     * {@code Executable}
     */
    public AnnotatedType[] getAnnotatedParameterTypes() {
        return TypeAnnotationParser.buildAnnotatedTypes(getTypeAnnotationBytes0(),
                sun.misc.SharedSecrets.getJavaLangAccess().
                        getConstantPool(getDeclaringClass()),
                this,
                getDeclaringClass(),
                getAllGenericParameterTypes(),
                TypeAnnotation.TypeAnnotationTarget.METHOD_FORMAL_PARAMETER);
    }

    /**
     * Returns an array of {@code AnnotatedType} objects that represent the use
     * of types to specify the declared exceptions of the method/constructor
     * represented by this Executable. The order of the objects in the array
     * corresponds to the order of the exception types in the declaration of
     * the method/constructor.
     * <p>
     * Returns an array of length 0 if the method/constructor declares no
     * exceptions.
     *
     * @return an array of objects representing the declared
     * exceptions of the method or constructor represented by this {@code
     * Executable}
     */
    public AnnotatedType[] getAnnotatedExceptionTypes() {
        return TypeAnnotationParser.buildAnnotatedTypes(getTypeAnnotationBytes0(),
                sun.misc.SharedSecrets.getJavaLangAccess().
                        getConstantPool(getDeclaringClass()),
                this,
                getDeclaringClass(),
                getGenericExceptionTypes(),
                TypeAnnotation.TypeAnnotationTarget.THROWS);
    }

}
