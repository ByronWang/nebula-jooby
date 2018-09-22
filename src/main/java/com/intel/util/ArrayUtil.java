package com.intel.util;

import java.util.Collection;
import java.util.List;

public class ArrayUtil {

	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	public static final byte[] EMPTY_BYTE_ARRAY = null;

	public static String[] toStringArray(List<String> strings) {
		String[] a = new String[strings.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] =strings.get(i);
		}
		return a;
	}

	public static int[] realloc(int[] data, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> T[] mergeCollections(Collection<? extends T> c1, Collection<? extends T> c2, ArrayFactory<T> factory) {
		// TODO Auto-generated method stub
		return null;
	}

}
