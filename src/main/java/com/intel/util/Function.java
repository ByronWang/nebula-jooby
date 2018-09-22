package com.intel.util;

public interface Function<T, R> extends java.util.function.Function<T, R> {

	R fun(T t);

	default R apply(T t) {
		return fun(t);
	}
}
