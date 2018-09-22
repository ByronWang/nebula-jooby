package com.intellij.psi;

import static org.junit.Assert.*;

import org.junit.Test;

import com.intellij.util.xml.DbNameStrategy;

public class DBNameStrategyTest {

	DbNameStrategy nameStrategy = new DbNameStrategy();

	@Test
	public void testConvertName() {
		assertEquals("HEIGHT", nameStrategy.convertName("Height"));
		assertEquals("FIRST_NAME", nameStrategy.convertName("FirstName"));
		assertEquals("ID", nameStrategy.convertName("ID"));
		assertEquals("ID", nameStrategy.convertName("Id"));
		assertEquals("ISDN", nameStrategy.convertName("ISDN"));
		assertEquals("FIRST_ID", nameStrategy.convertName("FirstID"));

		assertEquals("AD_ISSUE_ID", nameStrategy.convertName("AdIssueId"));
		assertEquals("AD_CLIENT_ID", nameStrategy.convertName("AdClientId"));
		assertEquals("SUPPORT_EMAIL", nameStrategy.convertName("SupportEmail"));
		assertEquals("DB_ADDRESS", nameStrategy.convertName("DbAddress"));
		assertEquals("LOCAL_HOST", nameStrategy.convertName("LocalHost"));
		assertEquals("OPERATING_SYSTEM_INFO", nameStrategy.convertName("OperatingSystemInfo"));
	}

	@Test
	public void testSplitIntoWords() {
		assertEquals("height", nameStrategy.splitIntoWords("HEIGHT"));
		assertEquals("first name", nameStrategy.splitIntoWords("FIRST_NAME"));
		assertEquals("id", nameStrategy.splitIntoWords("ID"));
		assertEquals("id", nameStrategy.splitIntoWords("ID"));
		assertEquals("isdn", nameStrategy.splitIntoWords("ISDN"));
		assertEquals("first id", nameStrategy.splitIntoWords("FIRST_ID"));

		assertEquals("ad issue id", nameStrategy.splitIntoWords("AD_ISSUE_ID"));
		assertEquals("ad client id", nameStrategy.splitIntoWords("AD_CLIENT_ID"));
		assertEquals("support email", nameStrategy.splitIntoWords("SUPPORT_EMAIL"));
		assertEquals("db address", nameStrategy.splitIntoWords("DB_ADDRESS"));
		assertEquals("local host", nameStrategy.splitIntoWords("LOCAL_HOST"));
		assertEquals("operating system info", nameStrategy.splitIntoWords("OPERATING_SYSTEM_INFO"));
	}

}
//camelCase PascalCase 
//Abbreviation  : 當字母縮寫仍須以個別字母發音時（如 rpm 和 PDF）便是 initialism
//acronym : 當字母縮寫可以單獨發音時（如 laser 和 scuba）便是 acronym

