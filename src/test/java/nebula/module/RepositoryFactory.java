package nebula.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import nebula.jdbc.builders.schema.ColumnDefination;
import nebula.jdbc.builders.schema.ColumnFactory;
import nebula.jdbc.builders.schema.JDBCConfiguration;
import nebula.jdbc.builders.schema.JDBCTypes;

public class RepositoryFactory {
	Connection conn;

	public RepositoryFactory(Connection conn) {
		this.conn = conn;
	}

	class MyClassLoader extends ClassLoader {
		public Class<?> defineClassByName(String name, byte[] b, int off, int len) {
			{
				File root = new File("target/generated-sources");
				if (!root.exists()) root.mkdirs();

				File file = new File(root, name.replace(".", "/") + ".class");
				if (!file.getParentFile().exists()) {
					makesureFolderExists(file.getParentFile());
				}

				try {
					FileOutputStream o = new FileOutputStream(file);
					o.write(b);
					o.close();
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			Class<?> clazz = super.defineClass(name, b, 0, b.length);
			return clazz;
		}

		public Class<?> defineClassByName(String name, byte[] b) {
			return defineClassByName(name, b, 0, b.length);
		}
	}

	private void makesureFolderExists(File file) {
		if (!file.getParentFile().exists()) makesureFolderExists(file.getParentFile());
		file.mkdir();
	}

	private MyClassLoader myClassLoader = new MyClassLoader();

	public <T> Repository<T> getRepository(Class<T> type) {
		List<FieldMapper> mappers = build(type);

		getMapper(type);

		String clazz = type.getName() + "Repository";
		String targetClazz = type.getName();
		String mapClazz = type.getName() + "RowMapper";

		byte[] code = repositoryBuilder.make(clazz, targetClazz, mapClazz, mappers);

		try {
			@SuppressWarnings("unchecked")
			Class<JdbcRepository<T>> clazzJdbcRepository = (Class<JdbcRepository<T>>) myClassLoader
				.defineClassByName(clazz, code);
			JdbcRepository<T> jdbcRepository = clazzJdbcRepository.newInstance();
			jdbcRepository.setConnection(this.conn);
			return jdbcRepository;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<FieldMapper> build(Class<?> type) {
		List<FieldMapper> mappers = new ArrayList<>();

		for (Field field : type.getDeclaredFields()) {
			String name = field.getName();
			String fieldType = field.getType().getName();
			JDBCTypes jdbctype = JDBCConfiguration.mapperRevert.get(fieldType);
			ColumnDefination column = ColumnFactory.Column(jdbctype, name);
			FieldMapper mapper = new FieldMapper(name, fieldType, column);
			mappers.add(mapper);
		}
		return mappers;
	}

	public JdbcRowMapperBuilder rowMapperBuilder = new JdbcRowMapperBuilder();
	public JdbcRepositoryBuilder repositoryBuilder = new JdbcRepositoryBuilder();

	public <T> JdbcRowMapper<T> getMapper(Class<T> type) {
		List<FieldMapper> mappers = build(type);

		String clazz = type.getName() + "RowMapper";
		String targetClazz = type.getName();

		byte[] code = rowMapperBuilder.make(clazz, targetClazz, mappers);

		try {
			@SuppressWarnings("unchecked")
			Class<JdbcRowMapper<T>> rowMapperClazz = (Class<JdbcRowMapper<T>>) myClassLoader.defineClassByName(clazz,
					code);
			JdbcRowMapper<T> rowMapper = rowMapperClazz.newInstance();
			return rowMapper;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
