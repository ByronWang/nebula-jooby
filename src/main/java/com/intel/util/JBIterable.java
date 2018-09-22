// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intel.util;


import com.intel.annotations.NotNull;
import com.intel.annotations.Nullable;
import com.intellij.openapi.util.Ref;
import com.intellij.util.*;

import java.util.*;

/**
 * An in-house and immutable version of {@code com.google.common.collect.FluentIterable}
 * with some insights from Clojure. Added bonus is that the JBIterator instances are preserved
 * during most transformations, a feature employed by {@link JBTreeTraverser}.
 *
 * <p/>
 * The original JavaDoc ('FluentIterable' replaced by 'JBIterable'):
 * <p/>
 * {@code JBIterable} provides a rich interface for manipulating {@code Iterable} instances in a
 * chained fashion. A {@code JBIterable} can be created from an {@code Iterable}, or from a set
 * of elements. The following types of methods are provided on {@code JBIterable}:
 * <ul>
 * <li>chained methods which return a new {@code JBIterable} based in some way on the contents
 * of the current one (for example {@link #map})
 * <li>conversion methods which copy the {@code JBIterable}'s contents into a new collection or
 * array (for example {@link #toList})
 * <li>element extraction methods which facilitate the retrieval of certain elements (for example
 * {@link #last})
 * </ul>
 * <p/>
 * <p>Here is an example that merges the lists returned by two separate database calls, transforms
 * it by invoking {@code toString()} on each element, and returns the first 10 elements as a
 * {@code List}: <pre>   {@code
 *   JBIterable
 *       .from(database.getClientList())
 *       .filter(activeInLastMonth())
 *       .map(Functions.toStringFunction())
 *       .toList();}</pre>
 * <p/>
 * <p>Anything which can be done using {@code JBIterable} could be done in a different fashion
 * (often with {@code Iterables}), however the use of {@code JBIterable} makes many sets of
 * operations significantly more concise.
 *
 * @author Marcin Mikosik
 *
 * @noinspection unchecked
 */
public abstract class JBIterable<E> implements Iterable<E> {

  /**
   * a Collection, an Iterable, or a single object
   */
  final Object content;

  /**
   * Constructor for use by subclasses.
   */
  protected JBIterable() {
    content = this;
  }

  JBIterable(@NotNull Object content) {
    this.content = content;
  }

  /**
   * Lambda-friendly construction method.
   */
  @NotNull
  public static <E> JBIterable<E> create(@Nullable final Producer<? extends Iterator<E>> producer) {
    if (producer == null) return empty();
    return new JBIterable<E>() {
      @NotNull
      @Override
      public Iterator<E> iterator() {
        return producer.produce();
      }
    };
  }

  /**
   * Returns a {@code JBIterable} that wraps {@code iterable}, or {@code iterable} itself if it
   * is already a {@code JBIterable}.
   */
  @NotNull
  public static <E> JBIterable<E> from(@Nullable Iterable<? extends E> iterable) {
    if (iterable == null || iterable == EMPTY) return empty();
    if (iterable instanceof JBIterable) return (JBIterable<E>)iterable;
    if (iterable instanceof Collection && ((Collection)iterable).isEmpty()) return empty();
    return new Multi(iterable);
  }

  private static final class Multi<E> extends JBIterable<E> {
    Multi(Iterable<? extends E> iterable) { super(iterable);}

    @Override
    public Iterator<E> iterator() {
      return JBIterator.from(((Iterable<E>)content).iterator());
    }
  }

  /**
   * Returns a {@code JBIterable} containing {@code elements} in the specified order.
   */
  @NotNull
  public static <E> JBIterable<E> of(@Nullable E... elements) {
    return elements == null || elements.length == 0 ? JBIterable.<E>empty() : from(ContainerUtilRt.newArrayList(elements));
  }

  private static final JBIterable EMPTY = new Empty();

  private static final class Empty extends JBIterable {
    @Override
    public Iterator iterator() {
      return new Iterator() {
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object next() {
			// TODO Auto-generated method stub
			return null;
		}
	};
    }
  }

  @NotNull
  public static <E> JBIterable<E> empty() {
    return (JBIterable<E>)EMPTY;
  }


  /**
   * Returns iterator, useful for graph traversal.
   *
   * @see TreeTraversal.TracingIt
   */
  @NotNull
  public <T extends Iterator<E>> T typedIterator() {
    return (T)iterator();
  }

//  public final boolean processEach(@NotNull Processor<E> processor) {
//    return ContainerUtil.process(this, processor);
//  }

  public final void consumeEach(@NotNull Consumer<? super E> consumer) {
    for (E e : this) {
      consumer.consume(e);
    }
  }


}
