// Copyright 2000-2017 JetBrains s.r.o.
// Use of this source code is governed by the Apache 2.0 license that can be
// found in the LICENSE file.
package nebula.intellij.util;

import java.util.function.Function;

import nebula.intellij.util.annotations.NotNull;
import nebula.intellij.util.annotations.Nullable;

public class Pair<A, B> {
	public final A first;
	public final B second;

	@NotNull
	public static <A, B> Pair<A, B> create(A first, B second) {
		// noinspection DontUsePairConstructor
		return new Pair<A, B>(first, second);
	}

	@NotNull
	public static <A, B> NonNull<A, B> createNonNull(@NotNull A first, @NotNull B second) {
		return new NonNull<A, B>(first, second);
	}

	@NotNull
	public static <A, B> Pair<A, B> pair(A first, B second) {
		// noinspection DontUsePairConstructor
		return new Pair<A, B>(first, second);
	}

	@NotNull
	public static <A, B> Function<A, Pair<A, B>> createFunction(final B value) {
		return new Function<A, Pair<A, B>>() {
			public Pair<A, B> apply(A a) {
				return create(a, value);
			}
		};
	}

	public static <T> T getFirst(@Nullable Pair<T, ?> pair) {
		return pair != null ? pair.first : null;
	}

	public static <T> T getSecond(@Nullable Pair<?, T> pair) {
		return pair != null ? pair.second : null;
	}

	@SuppressWarnings({ "rawtypes" })
	private static final Pair EMPTY = create(null, null);

	@SuppressWarnings("unchecked")
	public static <A, B> Pair<A, B> empty() {
		return EMPTY;
	}

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public final A getFirst() {
		return first;
	}

	public final B getSecond() {
		return second;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public final boolean equals(Object o) {
		return o instanceof Pair && Comparing.equal(first, ((Pair) o).first) && Comparing.equal(second, ((Pair) o).second);
	}

	@Override
	public int hashCode() {
		int result = first != null ? first.hashCode() : 0;
		result = 31 * result + (second != null ? second.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "<" + first + "," + second + ">";
	}

	public static class NonNull<A, B> extends Pair<A, B> {
		public NonNull(@NotNull A first, @NotNull B second) {
			super(first, second);
		}
	}
}