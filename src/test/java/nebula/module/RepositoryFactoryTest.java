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

		assert userRepository.getClass().getName() != null;
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

		List<User> userlist;
		{
			userlist = userRepository.list(0, 0);
			assertEquals("[]", userlist.toString());
		}
		{
			userRepository.insert(a);
			userlist = userRepository.list(0, 0);
			assertEquals("[User [id=10, name=name_a10, description=description_a10]]", userlist.toString());
		}
		{
			userRepository.insert(b);
			userlist = userRepository.list(0, 0);
			assertEquals(
					"[User [id=10, name=name_a10, description=description_a10], User [id=20, name=name_b20, description=description_b20]]",
					userlist.toString());
		}
		{
			User b2 = new User(20, "name_b20_new", "description_b20_new");
			userRepository.update(b2);

			userlist = userRepository.list(0, 0);
			assertEquals(
					"[User [id=10, name=name_a10, description=description_a10], User [id=20, name=name_b20_new, description=description_b20_new]]",
					userlist.toString());
		}
		{
			userRepository.delete(a.getId());
			userlist = userRepository.list(0, 0);
			assertEquals("[User [id=20, name=name_b20_new, description=description_b20_new]]", userlist.toString());
		}
		{
			userRepository.delete(b.getId());
			userlist = userRepository.list(0, 0);
			assertEquals("[]", userlist.toString());
		}
	}

	@Test
	public void test_getUserComplexRepository() {
		Repository<UserComplex> userRepository = repositoryFactory.getRepository(UserComplex.class);
		List<UserComplex> userlist;
		userRepository.init();
		{
			userlist = userRepository.list(0, 0);
			assertEquals("[]", userlist.toString());
		}
		UserComplex a = new UserComplex(10, "name_a10", new BigDecimal(10000), false, 'A', (byte) 51, (short) 601, 701,
				801L, 90.1F, 100.1D, new Date(1100), new Time(1200), new Timestamp(1300));
		UserComplex b = new UserComplex(20, "name_b20", new BigDecimal(20000), false, 'B', (byte) 251, (short) 2601,
				2701, 2801L, 290.1F, 2100.1D, new Date(21100), new Time(21200), new Timestamp(21300));
		UserComplex b2 = new UserComplex(20, "name_b30", new BigDecimal(30000), false, 'C', (byte) 351, (short) 3601,
				3701, 3801L, 390.1F, 3100.1D, new Date(31100), new Time(31300), new Timestamp(31300));

		{
			userRepository.insert(a);
			userlist = userRepository.list(0, 0);
			assertEquals(
					"[UserComplex [id=10, string=name_a10, bigDecimal=10000, z=false, c=A, b=51, s=601, i=701, l=801, f=90.1, d=100.1, date=1970-01-01, time=08:00:01, timestamp=1970-01-01 08:00:01.3]]",
					userlist.toString());
		}
		{
			userRepository.insert(b);
			userlist = userRepository.list(0, 0);
			assertEquals(
					"[UserComplex [id=10, string=name_a10, bigDecimal=10000, z=false, c=A, b=51, s=601, i=701, l=801, f=90.1, d=100.1, date=1970-01-01, time=08:00:01, timestamp=1970-01-01 08:00:01.3], "
							+ "UserComplex [id=20, string=name_b20, bigDecimal=20000, z=false, c=B, b=-5, s=2601, i=2701, l=2801, f=290.1, d=2100.1, date=1970-01-01, time=08:00:21, timestamp=1970-01-01 08:00:21.3]]",
					userlist.toString());
		}
		{
			userRepository.update(b2);

			userlist = userRepository.list(0, 0);
			assertEquals(
					"[UserComplex [id=10, string=name_a10, bigDecimal=10000, z=false, c=A, b=51, s=601, i=701, l=801, f=90.1, d=100.1, date=1970-01-01, time=08:00:01, timestamp=1970-01-01 08:00:01.3], "
							+ "UserComplex [id=20, string=name_b30, bigDecimal=30000, z=false, c=C, b=95, s=3601, i=3701, l=3801, f=390.1, d=3100.1, date=1970-01-01, time=08:00:31, timestamp=1970-01-01 08:00:31.3]]",
					userlist.toString());
		}
		{
			userRepository.delete(a.getId());
			userlist = userRepository.list(0, 0);
			assertEquals(
					"[UserComplex [id=20, string=name_b30, bigDecimal=30000, z=false, c=C, b=95, s=3601, i=3701, l=3801, f=390.1, d=3100.1, date=1970-01-01, time=08:00:31, timestamp=1970-01-01 08:00:31.3]]",
					userlist.toString());
		}
		{
			userRepository.delete(b.getId());
			userlist = userRepository.list(0, 0);
			assertEquals("[]", userlist.toString());
		}
	}

	@Test
	public void test_getUserMoreComplexRepository() {
		Repository<UserMoreComplex> userRepository = repositoryFactory.getRepository(UserMoreComplex.class);
		List<UserMoreComplex> userlist;
		userRepository.init();
		{
			userlist = userRepository.list(0, 0);
			assertEquals("[]", userlist.toString());
		}

//		long id, String string, BigDecimal bigDecimal, Boolean z, Character c, Byte b, Short s,
//		Integer i, Long l, Float f, Double d, Date date, Time time, Timestamp timestamp
//		UserMoreComplex a = new UserMoreComplex(10, "name_b20", null, null, null, null, null, null, null, null, null, null, null, null);

		UserMoreComplex a = new UserMoreComplex(10, "name_a10", new BigDecimal(10000), false, 'A', (byte) 51,
				(short) 601, 701, 801L, 90.1F, 100.1D, new Date(1100), new Time(1200), new Timestamp(1300));

		UserMoreComplex b = new UserMoreComplex(20, "name_b20", new BigDecimal(20000), false, 'B', (byte) 251,
				(short) 2601, 2701, 2801L, 290.1F, 2100.1D, new Date(21100), new Time(21200), new Timestamp(21300));
		UserMoreComplex b2 = new UserMoreComplex(20, "name_b30", new BigDecimal(30000), false, 'C', (byte) 351,
				(short) 3601, 3701, 3801L, 390.1F, 3100.1D, new Date(31100), new Time(31300), new Timestamp(31300));

		{
			userRepository.insert(a);
			userlist = userRepository.list(0, 0);
			assertEquals(
					"[UserMoreComplex [id=10, string=name_a10, bigDecimal=10000, z=false, c=A, b=51, s=601, i=701, l=801, f=90.1, d=100.1, date=1970-01-01, time=08:00:01, timestamp=1970-01-01 08:00:01.3]]",
					userlist.toString());
		}
		{
			userRepository.insert(b);
			userlist = userRepository.list(0, 0);
			assertEquals(
					"[UserMoreComplex [id=10, string=name_a10, bigDecimal=10000, z=false, c=A, b=51, s=601, i=701, l=801, f=90.1, d=100.1, date=1970-01-01, time=08:00:01, timestamp=1970-01-01 08:00:01.3], "
							+ "UserMoreComplex [id=20, string=name_b20, bigDecimal=20000, z=false, c=B, b=-5, s=2601, i=2701, l=2801, f=290.1, d=2100.1, date=1970-01-01, time=08:00:21, timestamp=1970-01-01 08:00:21.3]]",
					userlist.toString());
		}
		{
			userRepository.update(b2);

			userlist = userRepository.list(0, 0);
			assertEquals(
					"[UserMoreComplex [id=10, string=name_a10, bigDecimal=10000, z=false, c=A, b=51, s=601, i=701, l=801, f=90.1, d=100.1, date=1970-01-01, time=08:00:01, timestamp=1970-01-01 08:00:01.3], "
							+ "UserMoreComplex [id=20, string=name_b30, bigDecimal=30000, z=false, c=C, b=95, s=3601, i=3701, l=3801, f=390.1, d=3100.1, date=1970-01-01, time=08:00:31, timestamp=1970-01-01 08:00:31.3]]",
					userlist.toString());
		}
		{
			userRepository.delete(a.getId());
			userlist = userRepository.list(0, 0);
			assertEquals(
					"[UserMoreComplex [id=20, string=name_b30, bigDecimal=30000, z=false, c=C, b=95, s=3601, i=3701, l=3801, f=390.1, d=3100.1, date=1970-01-01, time=08:00:31, timestamp=1970-01-01 08:00:31.3]]",
					userlist.toString());
		}
		{
			userRepository.delete(b.getId());
			userlist = userRepository.list(0, 0);
			assertEquals("[]", userlist.toString());
		}
	}
//	@Test
//	public void printUserMapper() {
//		System.out.println(RefineCode.refineCode(toString(UserMapper.class.getName())));
//	}

}
