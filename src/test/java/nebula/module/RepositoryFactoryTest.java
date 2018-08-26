package nebula.module;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
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

		User a = new User(10, "name_a10", "description_a10");
		User b = new User(20, "name_b20", "description_b20");

		{
			List<User> users0 = userRepository.list(0, 0);
			assertEquals("[]", users0.toString());
		}
		{
			userRepository.insert(a);
			List<User> users1 = userRepository.list(0, 0);
			assertEquals("[User [id=10, name=name_a10, description=description_a10]]", users1.toString());
		}
		{
			userRepository.insert(b);
			List<User> users1 = userRepository.list(0, 0);
			System.out.println(users1);
			assertEquals(
					"[User [id=10, name=name_a10, description=description_a10], User [id=20, name=name_b20, description=description_b20]]",
					users1.toString());
		}
		{
			User b2 = new User(20, "name_b20_new", "description_b20_new");
			userRepository.update(b2);

			List<User> users1 = userRepository.list(0, 0);
			assertEquals(
					"[User [id=10, name=name_a10, description=description_a10], User [id=20, name=name_b20_new, description=description_b20_new]]",
					users1.toString());
		}
		{
			userRepository.delete(a.getId());
			List<User> users1 = userRepository.list(0, 0);
			assertEquals("[User [id=20, name=name_b20_new, description=description_b20_new]]", users1.toString());
		}
		{
			userRepository.delete(b.getId());
			List<User> users1 = userRepository.list(0, 0);
			assertEquals("[]", users1.toString());
		}
	}

	@Test
	public void test_getUserComplexRepository() {
		Repository<UserComplex> userRepository = repositoryFactory.getRepository(UserComplex.class);

		userRepository.init();
		{
			List<UserComplex> users1 = userRepository.list(0, 0);
			assertEquals("[]", users1.toString());
		}
		UserComplex a = new UserComplex(10, "name_a10", new BigDecimal(10000), false, (byte) 51, (short) 601, 701, 801L,
				90.1F, 100.1D, new Date(1100), new Time(1200), new Timestamp(1300));
		UserComplex b = new UserComplex(20, "name_b20", new BigDecimal(20000), false, (byte) 251, (short) 2601, 2701,
				2801L, 290.1F, 2100.1D, new Date(21100), new Time(21200), new Timestamp(21300));

		{
			userRepository.insert(a);
			List<UserComplex> users1 = userRepository.list(0, 0);
			assertEquals(
					"[UserComplex [id=10, string=name_a10, bigDecimal=10000, z=false, b=51, s=601, i=701, l=801, f=90.1, d=100.1, date=1970-01-01, time=08:00:01, timestamp=1970-01-01 08:00:01.3]]",
					users1.toString());
		}
		{
			userRepository.insert(b);
			List<UserComplex> users1 = userRepository.list(0, 0);
			System.out.println(users1);
			assertEquals(
					"[UserComplex [id=10, string=name_a10, bigDecimal=10000, z=false, b=51, s=601, i=701, l=801, f=90.1, d=100.1, date=1970-01-01, time=08:00:01, timestamp=1970-01-01 08:00:01.3], "
							+ "UserComplex [id=20, string=name_b20, bigDecimal=20000, z=false, b=-5, s=2601, i=2701, l=2801, f=290.1, d=2100.1, date=1970-01-01, time=08:00:21, timestamp=1970-01-01 08:00:21.3]]",
					users1.toString());
		}
		{
			UserComplex b2 = new UserComplex(20, "name_b30", new BigDecimal(30000), false, (byte) 351, (short) 3601,
					3701, 3801L, 390.1F, 3100.1D, new Date(31100), new Time(31300), new Timestamp(31300));
			userRepository.update(b2);

			List<UserComplex> users1 = userRepository.list(0, 0);
			assertEquals(
					"[UserComplex [id=10, string=name_a10, bigDecimal=10000, z=false, b=51, s=601, i=701, l=801, f=90.1, d=100.1, date=1970-01-01, time=08:00:01, timestamp=1970-01-01 08:00:01.3], "
							+ "UserComplex [id=20, string=name_b30, bigDecimal=30000, z=false, b=95, s=3601, i=3701, l=3801, f=390.1, d=3100.1, date=1970-01-01, time=08:00:31, timestamp=1970-01-01 08:00:31.3]]",
					users1.toString());
		}
		{
			userRepository.delete(a.getId());
			List<UserComplex> users1 = userRepository.list(0, 0);
			assertEquals(
					"[UserComplex [id=20, string=name_b30, bigDecimal=30000, z=false, b=95, s=3601, i=3701, l=3801, f=390.1, d=3100.1, date=1970-01-01, time=08:00:31, timestamp=1970-01-01 08:00:31.3]]",
					users1.toString());
		}
		{
			userRepository.delete(b.getId());
			List<UserComplex> users1 = userRepository.list(0, 0);
			assertEquals("[]", users1.toString());
		}
	}

	@Test
	public void printUserMapper() {
		System.out.println(RefineCode.refineCode(toString(UserMapper.class.getName())));

	}

}
