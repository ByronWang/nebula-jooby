package nebula.module;

import static org.objectweb.asm.Opcodes.ACC_BRIDGE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import nebula.jdbc.builders.schema.JDBCConfiguration;
import nebula.jdbc.builders.schema.JDBCConfiguration.JDBCType;
import nebula.tinyasm.ClassBuilder;
import nebula.tinyasm.data.ClassBody;
import nebula.tinyasm.data.MethodCode;

public class JdbcRowMapperBuilder {
	public byte[] make(String clazz, String targetClazz, List<FieldMapper> maps) {

		ClassBody cw = ClassBuilder.make(clazz).imPlements(JdbcRowMapper.class, targetClazz).body();

		cw.constructerEmpty();
		{
			cw.method("map").parameter("rs", ResultSet.class).reTurn(targetClazz).tHrow(SQLException.class).code(mv -> {
				mv.line();
				mv.NEW(targetClazz);
				mv.DUP();
				Class<?>[] clazzes = new Class<?>[maps.size()];
				for (int i = 0; i < maps.size(); i++) {
					FieldMapper fieldMapper = maps.get(i);
					String name = fieldMapper.fieldName;
					JDBCType javatype = JDBCConfiguration.javaJdbcTypes.get(fieldMapper.fieldClazz.getName());
					String getname = javatype.getname;
					Class<?> jdbcClass = javatype.clazz;
					clazzes[i] = javatype.clazz;

					map(mv, name, getname, jdbcClass, jdbcClass);
				}
				mv.SPECIAL(targetClazz, "<init>").parameter(clazzes).INVOKE();
				mv.RETURNTop();
			});
		}
		{
			cw.method(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "map")
				.parameter("rs", ResultSet.class)
				.reTurn(Object.class)
				.tHrow(SQLException.class)
				.code(mv -> {
					mv.line();
					mv.LOAD(0);
					mv.LOAD(1);
					mv.VIRTUAL(clazz, "map").parameter(ResultSet.class).reTurn(targetClazz).INVOKE();
					mv.RETURNTop();
				});
		}

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
