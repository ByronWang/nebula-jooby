package com.intellij.openapi.util.text;

public class CharArrayUtil {

	public static char[] fromSequenceWithoutCopying(CharSequence text) {
		char[] chars = new char[text.length()];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = text.charAt(i);
		}
		return chars;
	}

}
