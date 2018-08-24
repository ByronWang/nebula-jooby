package nebula.module;

import static org.objectweb.asm.Opcodes.ACC_BRIDGE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import nebula.jdbc.builders.schema.JDBCConfiguration;
import nebula.jdbc.builders.schema.JDBCConfiguration.JavaType;
import nebula.module.RepositoryFactory.FieldMapper;
import nebula.tinyasm.ClassBuilder;
import nebula.tinyasm.data.ClassBody;
import nebula.tinyasm.data.MethodCode;

public class RowMapperBuilder {

	public byte[] make(String clazz, String targetClazz, List<FieldMapper> maps) {
		ClassBody cw = ClassBuilder.make(clazz).imPlements(RowMapper.class, targetClazz).body();

		cw.constructerEmpty();

		cw.publicMethod(targetClazz, "map")
			.tHrow(SQLException.class)
			.parameter("rs", ResultSet.class)
			.parameter("ctx", StatementContext.class)
			.code(mv -> {
				mv.line();
				mv.NEW(targetClazz);
				mv.DUP();

				Class<?>[] clazzes = new Class<?>[maps.size()];
				for (int i = 0; i < maps.size(); i++) {
					FieldMapper fieldMapper = maps.get(i);
					String name = fieldMapper.javaname;
					JavaType javatype = JDBCConfiguration.javaJdbcTypes.get(fieldMapper.javatype);
					String getname = javatype.getname;
					Class<?> jdbcClass = javatype.getclazz;
					clazzes[i] = javatype.getclazz;

					map(mv, name, getname, jdbcClass, jdbcClass);
				}

				mv.SPECIAL(targetClazz, "<init>").parameter(clazzes).INVOKE();

				mv.RETURNTop();
			});

		cw.method(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, Object.class.getName(), "map")
			.tHrow(SQLException.class)
			.parameter("rs", ResultSet.class)
			.parameter("ctx", StatementContext.class)
			.code(mv -> {
				mv.line();
				mv.LOAD(0);
				mv.LOAD(1);
				mv.LOAD(2);
				mv.INVOKEVIRTUAL(cw.getName(), targetClazz, "map", ResultSet.class.getName(),
						StatementContext.class.getName());
				mv.RETURNTop();
			});

		return cw.end().toByteArray();
	}

	private void map(MethodCode mv, String name, String jdbcFuncName, Class<?> jdbcType, Class<?> pojoType) {
		{
			mv.LOAD("rs");
			mv.LOADConst(name);
			mv.INTERFACE(ResultSet.class, jdbcFuncName).parameter(String.class).reTurn(jdbcType).INVOKE();
		}
	}
}