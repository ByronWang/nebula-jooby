package nebula.module;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import static org.objectweb.asm.Opcodes.*;
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
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import nebula.tinyasm.ClassBuilder;
import nebula.tinyasm.data.ClassBody;
import nebula.tinyasm.data.GenericClazz;
import nebula.tinyasm.util.RefineCode;

public class UserRepositoryTest extends TestBase {

	String clazz = UserRepository.class.getName();

	@Test
	public void testPrint() throws IOException {
		System.out.println(RefineCode.refineCode(toString(clazz)));
	}

	@Test
	public void testConstructerEmpty() throws IOException {

		String targetClazz = User.class.getName();
		String mapClazz = "nebula/module/UserMapper";
		ClassBody cw = ClassBuilder.make(clazz).imPlements(Repository.class, targetClazz).body();

		cw.referInnerClass(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, MethodHandles.class.getName(), "Lookup");
		cw.field(0, "jdbi", Jdbi.class);

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

		cw.method("list")
			.parameter("start", int.class)
			.parameter("max", int.class)
			.reTurn(GenericClazz.generic(List.class, targetClazz))
			.code(mv -> {
				mv.line();
				mv.LOAD(0);
				mv.GET_THIS_FIELD("jdbi");

				mv.STATIC(clazz, "lambda$list$0")
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

		cw.method("findById").parameter("id", long.class).reTurn(targetClazz).code(mv -> {

			mv.line();
			mv.LOAD(0);
			mv.GET_THIS_FIELD("jdbi");

			mv.LOAD(1);
			mv.STATIC(clazz, "lambda$findById$1")
				.parameter(Handle.class)
				.reTurn(GenericClazz.genericBase(Object.class, targetClazz))
				.LAMBDA(Jdbi.class.getName(), "withHandle")
				.parameter(long.class)
				.reTurn(HandleCallback.class)
				.INVOKE();

			mv.VIRTUAL(Jdbi.class, "withHandle").parameter(HandleCallback.class).reTurn(Object.class).INVOKE();
			mv.CHECKCAST(targetClazz);
			mv.RETURNTop();

		});

		cw.method("insert").parameter("user", targetClazz).reTurn(boolean.class).code(mv -> {
			mv.line();
			mv.LOAD(0);
			mv.GET_THIS_FIELD("jdbi");
			mv.LOAD(1);

			mv.STATIC(clazz, "lambda$insert$2")
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

		cw.method("update").parameter("user", targetClazz).reTurn(boolean.class).code(mv -> {
			mv.line();
			mv.LOAD(0);
			mv.GET_THIS_FIELD("jdbi");
			mv.LOAD(1);

			mv.STATIC(clazz, "lambda$update$3")
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

		cw.method("delete").parameter("id", long.class).reTurn(boolean.class).code(mv -> {
			mv.line();
			mv.LOAD(0);
			mv.GET_THIS_FIELD("jdbi");
			mv.LOAD(1);

			mv.STATIC(clazz, "lambda$delete$4")
				.parameter(Handle.class)
				.reTurn(GenericClazz.genericBase(Object.class, Integer.class))
				.LAMBDA(Jdbi.class.getName(), "withHandle")
				.parameter(long.class)
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

		cw.method(Opcodes.ACC_PUBLIC + Opcodes.ACC_BRIDGE + Opcodes.ACC_SYNTHETIC, "findById")
			.parameter("id", long.class)
			.reTurn(Object.class)
			.code(mv -> {
				mv.line();
				mv.LOAD(0);
				mv.LOAD(1);
				mv.VIRTUAL(clazz, "findById").parameter(long.class).reTurn(targetClazz).INVOKE();
				mv.RETURNTop();
			});

		cw.method(Opcodes.ACC_PUBLIC + Opcodes.ACC_BRIDGE + Opcodes.ACC_SYNTHETIC, "update")
			.parameter("user", Object.class)
			.reTurn(boolean.class)
			.code(mv -> {
				mv.line();
				mv.LOAD(0);
				mv.LOAD(1);
				mv.CHECKCAST(targetClazz);
				mv.VIRTUAL(clazz, "update").parameter(targetClazz).reTurn(boolean.class).INVOKE();
				;
				mv.RETURNTop();
			});

		cw.method(Opcodes.ACC_PUBLIC + Opcodes.ACC_BRIDGE + Opcodes.ACC_SYNTHETIC, "insert")
			.parameter("user", Object.class)
			.reTurn(boolean.class)
			.code(mv -> {
				mv.line();
				mv.LOAD(0);
				mv.LOAD(1);
				mv.CHECKCAST(targetClazz);
				mv.VIRTUAL(clazz, "insert").parameter(targetClazz).reTurn(boolean.class).INVOKE();
				mv.RETURNTop();
			});


		cw.method(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, "lambda$insert$2")
			.parameter("user", targetClazz)
			.parameter("handle", Handle.class)
			.reTurn(Integer.class)
			.tHrow(RuntimeException.class)
			.code(mv -> {
				mv.line();
				mv.LOAD(1);
				mv.LOADConst("INSERT INTO user(id, name) VALUES (:id, :name)");
				mv.VIRTUAL(Handle.class, "createUpdate").parameter(String.class).reTurn(Update.class).INVOKE();
				{
					mv.line();
					mv.LOADConst("id");
					mv.LOAD(0);
					mv.VIRTUAL(targetClazz, "getId").reTurn(int.class).INVOKE();
					mv.VIRTUAL(Update.class, "bind")
						.parameter(String.class)
						.parameter(int.class)
						.reTurn(SqlStatement.class)
						.INVOKE();
					mv.CHECKCAST(Update.class);
				}
				{
					mv.line();
					mv.LOADConst("name");
					mv.LOAD(0);
					mv.VIRTUAL(targetClazz, "getName").reTurn(String.class).INVOKE();
					mv.VIRTUAL(Update.class, "bind")
						.parameter(String.class)
						.parameter(String.class)
						.reTurn(SqlStatement.class)
						.INVOKE();
					mv.CHECKCAST(Update.class);
				}

				mv.line();
				mv.VIRTUAL(Update.class, "execute").reTurn(int.class).INVOKE();
				mv.STATIC(Integer.class, "valueOf").parameter(int.class).reTurn(Integer.class).INVOKE();

				mv.line();

				mv.RETURNTop();
			});

		cw.method(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, "lambda$update$3")
			.parameter("user", targetClazz)
			.parameter("handle", Handle.class)
			.reTurn(Integer.class)
			.tHrow(RuntimeException.class)
			.code(mv -> {

				mv.line();
				mv.LOAD(1);
				mv.LOADConst("UPDATE user SET name=:name WHERE id=:id");
				mv.VIRTUAL(Handle.class, "createUpdate").parameter(String.class).reTurn(Update.class).INVOKE();
				{
					mv.line();
					mv.LOADConst("id");
					mv.LOAD(0);
					mv.VIRTUAL(targetClazz, "getId").reTurn(int.class).INVOKE();
					mv.VIRTUAL(Update.class, "bind")
						.parameter(String.class)
						.parameter(int.class)
						.reTurn(SqlStatement.class)
						.INVOKE();
					mv.CHECKCAST(Update.class);
				}
				{
					mv.line();
					mv.LOADConst("name");
					mv.LOAD(0);
					mv.VIRTUAL(targetClazz, "getName").reTurn(String.class).INVOKE();
					mv.VIRTUAL(Update.class, "bind")
						.parameter(String.class)
						.parameter(String.class)
						.reTurn(SqlStatement.class)
						.INVOKE();
					mv.CHECKCAST(Update.class);
				}

				mv.line();
				mv.VIRTUAL(Update.class, "execute").reTurn(int.class).INVOKE();
				mv.STATIC(Integer.class, "valueOf").parameter(int.class).reTurn(Integer.class).INVOKE();

				mv.line();
				mv.RETURNTop();

			});

		cw.method(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, "lambda$delete$4")
			.parameter("id", long.class)
			.parameter("handle", Handle.class)
			.reTurn(Integer.class)
			.tHrow(RuntimeException.class)
			.code(mv -> {
				mv.line();
				mv.LOAD(2);
				mv.LOADConst("DELETE user WHERE id=:id");
				mv.VIRTUAL(Handle.class, "createUpdate").parameter(String.class).reTurn(Update.class).INVOKE();
				mv.line();
				mv.LOADConst("id");
				mv.LOAD(0);
				mv.VIRTUAL(Update.class, "bind")
					.parameter(String.class)
					.parameter(long.class)
					.reTurn(SqlStatement.class)
					.INVOKE();
				mv.CHECKCAST(Update.class);

				mv.line();
				mv.VIRTUAL(Update.class, "execute").reTurn(int.class).INVOKE();
				mv.STATIC(Integer.class, "valueOf").parameter(int.class).reTurn(Integer.class).INVOKE();

				mv.line();
				mv.RETURNTop();
			});

		cw.method(Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC + Opcodes.ACC_SYNTHETIC, "lambda$findById$1")
			.parameter("id", long.class)
			.parameter("handle", Handle.class)
			.reTurn(targetClazz)
			.tHrow(RuntimeException.class)
			.code(mv -> {
				mv.line();
				mv.LOAD(2);
				mv.LOADConst("SELECT * FROM user ORDER BY name WHERE id=:id");
				mv.VIRTUAL(Handle.class, "createQuery").parameter(String.class).reTurn(Query.class).INVOKE();

				mv.LOADConst("id");
				mv.LOAD(0);
				mv.VIRTUAL(Query.class, "bind")
					.parameter(String.class)
					.parameter(long.class)
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

		cw.method(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, "lambda$list$0")
			.parameter("handle", Handle.class)
			.reTurn(List.class)
			.tHrow(RuntimeException.class)
			.code(mv -> {
				mv.line();
				mv.LOAD(0);
				mv.LOADConst("SELECT * FROM user");
				mv.VIRTUAL(Handle.class, "createQuery").parameter(String.class).reTurn(Query.class).INVOKE();

				mv.NEW(mapClazz);
				mv.DUP();
				mv.SPECIAL(mapClazz, "<init>").INVOKE();

				mv.VIRTUAL(Query.class, "map").parameter(RowMapper.class).reTurn(ResultIterable.class).INVOKE();
				mv.INTERFACE(ResultIterable.class, "list").reTurn(List.class).INVOKE();
				mv.RETURNTop();
			});

		String codeActual = toString(cw.end().toByteArray());
		String codeExpected = toString(clazz);
		assertEquals("Code", codeExpected, codeActual);

	}
}
