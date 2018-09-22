package nebula.intellij.util.name;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import nebula.intellij.util.name.NameUtil;

public class NameUtilTest {

	@Test
	public void testNameToWordsLowerCase() {
		assertEquals("test name to words lower case", String.join(" ", NameUtil.nameToWordsLowerCase("testNameToWordsLowerCase")));
	}

	@Test
	public void testNameToWords() {
		assertEquals("test Name To Words Lower Case", String.join(" ", NameUtil.nameToWords("testNameToWordsLowerCase")));
	}

//	@Test
//	public void testBuildRegexpStringIntBooleanBoolean() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testBuildRegexpStringIntBooleanBooleanBooleanBoolean() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testSplitWords() {
		assertEquals("test Name To Words Lower Case", String.join(" ", NameUtil.splitNameIntoWords("testNameToWordsLowerCase")));
	}

	@Test
	public void testSplitNameIntoWords() {

		assertEquals("test Name To Words Lower Case", String.join(" ", NameUtil.splitNameIntoWords("testNameToWordsLowerCase")));
	}

	@Test
	public void testGetSuggestionsByName() {
		assertEquals("PREFIXNAMESSuffix,PREFIXNAMESSuffix",String.join(",", NameUtil.getSuggestionsByName("Name", "Prefix", "Suffix", true, true, true)));
		assertEquals("PrefixNAMESuffix,PrefixNAMESuffix",String.join(",", NameUtil.getSuggestionsByName("Name", "Prefix", "Suffix", true, true, false)));

		assertEquals("PREFIXNAMESSuffix,PREFIXNAMESSuffix",String.join(",", NameUtil.getSuggestionsByName("Name", "Prefix", "Suffix", true, false, true)));
		assertEquals("PrefixNAMESuffix,PrefixNAMESuffix",String.join(",", NameUtil.getSuggestionsByName("Name", "Prefix", "Suffix", true, false, false)));

		assertEquals("PrefixNamesSuffix,PrefixNamesSuffix",String.join(",", NameUtil.getSuggestionsByName("Name", "Prefix", "Suffix", false, true, true)));
		assertEquals("PrefixNameSuffix,PrefixNameSuffix",String.join(",", NameUtil.getSuggestionsByName("Name", "Prefix", "Suffix", false, true, false)));

		assertEquals("PrefixNamesSuffix,PrefixNamesSuffix",String.join(",", NameUtil.getSuggestionsByName("Name", "Prefix", "Suffix", false, false, true)));
		assertEquals("PrefixNameSuffix,PrefixNameSuffix",String.join(",", NameUtil.getSuggestionsByName("Name", "Prefix", "Suffix", false, false, false)));

	}

	@Test
	public void testNextWord() {
		assertEquals(4, NameUtil.nextWord("testNameToWordsLowerCase", 0));
		assertEquals(8, NameUtil.nextWord("testNameToWordsLowerCase", 4));
		assertEquals(15, NameUtil.nextWord("testNameToWordsLowerCase", 10));
	}

	@Test
	public void testIsWordStart() {
		assertEquals(true, NameUtil.isWordStart("testNameToWordsLowerCase", 0));
		assertEquals(true, NameUtil.isWordStart("testNameToWordsLowerCase", 4));
		assertEquals(true, NameUtil.isWordStart("testNameToWordsLowerCase", 10));
	}

	@Test
	public void testCapitalizeAndUnderscore() {
		assertEquals("TEST_NAME_TO_WORDS_LOWER_CASE", String.join(" ", NameUtil.capitalizeAndUnderscore("testNameToWordsLowerCase")));

	}

	  @Test
	  public void testSplitIntoWords1() {
	    assertSplitEquals(new String[]{"I", "Base"}, "IBase");
	  }

	  @Test
	  public void testSplitIntoWords2() {
	    assertSplitEquals(new String[]{"Order", "Index"}, "OrderIndex");
	  }

	  @Test
	  public void testSplitIntoWords3() {
	    assertSplitEquals(new String[]{"order", "Index"}, "orderIndex");
	  }

	  @Test
	  public void testSplitIntoWords4() {
	    assertSplitEquals(new String[]{"Order", "Index"}, "Order_Index");
	  }

	  @Test
	  public void testSplitIntoWords5() {
	    assertSplitEquals(new String[]{"ORDER", "INDEX"}, "ORDER_INDEX");
	  }

	  @Test
	  public void testSplitIntoWords6() {
	    assertSplitEquals(new String[]{"gg", "J"}, "ggJ");
	  }

	  private static void assertSplitEquals(String[] expected, String name) {
	    final String[] result = NameUtil.splitNameIntoWords(name);
	    assertEquals(Arrays.asList(expected).toString(), Arrays.asList(result).toString());
	  }
}
