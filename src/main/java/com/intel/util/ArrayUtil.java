package com.intel.util;

import java.util.Collection;
import java.util.List;

public class ArrayUtil {

	public static String[] toStringArray(List<String> strings) {
		String[] a = new String[strings.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] =strings.get(i);
		}
		return a;
	}


}
