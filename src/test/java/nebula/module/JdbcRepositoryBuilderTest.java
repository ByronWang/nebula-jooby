package nebula.module;

import static org.junit.Assert.assertEquals;
import static org.objectweb.asm.Opcodes.ACC_BRIDGE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jooby.Results;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nebula.tinyasm.ClassBuilder;
import nebula.tinyasm.data.ClassBody;
import nebula.tinyasm.util.RefineCode;

public class JdbcRepositoryBuilderTest extends TestBase {
	Jdbi jdbi;

	@Before
	public void before() {
		jdbi = Jdbi.create("jdbc:h2:mem:test"); // (H2 in-memory database)
		jdbi.open();
	}

	@After
	public void after() {
	}

//	@Test
//	public void testPrint() throws IOException {
//		System.out.println(RefineCode.refineCode(toString(UserJdbcRowMapper.class),ResultSet.class,PreparedStatement.class,JdbcRepository.class));
//	}

	class MyClassLoader extends ClassLoader {
		public Class<?> defineClassByName(String name, byte[] b, int off, int len) {
			Class<?> clazz = super.defineClass(name, b, off, len);
			return clazz;
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testConstructerEmpty() throws IOException, InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {

		String clazz = "ThisRepository";
		String targetClazz = User.class.getName();
		String mapClazz = "nebula.module.UserMapper";

		Connection connection = jdbi.open().getConnection();
		// 利用反射创建对象
		Repository<User> userRepository = new RepositoryFactory(connection).getRepository(User.class);
		((JdbcRepository<User>)userRepository).setConnection(connection);

		jdbi.useHandle(handle -> {
			handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR ,description VARCHAR)");
			handle.commit();
		});
//
////		UserRepository userRepository = new UserRepository(jdbi);
//		UserRepository userRepository = RepositoryFactory.getRepository(User.class);
		List<User> users1 = userRepository.list(0, 0);

		User a = new User(0, "wangshilian", "desctiption0");
		User b = new User(2, "lixiang", "desctiption2");

		userRepository.insert(a);

		users1 = userRepository.list(0, 0);
		System.out.println(users1);

		userRepository.insert(b);

		users1 = userRepository.list(0, 0);
		System.out.println(users1);

		User b2 = new User(2, "lixiang_new_name", "desctiption");
		userRepository.update(b2);

		users1 = userRepository.list(0, 0);
		System.out.println(users1);
		userRepository.delete(a.getId());

		users1 = userRepository.list(0, 0);
		System.out.println(users1);

		System.out.println(userRepository.getClass().getName());
	}

	@Test
	public void testConstructerEmptyCode() {

		String clazz = UserJdbcRepository.class.getName();
		String targetClazz = User.class.getName();
		String mapClazz = UserJdbcRowMapper.class.getName();

		JdbcRepositoryBuilder builder = new JdbcRepositoryBuilder();
		byte[] code = builder.make(clazz, targetClazz, mapClazz, null);

		String codeActual = toString(code);
		String codeExpected = toString(clazz);
		assertEquals("Code", codeExpected, codeActual);
	}
}
