package nebula.module;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JDBIUserTest extends TestBase {
	Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test"); // (H2 in-memory database)

	@Before
	public void before() {
		jdbi.open();
	}

	@After
	public void after() {
	}

	@Test
	public void testJdbi() throws InterruptedException {
		jdbi.useHandle(handle -> {
			handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");
			handle.commit();
		});

		UserRepository2 userRepository = new UserRepository2(jdbi);
		List<User> users1 = userRepository.list(0, 0);

		User a = new User(0, "wangshilian","desctiption");
		User b = new User(2, "lixiang","desctiption");

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

//		assertTrue(users1.size() == 4);

//		assertThat(users).containsExactly(new User(0, "Alice"), new User(1, "Bob"), new User(2, "Clarice"),
//				new User(3, "David"));
	}
}
