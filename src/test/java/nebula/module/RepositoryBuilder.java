package nebula.module;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.SqlStatement;
import org.jdbi.v3.core.statement.Update;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import nebula.tinyasm.ClassBuilder;
import nebula.tinyasm.data.ClassBody;
import nebula.tinyasm.data.GenericClazz;
import nebula.tinyasm.data.MethodCode;

public class RepositoryBuilder {

	ClassBody cw;

	String clazz;

	String targetClazz;

	String mapClazz;

	private void make_bind() {
		cw.method("bind")
			.ACC_STATIC()
			.parameter("update", Update.class)
			.parameter("data", targetClazz)
			.reTurn(Update.class)
			.code(mv -> {
				mv.LOAD("update");
				bindField(mv, targetClazz, "data", "id", "getId", long.class);
				bindField(mv, targetClazz, "data", "name", "getName", String.class);
				bindField(mv, targetClazz, "data", "description", "getDescription", String.class);
				mv.RETURNTop();
			});
	}

	private void bindField(MethodCode mv, String targetClazz, String dataName, String paramName, String propGetName,
			Class<?> propGetClazz) {
		{
			mv.line();
			mv.LOADConst(paramName);
			mv.LOAD(dataName);
			mv.VIRTUAL(targetClazz, propGetName).reTurn(propGetClazz).INVOKE();
			mv.VIRTUAL(Update.class, "bind")
				.parameter(String.class)
				.parameter(propGetClazz)
				.reTurn(SqlStatement.class)
				.INVOKE();
			mv.CHECKCAST(Update.class);
		}
	}

	public byte[] make(String clazz, String targetClazz, String mapClazz, List<FieldMapper> maps) {

		this.clazz = clazz;
		this.targetClazz = targetClazz;
		this.mapClazz = mapClazz;

		String idName = "id";
		Class<Long> idClazz = long.class;

		cw = ClassBuilder.make(clazz).imPlements(Repository.class, targetClazz).body();

		cw.referInnerClass(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, MethodHandles.class.getName(), "Lookup");
		cw.field(0, "jdbi", Jdbi.class);
		cw.field(ACC_STATIC, "mapper", mapClazz);

		make_clinit(clazz, mapClazz);
		make_init();

		make_bind();

		make_list("SELECT * FROM user");

		make_findbyid(idName, idClazz, "SELECT * FROM user ORDER BY name WHERE id=:id");

		make_insert("INSERT INTO user(id, name,description) VALUES (:id, :name, :description)");

		make_update("UPDATE user SET name=:name, description=:description WHERE id=:id");

		make_delete(idName, idClazz, "DELETE user WHERE id=:id");

		return cw.end().toByteArray();

	}

	private void make_clinit(String clazz, String mapClazz) {
		cw.method(ACC_STATIC, "<clinit>").code(mv -> {
			mv.line();
			mv.NEW(mapClazz);
			mv.DUP();
			mv.SPECIAL(mapClazz, "<init>").INVOKE();
			mv.PUTSTATIC(clazz, "mapper", mapClazz);
			mv.RETURN();
		});
	}

	private void make_delete(String idName, Class<Long> idClazz, String deleteSQL) {
		String lambdaName = "lambda$delete$4";

		cw.method("delete").parameter(idName, idClazz).reTurn(boolean.class).code(mv -> {
			mv.line();
			mv.LOAD(0);
			mv.GET_THIS_FIELD("jdbi");
			mv.LOAD(1);

			mv.STATIC(clazz, lambdaName)
				.parameter(Handle.class)
				.reTurn(GenericClazz.genericBase(Object.class, Integer.class))
				.LAMBDA(Jdbi.class.getName(), "withHandle")
				.parameter(idClazz)
				.reTurn(HandleCallback.class)
				.INVOKE();

			mv.VIRTUAL(Jdbi.class, "withHandle").parameter(HandleCallback.class).reTurn(Object.class).INVOKE();
			mv.CHECKCAST(Integer.class);
			mv.VIRTUAL(Integer.class, "intValue").reTurn(int.class).INVOKE();

			mv.line();
			mv.LOADConst(1);
			mv.line();
			Label labelElse = mv.codeNewLabel();
			mv.IF_ICMPNE(labelElse);
			mv.LOADConst(1);
			mv.RETURNTop();
			mv.visitLabel(labelElse);
			mv.LOADConst(0);
			mv.RETURNTop();
		});

		cw.method(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, lambdaName)
			.parameter(idName, idClazz)
			.parameter("handle", Handle.class)
			.reTurn(Integer.class)
			.tHrow(RuntimeException.class)
			.code(mv -> {
				mv.line();
				mv.LOAD(2);
				mv.LOADConst(deleteSQL);
				mv.VIRTUAL(Handle.class, "createUpdate").parameter(String.class).reTurn(Update.class).INVOKE();
				mv.line();
				mv.LOADConst(idName);
				mv.LOAD(0);
				mv.VIRTUAL(Update.class, "bind")
					.parameter(String.class)
					.parameter(idClazz)
					.reTurn(SqlStatement.class)
					.INVOKE();
				mv.CHECKCAST(Update.class);

				mv.line();
				mv.VIRTUAL(Update.class, "execute").reTurn(int.class).INVOKE();
				mv.STATIC(Integer.class, "valueOf").parameter(int.class).reTurn(Integer.class).INVOKE();

				mv.line();
				mv.RETURNTop();
			});
	}

	private void make_findbyid(String idName, Class<?> idClazz, String findbyidSQL) {
		String lambdaName = "lambda$findById$1";

		cw.method("findById").parameter("id", long.class).reTurn(targetClazz).code(mv -> {

			mv.line();
			mv.LOAD(0);
			mv.GET_THIS_FIELD("jdbi");

			mv.LOAD(1);
			mv.STATIC(clazz, lambdaName)
				.parameter(Handle.class)
				.reTurn(GenericClazz.genericBase(Object.class, targetClazz))
				.LAMBDA(Jdbi.class.getName(), "withHandle")
				.parameter(idClazz)
				.reTurn(HandleCallback.class)
				.INVOKE();

			mv.VIRTUAL(Jdbi.class, "withHandle").parameter(HandleCallback.class).reTurn(Object.class).INVOKE();
			mv.CHECKCAST(targetClazz);
			mv.RETURNTop();

		});

		cw.method(Opcodes.ACC_PUBLIC + Opcodes.ACC_BRIDGE + Opcodes.ACC_SYNTHETIC, "findById")
			.parameter(idName, idClazz)
			.reTurn(Object.class)
			.code(mv -> {
				mv.line();
				mv.LOAD(0);
				mv.LOAD(idName);
				mv.VIRTUAL(clazz, "findById").parameter(idClazz).reTurn(targetClazz).INVOKE();
				mv.RETURNTop();
			});

		cw.method(Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC + Opcodes.ACC_SYNTHETIC, lambdaName)
			.parameter(idName, idClazz)
			.parameter("handle", Handle.class)
			.reTurn(targetClazz)
			.tHrow(RuntimeException.class)
			.code(mv -> {
				mv.line();
				mv.LOAD("handle");
				mv.LOADConst(findbyidSQL);
				mv.VIRTUAL(Handle.class, "createQuery").parameter(String.class).reTurn(Query.class).INVOKE();

				mv.LOADConst(idName);
				mv.LOAD(idName);
				mv.VIRTUAL(Query.class, "bind")
					.parameter(String.class)
					.parameter(idClazz)
					.reTurn(SqlStatement.class)
					.INVOKE();
				mv.CHECKCAST(Query.class);

				mv.line();
				mv.NEW(mapClazz);
				mv.DUP();
				mv.SPECIAL(mapClazz, "<init>").INVOKE();
				mv.VIRTUAL(Query.class, "map").parameter(RowMapper.class).reTurn(ResultIterable.class).INVOKE();
				mv.INTERFACE(ResultIterable.class, "findOnly").reTurn(Object.class).INVOKE();
				mv.CHECKCAST(targetClazz);

				mv.line();
				mv.RETURNTop();
			});
	}

	private void make_init() {
		cw.method("<init>").parameter("jdbi", Jdbi.class).code(mv -> {
			Label l0 = mv.codeNewLabel();
			mv.visitLabel(l0);
			mv.line();
			mv.LOAD(0);
			mv.SPECIAL(Object.class, "<init>").INVOKE();
			mv.line();
			{
				mv.LOAD("this");
				mv.LOAD("jdbi");
				mv.PUTFIELD_OF_THIS("jdbi");
			}
			mv.line();
			mv.RETURN();
		});
	}

	private void make_insert(String insertSQL) {
		String lambdaName = "lambda$insert$2";

		cw.method("insert").parameter("data", targetClazz).reTurn(boolean.class).code(mv -> {
			mv.line();
			mv.LOAD("this");
			mv.GET_THIS_FIELD("jdbi");
			mv.LOAD("data");

			mv.STATIC(clazz, lambdaName)
				.parameter(Handle.class)
				.reTurn(GenericClazz.genericBase(Object.class, Integer.class))
				.LAMBDA(Jdbi.class.getName(), "withHandle")
				.parameter(targetClazz)
				.reTurn(HandleCallback.class)
				.INVOKE();

			mv.VIRTUAL(Jdbi.class, "withHandle").parameter(HandleCallback.class).reTurn(Object.class).INVOKE();
			mv.CHECKCAST(Integer.class);
			mv.VIRTUAL(Integer.class, "intValue").reTurn(int.class).INVOKE();
			mv.LOADConst(1);
			Label labelElse = mv.codeNewLabel();
			mv.IF_ICMPNE(labelElse);
			mv.LOADConst(1);
			mv.RETURNTop();
			mv.visitLabel(labelElse);
			mv.LOADConst(0);
			mv.RETURNTop();
		});

		cw.method(Opcodes.ACC_PUBLIC + Opcodes.ACC_BRIDGE + Opcodes.ACC_SYNTHETIC, "insert")
			.parameter("data", Object.class)
			.reTurn(boolean.class)
			.code(mv -> {
				mv.line();
				mv.LOAD("this");
				mv.LOAD("data");
				mv.CHECKCAST(targetClazz);
				mv.VIRTUAL(clazz, "insert").parameter(targetClazz).reTurn(boolean.class).INVOKE();
				mv.RETURNTop();
			});

		cw.method(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, lambdaName)
			.parameter("data", targetClazz)
			.parameter("handle", Handle.class)
			.reTurn(Integer.class)
			.tHrow(RuntimeException.class)
			.code(mv -> {
				mv.line();
				mv.LOAD(1);
				mv.LOADConst(insertSQL);
				mv.VIRTUAL(Handle.class, "createUpdate").parameter(String.class).reTurn(Update.class).INVOKE();
				{

					mv.LOAD("data");
					mv.STATIC(clazz, "bind")
						.parameter(Update.class)
						.parameter(targetClazz)
						.reTurn(Update.class)
						.INVOKE();
				}

				mv.line();
				mv.VIRTUAL(Update.class, "execute").reTurn(int.class).INVOKE();
				mv.STATIC(Integer.class, "valueOf").parameter(int.class).reTurn(Integer.class).INVOKE();

				mv.line();

				mv.RETURNTop();
			});
	}

	private void make_list(String querySQL) {
		String lambdaName = "lambda$list$0";

		cw.method("list")
			.parameter("start", int.class)
			.parameter("max", int.class)
			.reTurn(GenericClazz.generic(List.class, targetClazz))
			.code(mv -> {
				mv.line();
				mv.LOAD(0);
				mv.GET_THIS_FIELD("jdbi");

				mv.STATIC(clazz, lambdaName)
					.parameter(Handle.class)
					.reTurn(GenericClazz.genericBase(Object.class, List.class))
					.LAMBDA(Jdbi.class.getName(), "withHandle")
					.reTurn(HandleCallback.class)
					.INVOKE();

				mv.VIRTUAL(Jdbi.class, "withHandle")
					.parameter(HandleCallback.class)
					.reTurn(GenericClazz.genericBase(Object.class, targetClazz))
					.INVOKE();
				mv.CHECKCAST(List.class);
				mv.RETURNTop();
				Label l2 = mv.codeNewLabel();
				mv.visitLabel(l2);
			});

		cw.method(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, lambdaName)
			.parameter("handle", Handle.class)
			.reTurn(List.class)
			.tHrow(RuntimeException.class)
			.code(mv -> {
				mv.line();
				mv.LOAD("handle");
				mv.LOADConst(querySQL);
				mv.VIRTUAL(Handle.class, "createQuery").parameter(String.class).reTurn(Query.class).INVOKE();

				mv.GETSTATIC(clazz, "mapper", mapClazz);

				mv.VIRTUAL(Query.class, "map").parameter(RowMapper.class).reTurn(ResultIterable.class).INVOKE();
				mv.INTERFACE(ResultIterable.class, "list").reTurn(List.class).INVOKE();
				mv.RETURNTop();
			});
	}

	private void make_update(String updateSQL) {
		String lambdaName = "lambda$update$3";

		cw.method("update").parameter("data", targetClazz).reTurn(boolean.class).code(mv -> {
			mv.line();
			mv.LOAD("this");
			mv.GET_THIS_FIELD("jdbi");
			mv.LOAD("data");

			mv.STATIC(clazz, lambdaName)
				.parameter(Handle.class)
				.reTurn(GenericClazz.genericBase(Object.class, Integer.class))
				.LAMBDA(Jdbi.class.getName(), "withHandle")
				.parameter(targetClazz)
				.reTurn(HandleCallback.class)
				.INVOKE();

			mv.VIRTUAL(Jdbi.class, "withHandle").parameter(HandleCallback.class).reTurn(Object.class).INVOKE();
			mv.CHECKCAST(Integer.class);
			mv.VIRTUAL(Integer.class, "intValue").reTurn(int.class).INVOKE();

			mv.line();
			mv.LOADConst(1);
			mv.line();
			Label labelElse = mv.codeNewLabel();
			mv.IF_ICMPNE(labelElse);
			mv.LOADConst(1);
			mv.RETURNTop();
			mv.visitLabel(labelElse);
			mv.LOADConst(0);
			mv.RETURNTop();
		});

		cw.method(Opcodes.ACC_PUBLIC + Opcodes.ACC_BRIDGE + Opcodes.ACC_SYNTHETIC, "update")
			.parameter("data", Object.class)
			.reTurn(boolean.class)
			.code(mv -> {
				mv.line();
				mv.LOAD("this");
				mv.LOAD("data");
				mv.CHECKCAST(targetClazz);
				mv.VIRTUAL(clazz, "update").parameter(targetClazz).reTurn(boolean.class).INVOKE();
				mv.RETURNTop();
			});

		cw.method(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, lambdaName)
			.parameter("data", targetClazz)
			.parameter("handle", Handle.class)
			.reTurn(Integer.class)
			.tHrow(RuntimeException.class)
			.code(mv -> {

				mv.line();
				mv.LOAD(1);
				mv.LOADConst(updateSQL);
				mv.VIRTUAL(Handle.class, "createUpdate").parameter(String.class).reTurn(Update.class).INVOKE();

				mv.LOAD("data");
				mv.STATIC(clazz, "bind").parameter(Update.class).parameter(targetClazz).reTurn(Update.class).INVOKE();

				mv.line();
				mv.VIRTUAL(Update.class, "execute").reTurn(int.class).INVOKE();
				mv.STATIC(Integer.class, "valueOf").parameter(int.class).reTurn(Integer.class).INVOKE();

				mv.line();
				mv.RETURNTop();

			});
	}
}
