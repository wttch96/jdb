package java.lang.ref;

/**
 * 最终引用, 用于最终实现.
 * 既: final 标识的类
 */
class FinalReference<T> extends Reference<T> {

    public FinalReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
    }
}
