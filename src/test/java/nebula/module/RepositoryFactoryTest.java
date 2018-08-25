package nebula.module;

import java.sql.Connection;
import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.junit.Before;
import org.junit.Test;

import nebula.tinyasm.util.RefineCode;

public class RepositoryFactoryTest extends TestBase {

	Jdbi jdbi;
	RepositoryFactory repositoryFactory;

	@Before
	public void before() {
		jdbi = Jdbi.create("jdbc:h2:mem:test");
		Connection connection = jdbi.open().getConnection();
		repositoryFactory = new RepositoryFactory(connection);
	}

	@Test
	public void test_getMapper() {
		repositoryFactory.getMapper(User.class);
	}

	@Test
	public void test_getRepository() {
		Repository<User> userRepository = repositoryFactory.getRepository(User.class);

		System.out.println(userRepository.getClass().getName());
	}

	@Test
	public void test_getUserRepository() {
		Repository<User> userRepository = repositoryFactory.getRepository(User.class);

		jdbi.useHandle(handle -> {
			handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR ,description VARCHAR)");
			handle.commit();
		});

		List<User> users1 = userRepository.list(0, 0);

		User userFirst = new User(0, "wangshilian", "desctiption0");
		User userSecond = new User(2, "lixiang", "desctiption2");

		userRepository.insert(userFirst);

		users1 = userRepository.list(0, 0);
		System.out.println(users1);

		userRepository.insert(userSecond);

		users1 = userRepository.list(0, 0);
		System.out.println(users1);

		User b2 = new User(2, "lixiang_new_name", "desctiption");
		userRepository.update(b2);

		users1 = userRepository.list(0, 0);
		System.out.println(users1);
		userRepository.delete(userFirst.getId());

		users1 = userRepository.list(0, 0);
		System.out.println(users1);

		System.out.println(userRepository.getClass().getName());
	}

	@Test
	public void printUserMapper() {
		System.out.println(RefineCode.refineCode(toString(UserMapper.class.getName())));

	}

}
