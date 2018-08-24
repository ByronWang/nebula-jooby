package nebula.module;

import org.junit.Test;

import nebula.tinyasm.util.RefineCode;

public class RepositoryFactoryTest extends TestBase {

	@Test
	public void test() {
		RepositoryFactory.getMapper(User.class);
	}
	@Test
	public void test_getRepository() {
		RepositoryFactory.getRepository(User.class);
	}

	@Test
	public void printUserMapper() {
		System.out.println(RefineCode.refineCode(toString(UserMapper.class.getName())));

	}

}
