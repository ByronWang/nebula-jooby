package com.intellij.psi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.nebula.util.xml.HyphenNameStrategy;

public class HyphenNameStrategyTest {

	HyphenNameStrategy nameStrategy = new HyphenNameStrategy();

	@Test
	public void testConvertName() {
		assertEquals("height", nameStrategy.convertName("Height"));
		assertEquals("first-name", nameStrategy.convertName("FirstName"));
		assertEquals("ID", nameStrategy.convertName("ID"));
		assertEquals("id", nameStrategy.convertName("Id"));
		assertEquals("ISDN", nameStrategy.convertName("ISDN"));
		assertEquals("first-ID", nameStrategy.convertName("FirstID"));

		assertEquals("ad-issue-id", nameStrategy.convertName("AdIssueId"));
		assertEquals("ad-client-id", nameStrategy.convertName("AdClientId"));
		assertEquals("support-email", nameStrategy.convertName("SupportEmail"));
		assertEquals("db-address", nameStrategy.convertName("DbAddress"));
		assertEquals("local-host", nameStrategy.convertName("LocalHost"));
		assertEquals("operating-system-info", nameStrategy.convertName("OperatingSystemInfo"));

	}

	@Test
	public void testSplitIntoWords() {
		assertEquals("height", nameStrategy.splitIntoWords("height"));
		assertEquals("first name", nameStrategy.splitIntoWords("first-name"));
		assertEquals("ID", nameStrategy.splitIntoWords("ID"));
		assertEquals("id", nameStrategy.splitIntoWords("id"));
		assertEquals("ISDN", nameStrategy.splitIntoWords("ISDN"));
		assertEquals("first ID", nameStrategy.splitIntoWords("first-ID"));

		assertEquals("ad issue id", nameStrategy.splitIntoWords("ad-issue-id"));
		assertEquals("ad client id", nameStrategy.splitIntoWords("ad-client-id"));
		assertEquals("support email", nameStrategy.splitIntoWords("support-email"));
		assertEquals("db address", nameStrategy.splitIntoWords("db-address"));
		assertEquals("local host", nameStrategy.splitIntoWords("local-host"));
		assertEquals("operating system info", nameStrategy.splitIntoWords("operating-system-info"));
	}

}
