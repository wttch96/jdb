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
     * Wttch:
     * 通过反射获取到的是 Executable 的拷贝, 通过 getRoot() 方法可以获取真正的 Executable 对象.
     */
    abstract Executable getRoot();

    /**
     * Executable 是否拥有泛型的信息.
     */
    abstract boolean hasGenericInformation();

    /**
     * 获取泛型信息.
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
                    // 这里不能出现类成员; 无需测试并调用 Class.getName().
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
     * 返回表示声明此对象表示的可执行对象的类或接口的{@code Class}对象.
     */
    @Override
    public abstract Class<?> getDeclaringClass();

    /**
     * 返回此对象表示的可执行对象的名称.
     */
    @Override
    public abstract String getName();

    /**
     * 返回此对象表示的可执行对象的修饰符 {@linkplain Modifier modifiers}.
     */
    @Override
    public abstract int getModifiers();

    /**
     * 返回 {@code TypeVariable} 对象的数组， 这些对象表示由此 {@code GenericDeclaration} 对象
     * 表示的泛型声明声明的类型变量, 按声明顺序. 如果底层泛型声明未声明类型变量, 则返回长度为 0 的数组.
     *
     * @return {@code TypeVariable} 对象的数组， 这些对象表示由此 {@code GenericDeclaration} 对象
     * 表示的泛型声明声明的类型变量
     * @throws GenericSignatureFormatError 如果此泛型声明的泛型签名不符合
     *                                     <cite>The Java&trade; Virtual Machine Specification</cite>中指定的格式
     */
    @Override
    public abstract TypeVariable<?>[] getTypeParameters();

    /**
     * 返回一个 {@code Class} 对象数组代表该可执行对象的正式的按声明顺序的参数类型,
     * 如果底层可执行对象不带参数, 则返回长度为 0 的数组.
     *
     * @return 此可执行对象代表的参数类型的列表
     */
    public abstract Class<?>[] getParameterTypes();

    /**
     * 返回此对象表示的可执行对象的形式参数的数量(无论是显式声明还是隐式声明, 或两者都没有).
     * <p>
     * 默认实现直接抛出 {@link AbstractMethodError} 错误.
     *
     * @return 此对象表示的可执行对象的形式参数的数量.
     */
    public int getParameterCount() {
        throw new AbstractMethodError();
    }

    /**
     * 返回一个 {@code Type} 对象数组代表该可执行对象的正式的按声明顺序的参数类型,
     * 如果底层可执行对象不带参数, 则返回长度为 0 的数组.
     *
     * <p>如果形式参数类型是参数化类型, 则为其返回的{@code Type}对象必须准确反映
     * 源代码中使用的实际类型参数.
     *
     * <p>如果形式参数类型是类型变量或参数化类型, 则创建它.否则, 它就解决了.
     *
     * @return 一个{@code Type}数组, 以声明顺序表示底层可执行对象的形式参数类型
     * @throws GenericSignatureFormatError         如果泛型方法签名不符合<cite>JVM 规范</cite>中指定的格式
     * @throws TypeNotPresentException             如果底层可执行对象的任何参数类型引用了不存在的类型声明
     * @throws MalformedParameterizedTypeException 如果任何底层可执行对象的参数类型引用了因任何原因无法实例化的参数化类型
     */
    public Type[] getGenericParameterTypes() {
        if (hasGenericInformation())
            return getGenericInfo().getParameterTypes();
        else
            return getParameterTypes();
    }

    /**
     * 和 {@code getGenericParameterTypes} 方法一样, 但返回所有参数的类型信息, 包括合成参数.
     * TODO Synthetic Parameters? 以后发现了记得重现
     */
    Type[] getAllGenericParameterTypes() {
        final boolean genericInfo = hasGenericInformation();

        // 简单情况: 没有泛型信息. 这种情况下, 只需要返回 getParameterTypes() 的结果即可.
        if (!genericInfo) {
            return getParameterTypes();
        } else {
            // 是否存在真正的参数数据
            final boolean realParamData = hasRealParameterData();
            // 泛型参数类型
            final Type[] genericParamTypes = getGenericParameterTypes();
            // 参数类型
            final Type[] nonGenericParamTypes = getParameterTypes();
            // 非参数类型参数的数量
            final Type[] out = new Type[nonGenericParamTypes.length];
            // 获取参数信息
            final Parameter[] params = getParameters();
            int fromidx = 0;
            // 如果我们有真实的参数数据, 那么我们就可以使用 synthetic 和 mandate 标志
            if (realParamData) {
                for (int i = 0; i < out.length; i++) {
                    final Parameter param = params[i];
                    if (param.isSynthetic() || param.isImplicit()) {
                        //如果我们明中 synthetic 或 mandate 参数, 使用非泛型参数信息.
                        out[i] = nonGenericParamTypes[i];
                    } else {
                        // 否则使用泛型参数信息
                        out[i] = genericParamTypes[fromidx];
                        fromidx++;
                    }
                }
            } else {
                // 否则, 使用非泛型参数数据.
                // 如果没有方法参数反射数据, 我们无法确定哪些参数是合成/强制的,
                // 因此无法匹配索引.
                return genericParamTypes.length == nonGenericParamTypes.length ?
                        genericParamTypes : nonGenericParamTypes;
            }
            return out;
        }
    }

    /**
     * 返回一个 {@code Parameter} 对象数组, 表示此对象表示的底层可执行对象的所有参数.
     * 如果可执行对象没有参数, 则返回长度为 0 的数组.
     *
     * <p>底层可执行对象的参数不一定具有唯一的名称, 或者是 java 语言中和合法标示符的名称 (JSL 3.8).
     *
     * @return 一个 {@code Parameter} 对象数组, 表示此对象表示的底层可执行对象的所有参数.
     * @throws MalformedParametersException 如果类成员包含格式不正确的 MethodParameters 属性.
     */
    public Parameter[] getParameters() {
        // TODO 最终可能需要通过与 Field, Method等类似的安全机制来保护
        //
        // 需要复制缓存的数组以防止用户表单丢失. 由于参数是不可变的, 我们可以浅层复制.
        return privateGetParameters().clone();
    }

    /**
     * 根据参数的数量, 合成参数
     *
     * @return 合成的参数数组
     */
    private Parameter[] synthesizeAllParams() {
        final int realparams = getParameterCount();
        final Parameter[] out = new Parameter[realparams];
        for (int i = 0; i < realparams; i++)
            // TODO: 有没有办法综合推导修饰符? 可能不是在一般情况下, 因为我们无法了结它们, 但是可能存在特殊情况.
            out[i] = new Parameter("arg" + i, 0, this, i);
        return out;
    }

    /**
     * 校验参数类型
     *
     * @param parameters 要检验的参数类型数组
     * @throws MalformedParametersException 参数格式错误:
     *                                      参数类型数量和参数数量不相等;
     *                                      参数类型名字为空或者存在 {code .}, {@code ;}, {@code [} 和 {@code /};
     *                                      参数类型的修饰符是 {@link Modifier#FINAL}, {@link Modifier#SYNTHETIC}
     *                                      或者 {@link Modifier#MANDATED} 修饰的.
     */
    private void verifyParameters(final Parameter[] parameters) {
        final int mask = Modifier.FINAL | Modifier.SYNTHETIC | Modifier.MANDATED;

        // 参数类型的数量和参数数量不同
        if (getParameterTypes().length != parameters.length)
            throw new MalformedParametersException("Wrong number of parameters in MethodParameters attribute");

        for (Parameter parameter : parameters) {
            final String name = parameter.getRealName();
            final int mods = parameter.getModifiers();

            if (name != null) {
                // 参数类型名字为空或者存在 {code .}, {@code ;}, {@code [} 和 {@code /}
                if (name.isEmpty() || name.indexOf('.') != -1 ||
                        name.indexOf(';') != -1 || name.indexOf('[') != -1 ||
                        name.indexOf('/') != -1) {
                    throw new MalformedParametersException("Invalid parameter name \"" + name + "\"");
                }
            }

            if (mods != (mods & mask)) {
                // 参数访问修饰符中包含 {@link Modifier#FINAL}, {@link Modifier#SYNTHETIC}
                // 或者 {@link Modifier#MANDATED} 修饰的
                throw new MalformedParametersException("Invalid parameter modifiers");
            }
        }
    }

    /**
     * 私有的获取参数信息.
     * 如果存在参数信息直接返回.
     * <p>
     * 如果不存在参数信息, 通过调用 {@link #getParameters0()} 向 JVM 请求获取参数信息.
     * 如果获取到了参数信息将初始化 {@link #parameters} 并将 {@link #hasRealParameterData} 设置为 {@code true},
     * 之后调用 {@link #verifyParameters(Parameter[])} 对这些参数进行校验;
     * 否则合成这些参数类型, 但是 {@link #hasRealParameterData} 设置为 {@code false},
     * 这样合成的参数, 仅仅包含了参数位置信息, 别的信息无法包含. 我不知道为什么会出现这种情况:
     * 既然从 JVM 获取了参数数量, 为什么获取不到任何参数信息.
     *
     * @throws MalformedParametersException 参数格式错误:
     *                                      获取不到常量池中的参数信息;或者
     *                                      参数中存在某个参数存在以下问题:
     *                                      参数类型数量和参数数量不相等;
     *                                      参数类型名字为空或者存在 {code .}, {@code ;}, {@code [} 和 {@code /};
     *                                      参数类型的修饰符是 {@link Modifier#FINAL}, {@link Modifier#SYNTHETIC}
     *                                      或者 {@link Modifier#MANDATED} 修饰的.
     * @see #getParameters0()
     * @see #synthesizeAllParams()
     * @see #verifyParameters(Parameter[])
     */
    private Parameter[] privateGetParameters() {
        // 使用 tmp 来避免多次写入 volatile 变量.
        Parameter[] tmp = parameters;

        if (tmp == null) {

            // 否则, 转到 JVM 以获取它们
            try {
                tmp = getParameters0();
            } catch (IllegalArgumentException e) {
                // 抛出 ClassFormatErrors
                throw new MalformedParametersException("Invalid constant pool index");
            }

            // 如果没有获取到任何参数信息, 就合成它们
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
        // 如果在参数初始化之前以某种方式调用它, 则强制它存在.
        if (parameters == null) {
            privateGetParameters();
        }
        return hasRealParameterData;
    }

    private transient volatile boolean hasRealParameterData;
    private transient volatile Parameter[] parameters;

    private native Parameter[] getParameters0();

    native byte[] getTypeAnnotationBytes0();

    // 反射访问需要
    byte[] getTypeAnnotationBytes() {
        return getTypeAnnotationBytes0();
    }

    /**
     * 返回 {@code Class} 对象的数组, 这些对象表示声明由此对象表示的基础可执行对象抛出的异常类型.
     * 如果可执行对象在其{@code throws}子句中声明没有异常, 则返回长度为0的数组.
     * <p>
     * Wttch: 即除非运行时异常在 {@code throws} 子句中声明, 否则该运行时异常是无法通过该函数获取到.
     *
     * @return 表示声明由此对象表示的基础可执行对象抛出的异常类型
     */
    public abstract Class<?>[] getExceptionTypes();

    /**
     * 返回 {@code Type} 对象的数组, 这些对象表示声明由此可执行对象引发的异常.
     * 如果底层可执行对象在其 {@code throws} 子句中未声明异常, 则返回长度为0的数组.
     *
     * <p>如果异常类型是类型变量或参数化类型, 则会创建它. 否则, 它就解决了.
     *
     * @return 表示声明由此可执行对象引发的异常
     * @throws GenericSignatureFormatError         如果泛型方法签名不符合<cite>JVM 规范</cite>中指定的格式
     * @throws TypeNotPresentException             如果底层可执行对象的任何参数类型引用了不存在的类型声明
     * @throws MalformedParameterizedTypeException 如果任何底层可执行对象的参数类型引用了因任何原因无法实例化的参数化类型
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
     * 返回描述此 {@code Executable} 的字符串, 包括任何类型参数.
     *
     * @return 描述此 {@code Executable} 的字符串, 包括任何类型参数
     */
    public abstract String toGenericString();

    /**
     * 如果声明此可执行对象采用可变数量的参数, 则返回 {@code true};
     * 否则返回 {@code false}.
     *
     * @return {@code true} 声明此可执行对象采用可变数量的参数
     */
    public boolean isVarArgs() {
        return (getModifiers() & Modifier.VARARGS) != 0;
    }

    /**
     * 如果此可执行对象是合成构造的, 则返回 {@code true};
     * 否则返回 {@code false}.
     *
     * @return t此可执行对象如<cite>Java 语言规范</cite>中描述的那样是合成构造的
     * @jls 13.1 The Form of a Binary
     */
    @Override
    public boolean isSynthetic() {
        return Modifier.isSynthetic(getModifiers());
    }

    /**
     * 返回 {@code Annotation} 数组的数组, 这些数组表示由此对象表示的 {@code Executable} 的声明顺序的形式参数的注解.
     * 合成和强制参数(参见下面的说明), 例如内部类构造函数的外部 "this" 参数将在返回的数组中表示.
     * 如果可执行对象没有参数(意味着没有正式参数, 没有合成参数, 也没有强制参数), 则返回零长度数组.
     * 如果{@code Executable}具有一个或多个参数, 则为每个没有注解的参数返回长度为零的嵌套数组.
     * 返回的数组中包含的注解对象是可序列化的.
     * 此方法的调用者可以自由修改返回的数组; 它对返回给其他调用者的数组没有影响.
     * <p>
     * 编译器可以在源("强制")中强制添加隐式声明的额外参数, 以及在源("合成")中未对方法的参数列表
     * 进行隐式或显式声明的参数. 有关更多信息, 请参阅 {@link java.lang.reflect.Parameter}.
     *
     * @return 表示由此对象表示的 {@code Executable} 的声明顺序的形式参数的注解数组
     * @see java.lang.reflect.Parameter
     * @see java.lang.reflect.Parameter#getAnnotations
     */
    public abstract Annotation[][] getParameterAnnotations();

    /**
     * 共享获取参数注解
     *
     * @param parameterTypes       参数类型
     * @param parameterAnnotations 参数注解: 此数组包含了二维数组的两个维度大小和注解类在常量池中的索引位置
     * @return 注解的二维数组, 第一维表示参数声明的先后顺序
     */
    Annotation[][] sharedGetParameterAnnotations(Class<?>[] parameterTypes,
                                                 byte[] parameterAnnotations) {
        int numParameters = parameterTypes.length;
        // 没有参数注解类型, 直接返回
        if (parameterAnnotations == null)
            return new Annotation[numParameters][0];

        // 解析参数注解
        Annotation[][] result = parseParameterAnnotations(parameterAnnotations);

        if (result.length != numParameters)
            // 处理参数注解数组长度和参数长度不匹配
            handleParameterNumberMismatch(result.length, numParameters);
        return result;
    }

    /**
     * 处理参数注解数组长度和参数长度不匹配
     *
     * @param resultLength  参数注解数组长度
     * @param numParameters 参数长度
     */
    abstract void handleParameterNumberMismatch(int resultLength, int numParameters);

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
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
    @Override
    public Annotation[] getDeclaredAnnotations() {
        return AnnotationParser.toArray(declaredAnnotations());
    }

    private transient Map<Class<? extends Annotation>, Annotation> declaredAnnotations;

    /**
     * 获取所有的注解
     *
     * @return 所有注解的映射, 即注解的类到该类所有注解的映射
     */
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
     * 返回 {@code AnnotatedType} 对象, 该对象表示使用类型来指定此可执行对象表示的方法/构造函数的返回类型.
     * <p>
     * 如果此 {@code Executable} 对象表示构造函数, 则 {@code AnnotatedType} 对象表示构造对象的类型.
     * <p>
     * 如果此 {@code Executable} 对象表示方法, 则 {@code AnnotatedType} 对象表示使用类型来指定方法的返回类型.
     *
     * @return 表示使用类型来指定此 {@code Executable} 表示的方法/构造函数的返回类型
     */
    public abstract AnnotatedType getAnnotatedReturnType();

    /* Helper 用于可执行对象的子类.
     *
     * 返回一个 AnnotatedType 对象, 该对象表示使用类型来指定此可执行对象所表示的方法/构造函数的返回类型.
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
     * 返回 {@code AnnotatedType} 对象, 该对象表示使用类型类指定可执行对象所表示的
     * 方法/构造函数的接受器类型. 仅当方法/构造函数具有<em>接收器参数</em>(JLS 8.4.1)时,
     * 方法/构造函数的接受器类型才可用.
     * <p>
     * 如果此 {@code Executable} 对象表示没有 receiver 参数的构造函数或实例方法,
     * 或者其类型没有的 receiver 参数, 则返回值是表示元素的 {@code AnnotatedType} 对象没有注解.
     * <p>
     * 如果此 {@code Executable} 对象表示静态方法, 则返回值为 null.
     *
     * @return 一个 {@code AnnotatedType} 对象表示使用类型类指定可执行对象所表示的方法/构造函数的接受器类
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
     * 返回 {@code AnnotatedType} 对象数组, 这些对象表示当前使用类型来指定
     * 由此可执行对象表示的方法/构造函数的形参类型. 数组中的顺序对应于方法/构造函数声明中
     * 形参类型的顺序.
     * <p>
     * 如果方法/构造函数没有声明参数, 则返回长度为 0 的数组.
     *
     * @return 一个对象数组, 表示由此 {@code Executable} 表示的方法或构造函数的形式参数类型
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
     * 返回 {@code AnnotatedType} 对象的数组, 这些对象表示使用类型来指定由此可执行对象表示的
     * 方法/构造函数的声明的异常. 数组中对象的顺序对应于方法/构造函数声明中的异常类型的顺序.
     * <p>
     * 如果方法/构造函数声明没有异常, 则返回长度为0的数组.
     *
     * @return 一个对象数组, 表示由此 {@code Executable} 表示的方法或构造函数的声明异常
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
