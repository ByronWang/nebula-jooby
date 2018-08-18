package nebula.module.names;

import static org.junit.Assert.*;

import org.junit.Test;

public class NameUtilTest {

	@Test
	public void test() {
		
		System.out.println(StringUtil.join( NameUtil.nameToWords("HyphenName3434Strategy")," "));
	}

}
