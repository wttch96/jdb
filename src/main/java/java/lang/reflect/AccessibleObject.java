package java.lang.reflect;

import java.security.AccessController;

import sun.reflect.Reflection;
import sun.reflect.ReflectionFactory;

import java.lang.annotation.Annotation;

/**
 * AccessibleObject 类是 Field, Method 和 Constructor 的基类.
 * 它提供了将反射对象标记为在使用是禁止默认的 java 语言访问控制检查的能力.
 * 当使用 Field, Method, Constructor 设置或获取字段, 调用方法或创建和初始化类的新实例时,
 * 将分别执行访问检查 --- public, default(package), protected, private 成员.
 *
 * <p>在反射对象中设置 {@code accessible} 标志允许具有足够权限的复杂应用程序
 * (例如Java对象序列化或其他持久性机制)以通常被禁止的方式操作对象.
 *
 * <p>默认情况下, 反射对象 <em>不</em> 可访问.
 *
 * @see Field
 * @see Method
 * @see Constructor
 * @see ReflectPermission
 * @since 1.2
 */
public class AccessibleObject implements AnnotatedElement {

    /**
     * Permission 对象, 用于检查客户端是否具有权限来阻止 java 语言访问控制检查.
     */
    static final private java.security.Permission ACCESS_PERMISSION =
            new ReflectPermission("suppressAccessChecks");

    /**
     * 通过单个安全检查设置对象数组的 {@code accessible} 标志的便捷方法(为了提高效率).
     *
     * <p>首先, 这里有一个安全管理器, 它的 {@code checkPermission} 将使用
     * {@code ReflectPermission("suppressAccessChecks")} 权限被调用.
     *
     * <p>如果 {@code flag} 为 {@code true} 但是可能无法修改输入 {@code array}
     * 中任何元素的可访问性(例如, 如果元素是{@link java.lang.Class} 的 {@link Constructor} 对象)
     * 将抛出一个 {@code SecurityException} 异常. 如果出现这样的异常事件,
     * 引发这个异常的元素之前(不包含)的数组元素将设置这些元素的可访问性为 {@code flag};
     * 之后的元素(包含引发异常的元素)的元素可访问性不变.
     *
     * @param array AccessibleObject 对象数组
     * @param flag  给每一个对象的新的 {@code accessible} 标志值
     * @throws SecurityException 如果请求被拒绝.
     * @see SecurityManager#checkPermission
     * @see java.lang.RuntimePermission
     */
    public static void setAccessible(AccessibleObject[] array, boolean flag)
            throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPermission(ACCESS_PERMISSION);
        for (int i = 0; i < array.length; i++) {
            setAccessible0(array[i], flag);
        }
    }

    /**
     * 设置这个对象的 {@code accessible} 标志为给定的布尔值.
     * 值 {@code true} 表示反射对象在使用时应禁止 Java 语言访问检查.
     * 值 {@code false} 表示反射对象应强制执行 Java 语言访问检查.
     *
     * <p>首先, 这里有一个安全管理器, 它的 {@code checkPermission} 将使用
     * {@code ReflectPermission("suppressAccessChecks")} 权限被调用.
     *
     * <p>如果 {@code flag} 为 {@code true} 但是可能无法修改元素的可访问性
     * (例如, 如果元素是{@link java.lang.Class} 的 {@link Constructor} 对象)
     * 将抛出一个 {@code SecurityException} 异常.
     *
     * @param flag {@code accessible} 标志的新值
     * @throws SecurityException 请求被拒绝.
     * @see SecurityManager#checkPermission
     * @see java.lang.RuntimePermission
     */
    public void setAccessible(boolean flag) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPermission(ACCESS_PERMISSION);
        setAccessible0(this, flag);
    }

    /* 检查你是否暴露了 java.lang.Class.<init> 或者 java.lang.Class 中的敏感字段 */
    private static void setAccessible0(AccessibleObject obj, boolean flag)
            throws SecurityException {
        if (obj instanceof Constructor && flag) {
            Constructor<?> c = (Constructor<?>) obj;
            if (c.getDeclaringClass() == Class.class) {
                throw new SecurityException("Cannot make a java.lang.Class" +
                        " constructor accessible");
            }
        }
        obj.override = flag;
    }

    /**
     * 获取这个对象 {@code accessible} 标志的值.
     *
     * @return 这个对象 {@code accessible} 标志的值
     */
    public boolean isAccessible() {
        return override;
    }

    /**
     * 构造器: 仅仅在 JVM 中使用.
     */
    protected AccessibleObject() {
    }

    // 指示此对象是否覆盖语言级访问检查. 初始化是 "false". Field, Method, Constructor 可以使用该字段.
    //
    // 注意: 出于安全考虑, 此字段不该在此包外访问.
    boolean override;

    // 子类用于创建字段, 方法和构造函数访问器的反射工厂.
    // 请注意, 这在引导过程中很早就会调用.
    static final ReflectionFactory reflectionFactory =
            AccessController.doPrivileged(
                    new sun.reflect.ReflectionFactory.GetReflectionFactoryAction());

    /**
     * @throws NullPointerException {@inheritDoc}
     * @since 1.5
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        throw new AssertionError("All subclasses should override this method");
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException {@inheritDoc}
     * @since 1.5
     */
    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return AnnotatedElement.super.isAnnotationPresent(annotationClass);
    }

    /**
     * @throws NullPointerException {@inheritDoc}
     * @since 1.8
     */
    @Override
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
        throw new AssertionError("All subclasses should override this method");
    }

    /**
     * @since 1.5
     */
    public Annotation[] getAnnotations() {
        return getDeclaredAnnotations();
    }

    /**
     * @throws NullPointerException {@inheritDoc}
     * @since 1.8
     */
    @Override
    public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
        // 仅继承类上的注释, 对于所有其他对象, getDeclaredAnnotation 与 getAnnotation 相同.
        return getAnnotation(annotationClass);
    }

    /**
     * @throws NullPointerException {@inheritDoc}
     * @since 1.8
     */
    @Override
    public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
        // 仅继承类上的注释, 对于所有其他对象, getDeclaredAnnotationsByType 与 getAnnotationsByType 相同.
        return getAnnotationsByType(annotationClass);
    }

    /**
     * @since 1.5
     */
    public Annotation[] getDeclaredAnnotations() {
        throw new AssertionError("All subclasses should override this method");
    }


    // Shared access checking logic.

    // For non-public members or members in package-private classes,
    // it is necessary to perform somewhat expensive security checks.
    // If the security check succeeds for a given class, it will
    // always succeed (it is not affected by the granting or revoking
    // of permissions); we speed up the check in the common case by
    // remembering the last Class for which the check succeeded.
    //
    // The simple security check for Constructor is to see if
    // the caller has already been seen, verified, and cached.
    // (See also Class.newInstance(), which uses a similar method.)
    //
    // A more complicated security check cache is needed for Method and Field
    // The cache can be either null (empty cache), a 2-array of {caller,target},
    // or a caller (with target implicitly equal to this.clazz).
    // In the 2-array case, the target is always different from the clazz.

    // 共享访问逻辑检查.
    //
    // 对于非公共成员或者包私有类中成员, 有必要执行稍微昂贵的安全检查.
    // 如果给定的类检查成功, 它将始终成功(它不受授予或撤销权限的影响);
    // 通过记住检查成功的最后一个类, 我们加快了常见情况下的检查
    volatile Object securityCheckCache;

    /**
     * 检查是否可以访问.
     * 快速检查.
     *
     * @param caller    调用者
     * @param clazz     要调用的类
     * @param obj       要调用的类中的字段, 方法 或者 构造器
     * @param modifiers 修饰类型 {@link Modifier}
     * @throws IllegalAccessException
     */
    void checkAccess(Class<?> caller, Class<?> clazz, Object obj, int modifiers)
            throws IllegalAccessException {
        if (caller == clazz) {
            // 快速检查
            // 可以访问
            return;
        }
        // 读, 原子化操作
        Object cache = securityCheckCache;
        Class<?> targetClass = clazz;
        if (obj != null
                && Modifier.isProtected(modifiers)
                && ((targetClass = obj.getClass()) != clazz)) {
            // 要同时满足 cache 中的两个类
            if (cache instanceof Class[]) {
                Class<?>[] cache2 = (Class<?>[]) cache;
                if (cache2[1] == targetClass &&
                        cache2[0] == caller) {
                    // 可以访问
                    return;
                }
                //(首先测试缓存[1]因为[1]的范围检查包含范围检查[0].)
            }
        } else if (cache == caller) {
            // 没有受保护类型的形式或者两个类一样.
            // 可访问
            return;
        }

        // 如果没有返回, 则通过慢速路径访问检查.
        slowCheckMemberAccess(caller, clazz, obj, modifiers, targetClass);
    }

    // 保持所有这些缓慢的东西脱节:

    /**
     * 慢速类型访问检查
     *
     * @param caller      调用者
     * @param clazz       被访问者
     * @param obj         要调用的类中的字段, 方法 或者 构造器
     * @param modifiers   修饰类型{@link Modifier}
     * @param targetClass 要检查的目标类(具体到上面的字段, 方法, 构造器等)
     * @throws IllegalAccessException
     */
    void slowCheckMemberAccess(Class<?> caller, Class<?> clazz, Object obj, int modifiers,
                               Class<?> targetClass)
            throws IllegalAccessException {
        Reflection.ensureMemberAccess(caller, clazz, obj, modifiers);

        // 访问成功, 更新缓存
        Object cache = ((targetClass == clazz)
                ? caller
                : new Class<?>[]{caller, targetClass});

        // Note:  The two cache elements are not volatile,
        // but they are effectively final.  The Java memory model
        // guarantees that the initializing stores for the cache
        // elements will occur before the volatile write.

        // 注意: 两个缓存元素不是易失性的, 但它们实际上是最终的.
        // Java内存模型保证缓存元素的初始化存储将在 volatile 写入之前发生.

        // 原子化写
        securityCheckCache = cache;
    }
}
