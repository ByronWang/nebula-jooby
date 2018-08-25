package nebula.module;

import static org.junit.Assert.assertEquals;

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

		User a = new User(10, "name_a10", "description_a10");
		User b = new User(20, "name_b20", "description_b20");
		{
			userRepository.insert(a);
			users1 = userRepository.list(0, 0);
			assertEquals("[User [id=10, name=name_a10, description=description_a10]]", users1.toString());
		}
		{
			userRepository.insert(b);
			users1 = userRepository.list(0, 0);
			System.out.println(users1);
			assertEquals(
					"[User [id=10, name=name_a10, description=description_a10], User [id=20, name=name_b20, description=description_b20]]",
					users1.toString());
		}
		{
			User b2 = new User(20, "name_b20_new", "description_b20_new");
			userRepository.update(b2);

			users1 = userRepository.list(0, 0);
			assertEquals(
					"[User [id=10, name=name_a10, description=description_a10], User [id=20, name=name_b20_new, description=description_b20_new]]",
					users1.toString());
		}
		{
			userRepository.delete(a.getId());
			users1 = userRepository.list(0, 0);
			assertEquals("[User [id=20, name=name_b20_new, description=description_b20_new]]", users1.toString());
		}
		{
			userRepository.delete(b.getId());
			users1 = userRepository.list(0, 0);
			assertEquals("[]", users1.toString());
		}
	}

	@Test
	public void test_getUserComplexRepository() {
		Repository<UserComplex> userRepository = repositoryFactory.getRepository(UserComplex.class);

//		jdbi.useHandle(handle -> {
//			handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR ,description VARCHAR)");
//			handle.commit();
//		});
		
		userRepository.init();

		List<UserComplex> users1 = userRepository.list(0, 0);
//
//		User a = new User(10, "name_a10", "description_a10");
//		User b = new User(20, "name_b20", "description_b20");
//		{
//			userRepository.insert(a);
//			users1 = userRepository.list(0, 0);
//			assertEquals("[User [id=10, name=name_a10, description=description_a10]]", users1.toString());
//		}
//		{
//			userRepository.insert(b);
//			users1 = userRepository.list(0, 0);
//			System.out.println(users1);
//			assertEquals(
//					"[User [id=10, name=name_a10, description=description_a10], User [id=20, name=name_b20, description=description_b20]]",
//					users1.toString());
//		}
//		{
//			User b2 = new User(20, "name_b20_new", "description_b20_new");
//			userRepository.update(b2);
//
//			users1 = userRepository.list(0, 0);
//			assertEquals(
//					"[User [id=10, name=name_a10, description=description_a10], User [id=20, name=name_b20_new, description=description_b20_new]]",
//					users1.toString());
//		}
//		{
//			userRepository.delete(a.getId());
//			users1 = userRepository.list(0, 0);
//			assertEquals("[User [id=20, name=name_b20_new, description=description_b20_new]]", users1.toString());
//		}
//		{
//			userRepository.delete(b.getId());
//			users1 = userRepository.list(0, 0);
//			assertEquals("[]", users1.toString());
//		}
	}

	@Test
	public void printUserMapper() {
		System.out.println(RefineCode.refineCode(toString(UserMapper.class.getName())));

	}

}
