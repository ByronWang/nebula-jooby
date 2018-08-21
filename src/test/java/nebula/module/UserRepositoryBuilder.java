package nebula.module;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;

import java.io.IOException;
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

public class UserRepositoryBuilder {

	private void bind(MethodCode mv, String targetClazz, String paramName, String propGetName, Class<?> propGetClazz) {
		{
			mv.line();
			mv.LOADConst(paramName);
			mv.LOAD("data");
			mv.VIRTUAL(targetClazz, propGetName).reTurn(propGetClazz).INVOKE();
			mv.VIRTUAL(Update.class, "bind")
				.parameter(String.class)
				.parameter(propGetClazz)
				.reTurn(SqlStatement.class)
				.INVOKE();
			mv.CHECKCAST(Update.class);
		}
	}

	private void bindName(MethodCode mv, String targetClazz, String paramName, String propGetName,
			Class<?> propGetClazz) {
		{
			mv.line();
			mv.LOADConst(paramName);
			mv.LOAD("data");
			mv.VIRTUAL(targetClazz, propGetName).reTurn(propGetClazz).INVOKE();
			mv.VIRTUAL(Update.class, "bind")
				.parameter(String.class)
				.parameter(propGetClazz)
				.reTurn(SqlStatement.class)
				.INVOKE();
			mv.CHECKCAST(Update.class);
		}
	}

	private void delete(String lambdaName, String idName, Class<?> idClazz) {
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
			mv.codeAccessLabel(labelElse);
			mv.LOADConst(0);
			mv.RETURNTop();
		});
	}

	private void deleteLambda(String lamdaName, String deleteSQL, String idName, Class<Long> idClazz) {
		cw.method(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, lamdaName)
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

	private void findById(String lambdaName, Class<?> idClazz) {
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
	}

	private void findbyidBridge(String idName, Class<?> idClazz) {
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
	}

	private void findbyidLamda(String targetClazz, String mapClazz, String findbyidSQL, String idName, Class<?> idClass,
			String lambdaName) {
		cw.method(Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC + Opcodes.ACC_SYNTHETIC, lambdaName)
			.parameter(idName, idClass)
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
					.parameter(idClass)
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

	private void init(ClassBody cw) {
		cw.method("<init>").parameter("jdbi", Jdbi.class).code(mv -> {
			Label l0 = mv.codeNewLabel();
			mv.codeAccessLabel(l0);
			mv.line();
			mv.LOAD(0);
			mv.SPECIAL(Object.class, "<init>").INVOKE();
			mv.line();
			mv.LOAD(0);
			mv.LOAD(1);
			mv.PUTFIELD_OF_THIS("jdbi");
			Label l2 = mv.codeNewLabel();
			mv.codeAccessLabel(l2);
			mv.line();
			mv.RETURN();
		});
	}

	private void insert(String lambdaName) {
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
			mv.codeAccessLabel(labelElse);
			mv.LOADConst(0);
			mv.RETURNTop();
		});
	}

	private void insertBridge() {
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
	}

	private void insertLambda(String insertSQL, String lambdaName) {
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

				bind(mv, targetClazz, "id", "getId", long.class);
				bind(mv, targetClazz, "name", "getName", String.class);

				mv.line();
				mv.VIRTUAL(Update.class, "execute").reTurn(int.class).INVOKE();
				mv.STATIC(Integer.class, "valueOf").parameter(int.class).reTurn(Integer.class).INVOKE();

				mv.line();

				mv.RETURNTop();
			});
	}

	private void list(String lambadaName) {
		cw.method("list")
			.parameter("start", int.class)
			.parameter("max", int.class)
			.reTurn(GenericClazz.generic(List.class, targetClazz))
			.code(mv -> {
				mv.line();
				mv.LOAD(0);
				mv.GET_THIS_FIELD("jdbi");

				mv.STATIC(clazz, lambadaName)
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
				mv.codeAccessLabel(l2);
			});
	}

	private void listLambda(String querySQL, String lambdaName) {
		cw.method(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, lambdaName)
			.parameter("handle", Handle.class)
			.reTurn(List.class)
			.tHrow(RuntimeException.class)
			.code(mv -> {
				mv.line();
				mv.LOAD("handle");
				mv.LOADConst(querySQL);
				mv.VIRTUAL(Handle.class, "createQuery").parameter(String.class).reTurn(Query.class).INVOKE();

				mv.NEW(mapClazz);
				mv.DUP();
				mv.SPECIAL(mapClazz, "<init>").INVOKE();

				mv.VIRTUAL(Query.class, "map").parameter(RowMapper.class).reTurn(ResultIterable.class).INVOKE();
				mv.INTERFACE(ResultIterable.class, "list").reTurn(List.class).INVOKE();
				mv.RETURNTop();
			});
	}

	ClassBody cw;
	String clazz;
	String targetClazz;
	String mapClazz;

	public byte[] make(String clazz, String targetClazz, String mapClazz) throws IOException {

		this.clazz = clazz;
		this.targetClazz = targetClazz;
		this.mapClazz = mapClazz;

		String idName = "id";
		Class<Long> idClazz = long.class;

		cw = ClassBuilder.make(clazz).imPlements(Repository.class, targetClazz).body();

		cw.referInnerClass(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, MethodHandles.class.getName(), "Lookup");
		cw.field(0, "jdbi", Jdbi.class);

		init(cw);

		{
			String lambdaName = "lambda$list$0";
			list(lambdaName);
			listLambda("SELECT * FROM user", lambdaName);
		}
		{
			String lambdaName = "lambda$findById$1";
			findById(lambdaName, idClazz);
			findbyidLamda(targetClazz, mapClazz, "SELECT * FROM user ORDER BY name WHERE id=:id", idName, idClazz,
					lambdaName);
			findbyidBridge(idName, idClazz);
		}

		{
			String lambdaName = "lambda$insert$2";
			insert(lambdaName);
			insertLambda("INSERT INTO user(id, name) VALUES (:id, :name)", lambdaName);
			insertBridge();
		}

		{
			String lamdaName = "lambda$update$3";
			update(lamdaName);
			updateLambda("UPDATE user SET name=:name WHERE id=:id", lamdaName);
			updateBridge();
		}

		{
			String lamdaName = "lambda$delete$4";
			delete(lamdaName, idName, idClazz);
			deleteLambda(lamdaName, "DELETE user WHERE id=:id", idName, idClazz);
		}

		return cw.end().toByteArray();

	}

	private void update(String lambdaName) {
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
			mv.codeAccessLabel(labelElse);
			mv.LOADConst(0);
			mv.RETURNTop();
		});
	}

	private void updateBridge() {
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
	}

	private void updateLambda(String updateSQL, String lamdaName) {
		cw.method(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, lamdaName)
			.parameter("data", targetClazz)
			.parameter("handle", Handle.class)
			.reTurn(Integer.class)
			.tHrow(RuntimeException.class)
			.code(mv -> {

				mv.line();
				mv.LOAD(1);
				mv.LOADConst(updateSQL);
				mv.VIRTUAL(Handle.class, "createUpdate").parameter(String.class).reTurn(Update.class).INVOKE();
				bind(mv, targetClazz, "id", "getId", long.class);
				bindName(mv, targetClazz, "name", "getName", String.class);

				mv.line();
				mv.VIRTUAL(Update.class, "execute").reTurn(int.class).INVOKE();
				mv.STATIC(Integer.class, "valueOf").parameter(int.class).reTurn(Integer.class).INVOKE();

				mv.line();
				mv.RETURNTop();

			});
	}
}
