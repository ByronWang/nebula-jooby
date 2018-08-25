package nebula.module;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nebula.jdbc.builders.schema.ColumnDefination;
import nebula.jdbc.builders.schema.JDBCTypes;
import nebula.tinyasm.util.RefineCode;

public class JdbcRepositoryBuilderTest extends TestBase {
	Jdbi jdbi;

	String clazz;
	List<FieldMapper> maps;
	JdbcRowMapperBuilder jdbcRowMapperBuilder;

	JdbcRepositoryBuilder jdbcRepositoryBuilder;

	@Before
	public void before() {
		jdbi = Jdbi.create("jdbc:h2:mem:test"); // (H2 in-memory database)
		jdbi.open();
		classLoader = new MyClassLoader();
		jdbcRowMapperBuilder = new JdbcRowMapperBuilder();
		jdbcRepositoryBuilder = new JdbcRepositoryBuilder();
		maps = new ArrayList<FieldMapper>();
		clazz = UserJdbcRowMapper.class.getName();
		maps.add(new FieldMapper(true, "id", "getId", long.class, new ColumnDefination("id", JDBCTypes.INTEGER)));
		maps.add(new FieldMapper("name", "getName", String.class, new ColumnDefination("name", JDBCTypes.VARCHAR)));
		maps.add(new FieldMapper("description", "getDescription", String.class,
				new ColumnDefination("description", JDBCTypes.VARCHAR)));

	}

	@After
	public void after() {
	}

	@Test
	public void testPrint() throws IOException {
		System.out.println(RefineCode.refineCode(toString(UserJdbcRepository.class), ResultSet.class,
				PreparedStatement.class, JdbcRepository.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUser() throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException {

		String clazzTarget = User.class.getName();
		String clazzRowMapper = UserJdbcRowMapper.class.getName() + "testUser";
		Connection connection = jdbi.open().getConnection();

		byte[] codeRowMapper = jdbcRowMapperBuilder.make(clazzRowMapper, clazzTarget, maps);
		@SuppressWarnings("unused")
		Class<JdbcRowMapper<User>> clazzJdbcRowMapper = (Class<JdbcRowMapper<User>>) classLoader
			.defineClassByName(clazzRowMapper, codeRowMapper);
		byte[] codeRepository = jdbcRepositoryBuilder.make(clazz, clazzTarget, clazzRowMapper, "user", maps);
		Class<JdbcRepository<User>> clazzJdbcRepository = (Class<JdbcRepository<User>>) classLoader
			.defineClassByName(this.clazz, codeRepository);

		// 利用反射创建对象
		JdbcRepository<User> userRepository = clazzJdbcRepository.newInstance();
		userRepository.setConnection(connection);

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
	public void testConstructerEmptyCode() {

		String clazz = UserJdbcRepository.class.getName();
		String targetClazz = User.class.getName();
		String mapClazz = UserJdbcRowMapper.class.getName();

		JdbcRepositoryBuilder builder = new JdbcRepositoryBuilder();
		byte[] code = builder.make(clazz, targetClazz, mapClazz, "user", maps);

		String codeActual = toString(code);
		String codeExpected = toString(clazz);
		assertEquals("Code", codeExpected, codeActual);
	}
}
