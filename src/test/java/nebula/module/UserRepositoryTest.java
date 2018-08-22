package nebula.module;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nebula.tinyasm.util.RefineCode;

public class UserRepositoryTest extends TestBase {
	Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test"); // (H2 in-memory database)

	@Before
	public void before() {
		jdbi.open();
	}

	@After
	public void after() {
	}

	@Test
	public void testPrint() throws IOException {
		System.out.println(RefineCode.refineCode(toString(UserRepository2.class)));
	}

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

		UserRepositoryBuilder builder = new UserRepositoryBuilder();

		byte[] clazzcode = builder.make(clazz, targetClazz, mapClazz);

//		String codeActual = toString(clazzcode);
//		String codeExpected = toString(clazz);
//		assertEquals("Code", codeExpected, codeActual);

		MyClassLoader mcl = new MyClassLoader();
		Class<?> hw = mcl.defineClassByName(clazz, clazzcode, 0, clazzcode.length);

		Constructor<?> ct = hw.getConstructor(Jdbi.class);

		// 利用反射创建对象
		Repository<User> userRepository = (Repository<User>) ct.newInstance(jdbi);
		jdbi.useHandle(handle -> {
			handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR ,description VARCHAR)");
			handle.commit();
		});

//		UserRepository userRepository = new UserRepository(jdbi);
		List<User> users1 = userRepository.list(0, 0);

		User a = new User(0, "wangshilian","desctiption0");
		User b = new User(2, "lixiang","desctiption2");

		userRepository.insert(a);

		users1 = userRepository.list(0, 0);
		System.out.println(users1);

		userRepository.insert(b);

		users1 = userRepository.list(0, 0);
		System.out.println(users1);

		User b2 = new User(2, "lixiang_new_name","desctiption");
		userRepository.update(b2);

		users1 = userRepository.list(0, 0);
		System.out.println(users1);
		userRepository.delete(a.getId());

		users1 = userRepository.list(0, 0);
		System.out.println(users1);

		System.out.println(userRepository.getClass().getName());
	}
}
