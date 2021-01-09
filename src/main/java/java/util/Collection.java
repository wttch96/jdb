package java.util;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <i>集合结构层次</i>的根接口. 集合包含一组对象, 我们称之为<i>元素</i>.
 * 一些元素允许重复的元素, 另一些不允许. 一些是有序的, 另一些则乱序. JDK 不提供任何该接口的 <i>直接</i>实现: 它提供了更多特定子接口的实现,
 * 例如<tt>Set</tt>和<tt>List</tt>. 该接口通常用于传递集合并在需要最大通用性的地方操纵它们.
 *
 * <p><i>包</i>或<i>多集</i>(可能包含重复元素的无序集合)应直接实现此接口.
 *
 * <p>所有通用的<tt>Collection</tt>实现类(通常通过其子接口之一间接实现<tt>Collection</tt>)应提供两个"标准"构造函数: void(无参数)构造函数,
 * 它将创建一个空集合, 以及一个构造函数, 该构造函数具有一个类型为<tt>Collection</tt>的单个参数, 该参数将创建一个新集合, 其元素与参数相同. 实际上,
 * 后一个构造函数允许用户复制任何集合, 从而生成所需实现类型的等效集合. 无法强制执行此约定(因为接口不能包含构造函数), 但是Java平台库中的所有通用<tt>Collection</tt>实现都可以遵循.
 *
 * <p>此接口中包含的"破坏性"方法(即, 修改其操作的集合的方法)被指定为抛出<tt>UnsupportedOperationException</tt>(如果此集合不支持该操作).
 * 在这种情况下, 如果调用对集合没有影响, 则可能但不要求这些方法引发<tt>UnsupportedOperationException</tt>. 例如, 如果要添加的集合为空,
 * 则对不可修改的集合调用{@link #addAll(Collection)}方法可能(但并非必须)引发异常.
 *
 * <p><a name="optional-restrictions">一些集合实现对它们可能包含的元素有限制.</a>例如, 某些实现禁止使用null元素,
 * 而某些实现则对其元素的类型进行了限制. 尝试添加不合格元素会引发未经检查的异常, 通常为<tt>NullPointerException</tt>或<tt>ClassCastException</tt>.
 * 尝试查询不合格元素的存在可能会引发异常, 或者可能仅返回false; 否则, 可能会返回false. 一些实现将表现出前一种行为, 而某些将表现出后者. 更一般地, 尝试对不合格元素进行操作,
 * 该操作的完成不会导致将不合格元素插入集合中, 这可能会导致异常或成功实现, 具体取决于实现方式. 此类异常在此接口的规范中标记为"可选".
 *
 * <p>由每个集合决定自己的同步策略. 在实现没有更强有力的保证的情况下, 未定义的行为可能是由于调用另一个线程正在变异的集合上的任何方法而导致的;
 * 这包括直接调用, 将集合传递给可能执行调用的方法, 以及使用现有的迭代器检查集合.
 *
 * <p> Collections Framework接口中的许多方法都是根据{@link Object#equals(Object) equals}方法定义的.
 * 例如, {@link #contains(Object) contains(Object o)}方法的规范说:"当且仅当此集合包含至少一个元素<tt>e</tt>时,
 * 返回<tt>true</tt>, 这样<tt>(o == null?e == null:o.equals(e))</tt>." 本规范不应<i>理解为暗示使用非空参数<tt>o</tt>
 * 调用<tt>Collection.contains</tt>将导致<tt>o.equals(e)</tt>被任何元素<tt>e</tt>调用. 实现可以自由地进行优化,
 * 从而避免了<tt>equals</tt>调用, 例如, 首先比较两个元素的哈希码. ({@link Object#hashCode()}规范保证了具有不相等哈希码的两个对象不能相等.)
 * 更一般地, 各种Collections Framework接口的实现都可以自由利用基础{@link Object}方法, 只要实现者认为合适即可.
 *
 * <p>一些执行集合递归遍历的集合操作可能会失败, 但自引用实例的例外情况是, 集合直接或间接包含自身. 这包括{@code clone()},
 * {@code equals()}, {@code hashCode()}和{@code toString()}方法. 实现可以有选择地处理自引用场景, 但是大多数当前实现不这样做.
 *
 * <p>此接口是<a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @param <E> the type of elements in this collection
 * @author Josh Bloch
 * @author Neal Gafter
 * @implSpec 默认方法实现(继承或以其他方式)不应用任何同步协议. 如果{@code Collection}实现具有特定的同步协议, 则它必须覆盖默认实现以应用该协议.
 * @see Set
 * @see List
 * @see Map
 * @see SortedSet
 * @see SortedMap
 * @see HashSet
 * @see TreeSet
 * @see ArrayList
 * @see LinkedList
 * @see Vector
 * @see Collections
 * @see Arrays
 * @see AbstractCollection
 * @since 1.2
 */

public interface Collection<E> extends Iterable<E> {
  // 查询操作

  /**
   * 返回此集合中的元素数. 如果此集合包含多于<tt>Integer.MAX_VALUE</tt>元素, 则返回<tt>Integer.MAX_VALUE</tt>.
   *
   * @return the number of elements in this collection
   */
  int size();

  /**
   * 如果此集合不包含任何元素, 则返回<tt>true</tt>.
   *
   * @return <tt>true</tt> if this collection contains no elements
   */
  boolean isEmpty();

  /**
   * 如果此集合包含指定的元素, 则返回<tt>true</tt>. 更正式地讲, 当且仅当此集合包含至少一个元素<tt>e</tt>从而使
   * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
   *
   * @param o 要检查其是否存在于此集合中的元素
   * @return 如果此集合包含指定的则返回 <tt>true</tt> element
   * @throws ClassCastException   如果指定元素的类型与此集合不兼容(<a href="#optional-restrictions">可选</a>)
   * @throws NullPointerException 如果指定的元素为null, 并且此集合不允许使用null元素(<a href="#optional-restrictions">可选</a>)
   */
  boolean contains(Object o);

  /**
   * 返回对此集合中的元素进行迭代的迭代器. 没有关于元素返回顺序的保证(除非此集合是某个提供保证的类的实例)            .
   *
   * @return an <tt>Iterator</tt> over the elements in this collection
   */
  Iterator<E> iterator();

  /**
   * 返回一个包含此集合中所有元素的数组. 如果此集合对它的迭代器返回其元素的顺序进行了任何保证, 则此方法必须按相同的顺序返回元素.
   *
   * <p>返回的数组将是"安全的", 因为此集合不会维护对其的引用. (换句话说, 即使此集合由数组支持, 此方法也必须分配一个新数组).
   * 因此, 调用者可以自由修改返回的数组.
   *
   * <p>此方法充当基于数组的API和基于集合的API之间的桥梁.
   *
   * @return an array containing all of the elements in this collection
   */
  Object[] toArray();

  /**
   * 返回一个包含此集合中所有元素的数组; 返回数组的运行时类型是指定数组的运行时类型. 如果集合适合指定的数组, 则将其返回. 否则, 将使用指定数组的运行时类型和此集合的大小分配一个新数组.
   *
   * <p>如果此集合适合指定的数组且有剩余空间(即, 数组比该集合具有更多的元素), 则紧接集合结束后的数组中的元素将设置为<tt>null</tt>.
   * (如果调用者知道此集合不包含任何<tt>null</tt>元素, 则此方法仅在确定该集合的长度时有用.)
   *
   * <p>如果此集合对其元素由其迭代器返回的顺序做出任何保证, 则此方法必须按相同顺序返回元素.
   *
   * <p>类似于{@link #toArray()}方法, 该方法充当基于数组的API和基于集合的API之间的桥梁.
   * 此外, 此方法允许对输出数组的运行时类型进行精确控制, 并且在某些情况下可以用于节省分配成本.
   *
   * <p>假设<tt>x</tt>是一个已知仅包含字符串的集合. 以下代码可用于将集合转储到新分配的<tt>String</tt>数组中:
   *
   * <pre>
   *     String[] y = x.toArray(new String[0]);</pre>
   * <p>
   * 请注意, <tt>toArray(new Object [0])</tt>在功能上与<tt> toArray()</tt>相同.
   *
   * @param <T> 包含集合的数组的运行时类型
   * @param a   如果此集合足够大, 则存储该集合的元素的数组; 否则, 将为此目的分配一个具有相同运行时类型的新数组.
   * @return 包含此集合中所有元素的数组
   * @throws ArrayStoreException  如果指定数组的运行时类型不是集合中每个元素的运行时类型的超类型
   * @throws NullPointerException 如果指定的数组为null
   */
  <T> T[] toArray(T[] a);

  // 修改操作

  /**
   * 确保此集合包含指定的元素(可选操作). 如果此集合由于调用而更改, 则返回<tt>true</tt>. (如果此集合不允许重复并且已经包含指定的元素,
   * 则返回<tt>false</tt>.)
   *
   * <p>支持此操作的集合可能会对可以添加到此集合的元素施加限制. 特别是, 某些集合将拒绝添加null元素, 而其他集合将对可能添加的元素类型施加限制.
   * 集合类应在其文档中明确指定对可以添加哪些元素的任何限制.
   *
   * <p>如果一个集合由于已经包含该元素以外的其他原因拒绝添加一个特定元素, 则它必须抛出一个异常(而不是返回false).
   * 这保留了不变, 即在此调用返回之后, 集合一定包含指定的元素.
   *
   * @param e 要确保其存在于此集合中的元素
   * @return <tt>true</tt> if this collection changed as a result of the
   * call
   * @throws UnsupportedOperationException 如果此集合不支持<tt>add</tt>操作
   * @throws ClassCastException            如果指定元素的类阻止将其添加到此集合中
   * @throws NullPointerException          如果指定的元素为null, 并且此集合不允许使用null元素
   * @throws IllegalArgumentException      如果元素的某些属性阻止将其添加到此集合中
   * @throws IllegalStateException         如果由于插入限制当前无法添加该元素
   */
  boolean add(E e);

  /**
   * 如果存在, 则从此集合中删除指定元素的单个实例(可选操作). 更正式地说, 删除元素 <tt>e</tt> 像这样
   * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, 如果此集合包含一个或多个此类元素.
   * 如果此集合包含指定的元素(或等效地, 如果此集合由于调用而更改)返回<tt>true</tt> .
   *
   * @param o 要从此集合中删除的元素(如果存在)
   * @return <tt>true</tt> if an element was removed as a result of this call
   * @throws ClassCastException            if the type of the specified element is incompatible with
   *                                       this collection (<a href="#optional-restrictions">optional</a>)
   * @throws NullPointerException          if the specified element is null and this collection does
   *                                       not permit null elements (<a href="#optional-restrictions">optional</a>)
   * @throws UnsupportedOperationException if the <tt>remove</tt> operation is not supported by this
   *                                       collection
   */
  boolean remove(Object o);

  // 批量操作

  /**
   * 如果此集合包含指定集合中的所有元素, 则返回<tt>true</tt>.
   *
   * @param c collection to be checked for containment in this collection
   * @return <tt>true</tt> if this collection contains all of the elements
   * in the specified collection
   * @throws ClassCastException   if the types of one or more elements in the specified collection
   *                              are incompatible with this collection (<a href="#optional-restrictions">optional</a>)
   * @throws NullPointerException if the specified collection contains one or more null elements and
   *                              this collection does not permit null elements (<a
   *                              href="#optional-restrictions">optional</a>), or if the specified
   *                              collection is null.
   * @see #contains(Object)
   */
  boolean containsAll(Collection<?> c);

  /**
   * Adds all of the elements in the specified collection to this collection (optional operation).
   * The behavior of this operation is undefined if the specified collection is modified while the
   * operation is in progress. (This implies that the behavior of this call is undefined if the
   * specified collection is this collection, and this collection is nonempty.)
   *
   * @param c collection containing elements to be added to this collection
   * @return <tt>true</tt> if this collection changed as a result of the call
   * @throws UnsupportedOperationException if the <tt>addAll</tt> operation is not supported by this
   *                                       collection
   * @throws ClassCastException            if the class of an element of the specified collection
   *                                       prevents it from being added to this collection
   * @throws NullPointerException          if the specified collection contains a null element and
   *                                       this collection does not permit null elements, or if the
   *                                       specified collection is null
   * @throws IllegalArgumentException      if some property of an element of the specified
   *                                       collection prevents it from being added to this
   *                                       collection
   * @throws IllegalStateException         if not all the elements can be added at this time due to
   *                                       insertion restrictions
   * @see #add(Object)
   */
  boolean addAll(Collection<? extends E> c);

  /**
   * Removes all of this collection's elements that are also contained in the specified collection
   * (optional operation).  After this call returns, this collection will contain no elements in
   * common with the specified collection.
   *
   * @param c collection containing elements to be removed from this collection
   * @return <tt>true</tt> if this collection changed as a result of the
   * call
   * @throws UnsupportedOperationException if the <tt>removeAll</tt> method is not supported by this
   *                                       collection
   * @throws ClassCastException            if the types of one or more elements in this collection
   *                                       are incompatible with the specified collection (<a
   *                                       href="#optional-restrictions">optional</a>)
   * @throws NullPointerException          if this collection contains one or more null elements and
   *                                       the specified collection does not support null elements
   *                                       (<a href="#optional-restrictions">optional</a>), or if
   *                                       the specified collection is null
   * @see #remove(Object)
   * @see #contains(Object)
   */
  boolean removeAll(Collection<?> c);

  /**
   * 删除此集合中满足给定断言的所有元素. 在迭代过程中或由断言引发的错误或运行时异常将中继给调用者.
   *
   * @param filter a predicate which returns {@code true} for elements to be removed
   * @return {@code true} if any elements were removed
   * @throws NullPointerException          if the specified filter is null
   * @throws UnsupportedOperationException if elements cannot be removed from this collection.
   *                                       Implementations may throw this exception if a matching
   *                                       element cannot be removed or if, in general, removal is
   *                                       not supported.
   * @implSpec The default implementation traverses all elements of the collection using its {@link
   * #iterator}.  Each matching element is removed using {@link Iterator#remove()}.  If the
   * collection's iterator does not support removal then an {@code UnsupportedOperationException}
   * will be thrown on the first matching element.
   * @since 1.8
   */
  default boolean removeIf(Predicate<? super E> filter) {
    Objects.requireNonNull(filter);
    boolean removed = false;
    final Iterator<E> each = iterator();
    while (each.hasNext()) {
      if (filter.test(each.next())) {
        each.remove();
        removed = true;
      }
    }
    return removed;
  }

  /**
   * 仅保留此集合中包含在指定集合中的元素(可选操作). 换句话说, 从此集合中删除所有未包含在指定集合中的元素.
   *
   * @param c collection containing elements to be retained in this collection
   * @return <tt>true</tt> if this collection changed as a result of the call
   * @throws UnsupportedOperationException if the <tt>retainAll</tt> operation is not supported by
   *                                       this collection
   * @throws ClassCastException            if the types of one or more elements in this collection
   *                                       are incompatible with the specified collection (<a
   *                                       href="#optional-restrictions">optional</a>)
   * @throws NullPointerException          if this collection contains one or more null elements and
   *                                       the specified collection does not permit null elements
   *                                       (<a href="#optional-restrictions">optional</a>), or if
   *                                       the specified collection is null
   * @see #remove(Object)
   * @see #contains(Object)
   */
  boolean retainAll(Collection<?> c);

  /**
   * 从此集合中删除所有元素(可选操作). 此方法返回后, 集合将为空.
   *
   * @throws UnsupportedOperationException 如果此集合不支持<tt>clear</tt>操作
   */
  void clear();

  // 比较和哈希

  /**
   * 比较指定对象与此集合的相等性.
   * <p>
   * 尽管Collection接口没有为Object.equals的常规合同增加任何规定, 但是"直接"实现Collection接口(换句话说,
   * 创建一个不是Collection而不是Set或List的类)的程序员必须小心如果他们选择覆盖Object.equals. 不必这样做, 最简单的方法是依靠Object的实现,
   * 但是实现者可能希望实现"值比较"来代替默认的"引用比较". (List和Set接口要求进行这种值比较.)
   * <p>
   * Object.equals方法的常规协定规定, equals必须是对称的(换句话说, 当且仅当b.equals(a)时才是a.equals(b)).
   * List.equals和Set.equals的合同规定, 列表仅等于其他列表, 并设置为其他集合. 因此, 当将此集合与任何列表或集合进行比较时,
   * 既不实现List也不实现Set接口的集合类的自定义equals方法必须返回false.(通过相同的逻辑, 不可能编写正确实现Set和List接口的类.)
   *
   * @param o 要与此对象进行相等性比较的对象
   * @return <tt>true</tt> if the specified object is equal to this
   * collection
   * @see Object#equals(Object)
   * @see Set#equals(Object)
   * @see List#equals(Object)
   */
  boolean equals(Object o);

  /**
   * Returns the hash code value for this collection.  While the
   * <tt>Collection</tt> interface adds no stipulations to the general
   * contract for the <tt>Object.hashCode</tt> method, programmers should take note that any class
   * that overrides the <tt>Object.equals</tt> method must also override the
   * <tt>Object.hashCode</tt> method in order to satisfy the general contract for the
   * <tt>Object.hashCode</tt> method. In particular, <tt>c1.equals(c2)</tt> implies that
   * <tt>c1.hashCode()==c2.hashCode()</tt>.
   *
   * @return the hash code value for this collection
   * @see Object#hashCode()
   * @see Object#equals(Object)
   */
  int hashCode();

  /**
   * Creates a {@link Spliterator} over the elements in this collection.
   * <p>
   * Implementations should document characteristic values reported by the spliterator.  Such
   * characteristic values are not required to be reported if the spliterator reports {@link
   * Spliterator#SIZED} and this collection contains no elements.
   *
   * <p>The default implementation should be overridden by subclasses that
   * can return a more efficient spliterator.  In order to preserve expected laziness behavior for
   * the {@link #stream()} and {@link #parallelStream()}} methods, spliterators should either have
   * the characteristic of {@code IMMUTABLE} or {@code CONCURRENT}, or be
   * <em><a href="Spliterator.html#binding">late-binding</a></em>.
   * If none of these is practical, the overriding class should describe the spliterator's
   * documented policy of binding and structural interference, and should override the {@link
   * #stream()} and {@link #parallelStream()} methods to create streams using a {@code Supplier} of
   * the spliterator, as in:
   * <pre>{@code
   *     Stream<E> s = StreamSupport.stream(() -> spliterator(), spliteratorCharacteristics)
   * }</pre>
   * <p>These requirements ensure that streams produced by the
   * {@link #stream()} and {@link #parallelStream()} methods will reflect the contents of the
   * collection as of initiation of the terminal stream operation.
   *
   * @return a {@code Spliterator} over the elements in this collection
   * @implSpec The default implementation creates a
   * <em><a href="Spliterator.html#binding">late-binding</a></em> spliterator
   * from the collections's {@code Iterator}.  The spliterator inherits the
   * <em>fail-fast</em> properties of the collection's iterator.
   * <p>
   * The created {@code Spliterator} reports {@link Spliterator#SIZED}.
   * @implNote The created {@code Spliterator} additionally reports {@link Spliterator#SUBSIZED}.
   *
   * <p>If a spliterator covers no elements then the reporting of additional
   * characteristic values, beyond that of {@code SIZED} and {@code SUBSIZED}, does not aid clients
   * to control, specialize or simplify computation. However, this does enable shared use of an
   * immutable and empty spliterator instance (see {@link Spliterators#emptySpliterator()}) for
   * empty collections, and enables clients to determine if such a spliterator covers no elements.
   * @since 1.8
   */
  @Override
  default Spliterator<E> spliterator() {
    return Spliterators.spliterator(this, 0);
  }

  /**
   * Returns a sequential {@code Stream} with this collection as its source.
   *
   * <p>This method should be overridden when the {@link #spliterator()}
   * method cannot return a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT}, or
   * <em>late-binding</em>. (See {@link #spliterator()} for details.)
   *
   * @return a sequential {@code Stream} over the elements in this collection
   * @implSpec The default implementation creates a sequential {@code Stream} from the collection's
   * {@code Spliterator}.
   * @since 1.8
   */
  default Stream<E> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  /**
   * Returns a possibly parallel {@code Stream} with this collection as its source.  It is allowable
   * for this method to return a sequential stream.
   *
   * <p>This method should be overridden when the {@link #spliterator()}
   * method cannot return a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT}, or
   * <em>late-binding</em>. (See {@link #spliterator()} for details.)
   *
   * @return a possibly parallel {@code Stream} over the elements in this collection
   * @implSpec The default implementation creates a parallel {@code Stream} from the collection's
   * {@code Spliterator}.
   * @since 1.8
   */
  default Stream<E> parallelStream() {
    return StreamSupport.stream(spliterator(), true);
  }
}
