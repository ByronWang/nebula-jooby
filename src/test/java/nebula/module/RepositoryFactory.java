package nebula.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import nebula.jdbc.builders.schema.ColumnDefination;
import nebula.jdbc.builders.schema.ColumnFactory;
import nebula.jdbc.builders.schema.JDBCConfiguration;
import nebula.jdbc.builders.schema.JDBCTypes;

public class RepositoryFactory {

	static class MyClassLoader extends ClassLoader {
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

	static private void makesureFolderExists(File file) {
		if (!file.getParentFile().exists()) makesureFolderExists(file.getParentFile());
		file.mkdir();
	}

	static MyClassLoader myClassLoader = new MyClassLoader();

	static <T> Repository<T> getRepository(Class<T> type) {
		getMapper(type);

		List<FieldMapper> mappers = new ArrayList<>();

		for (Field field : type.getDeclaredFields()) {
			String name = field.getName();
			String fieldType = field.getType().getName();
			JDBCTypes jdbctype = JDBCConfiguration.mapperRevert.get(fieldType);
			ColumnDefination column = ColumnFactory.Column(jdbctype, name);
			FieldMapper mapper = new FieldMapper(name, fieldType, column);
			mappers.add(mapper);
		}
		String clazz = type.getName() + "Repository";
		String targetClazz = type.getName();
		String mapClazz = type.getName() + "RowMapper";

		byte[] code = repositoryBuilder.make(clazz, targetClazz, mapClazz, mappers);

		try {
			@SuppressWarnings("unchecked")
			Class<Repository<T>> rowMapperClazz = (Class<Repository<T>>) myClassLoader.defineClassByName(clazz, code);
			Constructor<Repository<T>> ct = rowMapperClazz.getConstructor(Jdbi.class);

			Jdbi jdbi = null;
			Repository<T> userRepository = (Repository<T>) ct.newInstance(jdbi);
			return userRepository;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static RowMapperBuilder rowMapperBuilder = new RowMapperBuilder();
	static RepositoryBuilder repositoryBuilder = new RepositoryBuilder();

	static <T> RowMapper<T> getMapper(Class<T> type) {
		List<FieldMapper> mappers = new ArrayList<>();

		for (Field field : type.getDeclaredFields()) {
			String name = field.getName();
			String fieldType = field.getType().getName();
			JDBCTypes jdbctype = JDBCConfiguration.mapperRevert.get(fieldType);
			ColumnDefination column = ColumnFactory.Column(jdbctype, name);
			FieldMapper mapper = new FieldMapper(name, fieldType, column);
			mappers.add(mapper);
		}

		String clazz = type.getName() + "RowMapper";
		String targetClazz = type.getName();

		byte[] code = rowMapperBuilder.make(clazz, targetClazz, mappers);

		try {
			@SuppressWarnings("unchecked")
			Class<RowMapper<T>> rowMapperClazz = (Class<RowMapper<T>>) myClassLoader.defineClassByName(clazz, code);
			RowMapper<T> rowMapper = rowMapperClazz.newInstance();
			return rowMapper;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	static class FieldMapper {
		String javaname;
		String javatype;
		ColumnDefination column;

		public FieldMapper(String javaname, String javatype, ColumnDefination column) {
			super();
			this.javaname = javaname;
			this.javatype = javatype;
			this.column = column;
		}

	}

}
