package nebula.module;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nebula.jdbc.builders.schema.ColumnDefination;
import nebula.jdbc.builders.schema.JDBCTypes;
import nebula.tinyasm.util.RefineCode;

public class JdbcRowMapperBuilderTest extends TestBase {

	String clazz;
	List<FieldMapper> maps;
	JdbcRowMapperBuilder builder;
	MyClassLoader classLoader;
	Arguments arguments = new Arguments();

	@Before
	public void before() {
		classLoader = new MyClassLoader();
		builder = new JdbcRowMapperBuilder(arguments);
		maps = new ArrayList<FieldMapper>();
		clazz = UserJdbcRowMapper.class.getName();
		maps.add(new FieldMapper("id", "getId", long.class, new ColumnDefination("id", JDBCTypes.INTEGER)));
		maps.add(new FieldMapper("name", "getName", String.class, new ColumnDefination("name", JDBCTypes.VARCHAR)));
		maps.add(new FieldMapper("description", "getDescription", String.class,
				new ColumnDefination("description", JDBCTypes.VARCHAR)));
	}

	@After
	public void after() {
	}

	public void testPrint() throws IOException {
		System.out.println(RefineCode.refineCode(toString(UserJdbcRowMapper.class), ResultSet.class,
				PreparedStatement.class, JdbcRepository.class));
	}

	class MyClassLoader extends ClassLoader {
		public Class<?> defineClassByName(String name, byte[] b, int off, int len) {
			Class<?> clazz = super.defineClass(name, b, off, len);
			return clazz;
		}

		public Class<?> defineClassByName(String name, byte[] b) {
			Class<?> clazz = super.defineClass(name, b, 0, b.length);
			return clazz;
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMockRunning()
			throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, SQLException {

		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getLong("id")).thenReturn(1047L);
		when(resultSet.getString("name")).thenReturn("TestName1047");
		when(resultSet.getString("description")).thenReturn("TestDescription1047");

		String targetClazz = User.class.getName();
		byte[] code = builder.make(clazz, targetClazz, maps);
		Class<JdbcRowMapper<User>> row = (Class<JdbcRowMapper<User>>) classLoader.defineClassByName(this.clazz, code);
		JdbcRowMapper<User> mapper = row.newInstance();
		User user = mapper.map(resultSet);

		assertEquals(1047L, user.getId());
		assertEquals("TestName1047", user.getName());
		assertEquals("TestDescription1047", user.getDescription());
	}

	@Test
	public void testMakedCode() {

		String targetClazz = User.class.getName();
		byte[] code = builder.make(clazz, targetClazz, maps);

		String codeActual = toString(code);
		String codeExpected = toString(clazz);
		assertEquals("Code", codeExpected, codeActual);
	}
}
