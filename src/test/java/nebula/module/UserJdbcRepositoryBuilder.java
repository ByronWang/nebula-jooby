package nebula.module;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Label;

import nebula.tinyasm.ClassBuilder;
import nebula.tinyasm.data.ClassBody;
import nebula.tinyasm.data.GenericClazz;

public class UserJdbcRepositoryBuilder {

	ClassBody cw;

	String clazz;

	String targetClazz;

	String mapClazz;

	public byte[] make(String clazz, String clazzTarget, String clazzRowMapper) throws IOException {

		this.clazz = clazz;
		this.targetClazz = clazzTarget;
		this.mapClazz = clazzRowMapper;

		String idName = "id";
		Class<Long> clazzID = long.class;

		cw = ClassBuilder.make(clazz).imPlements(JdbcRepository.class, clazzTarget).body();

		cw.field(ACC_PRIVATE, "conn", Connection.class);

		cw.field(ACC_PRIVATE, "mapper", this.mapClazz);

		init();

		setConnection();

		listJdbc(clazzTarget);

		findByIdJdbc(clazzTarget, clazzID);

		insertJdbc(clazzID);

		updateJdbc(clazzID);

		deleteJdbc(clazzID);

		cw.method(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "findByIdJdbc")
			.parameter("id", clazzID)
			.reTurn(Object.class)
			.tHrow(SQLException.class)
			.code(mv -> {
				mv.line();
				mv.LOAD("this");
				mv.LOAD("id");
				mv.VIRTUAL(this.clazz, "findByIdJdbc").parameter(clazzID).reTurn(this.targetClazz).INVOKE();
				mv.RETURNTop();
			});

		cw.method(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "updateJdbc")
			.parameter("data", Object.class)
			.reTurn(boolean.class)
			.tHrow(SQLException.class)/* new String[] { "java.sql.SQLException" } */
			.code(mv -> {
				mv.line();
				mv.LOAD("this");
				mv.LOAD("data");
				mv.CHECKCAST(this.targetClazz);
				mv.VIRTUAL(this.clazz, "updateJdbc").parameter(this.targetClazz).reTurn(boolean.class).INVOKE();
				mv.RETURNTop();
			});

		cw.method(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "insertJdbc")
			.parameter("data", Object.class)
			.reTurn(boolean.class)
			.tHrow(SQLException.class)
			.code(mv -> {
				mv.line();
				mv.LOAD("this");
				mv.LOAD("data");
				mv.CHECKCAST(this.targetClazz);
				mv.VIRTUAL(this.clazz, "insertJdbc").parameter(this.targetClazz).reTurn(boolean.class).INVOKE();
				mv.RETURNTop();
			});

		return cw.end().toByteArray();
	}

	private void deleteJdbc(Class<Long> clazzID) {
		cw.method(ACC_PUBLIC, "deleteJdbc")
			.parameter("id", clazzID)
			.reTurn(boolean.class)
			.tHrow(SQLException.class)
			.code(mv -> {
				mv.define("preparedStatement", PreparedStatement.class);
				{
					mv.line();
					mv.LOAD("this");
					mv.GETFIELD("conn", Connection.class);
					mv.LOADConst("DELETE user WHERE id=?");
					mv.INTERFACE(Connection.class, "prepareStatement")
						.parameter(String.class)
						.reTurn(PreparedStatement.class)
						.INVOKE();
					mv.STORE("preparedStatement");
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.LOADConst(1);
					mv.LOAD("id");
					mv.INTERFACE(PreparedStatement.class, "setLong").parameter(int.class, clazzID).INVOKE();
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.INTERFACE(PreparedStatement.class, "execute").reTurn(boolean.class).INVOKE();
					mv.RETURNTop();
				}
			});
	}

	private void updateJdbc(Class<Long> clazzID) {
		cw.method(ACC_PUBLIC, "updateJdbc")
			.parameter("data", this.targetClazz)
			.reTurn(boolean.class)
			.tHrow(SQLException.class)
			.code(mv -> {
				mv.define("preparedStatement", PreparedStatement.class);
				{
					mv.line();
					mv.LOAD("this");
					mv.GETFIELD("conn", Connection.class);
					mv.LOADConst("UPDATE user SET name=?,description=? WHERE id=?");
					mv.INTERFACE(Connection.class, "prepareStatement")
						.parameter(String.class)
						.reTurn(PreparedStatement.class)
						.INVOKE();
					mv.STORE("preparedStatement");
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.LOADConst(3);
					mv.LOAD("data");
					mv.VIRTUAL(this.targetClazz, "getId").reTurn(clazzID).INVOKE();
					mv.INTERFACE(PreparedStatement.class, "setLong").parameter(int.class, clazzID).INVOKE();
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.LOADConst(1);
					mv.LOAD("data");
					mv.VIRTUAL(this.targetClazz, "getName").reTurn(String.class).INVOKE();
					mv.INTERFACE(PreparedStatement.class, "setString").parameter(int.class, String.class).INVOKE();
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.LOADConst(2);
					mv.LOAD("data");
					mv.VIRTUAL(this.targetClazz, "getDescription").reTurn(String.class).INVOKE();
					mv.INTERFACE(PreparedStatement.class, "setString").parameter(int.class, String.class).INVOKE();
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.INTERFACE(PreparedStatement.class, "execute").reTurn(boolean.class).INVOKE();
					mv.RETURNTop();
				}
			});
	}

	private void insertJdbc(Class<Long> clazzID) {
		cw.method(ACC_PUBLIC, "insertJdbc")
			.parameter("data", this.targetClazz)
			.reTurn(boolean.class)
			.tHrow(SQLException.class)/* new String[] { "java.sql.SQLException" } */
			.code(mv -> {
				mv.define("preparedStatement", PreparedStatement.class);
				{
					mv.line();
					mv.LOAD("this");
					mv.GETFIELD("conn", Connection.class);
					mv.LOADConst("INSERT INTO user(id, name,description) VALUES (?,?,?)");
					mv.INTERFACE(Connection.class, "prepareStatement")
						.parameter(String.class)
						.reTurn(PreparedStatement.class)
						.INVOKE();
					mv.STORE("preparedStatement");
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.LOADConst(1);
					mv.LOAD("data");
					mv.VIRTUAL(this.targetClazz, "getId").reTurn(clazzID).INVOKE();
					mv.INTERFACE(PreparedStatement.class, "setLong").parameter(int.class, clazzID).INVOKE();
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.LOADConst(2);
					mv.LOAD("data");
					mv.VIRTUAL(this.targetClazz, "getName").reTurn(String.class).INVOKE();
					mv.INTERFACE(PreparedStatement.class, "setString").parameter(int.class, String.class).INVOKE();
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.LOADConst(3);
					mv.LOAD("data");
					mv.VIRTUAL(this.targetClazz, "getDescription").reTurn(String.class).INVOKE();
					mv.INTERFACE(PreparedStatement.class, "setString").parameter(int.class, String.class).INVOKE();
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.INTERFACE(PreparedStatement.class, "execute").reTurn(boolean.class).INVOKE();
					mv.RETURNTop();
				}
			});
	}

	private void findByIdJdbc(String clazzTarget, Class<Long> clazzID) {
		cw.method(ACC_PUBLIC, "findByIdJdbc")
			.parameter("id", clazzID)
			.reTurn(this.targetClazz)
			.tHrow(SQLException.class)
			.code(mv -> {
				mv.define("preparedStatement", PreparedStatement.class);
				mv.define("resultSet", ResultSet.class);
				mv.define("datas", GenericClazz.generic(List.class, clazzTarget));
				{
					mv.line();
					mv.NEW(ArrayList.class);
					mv.DUP();
					mv.SPECIAL(ArrayList.class, "<init>").INVOKE();
					mv.STORE("datas");
				}
				{
					mv.line();
					mv.LOAD("this");
					mv.GETFIELD("conn", Connection.class);
					mv.LOADConst("SELECT * FROM user ORDER BY name WHERE id=?");
					mv.INTERFACE(Connection.class, "prepareStatement")
						.parameter(String.class)
						.reTurn(PreparedStatement.class)
						.INVOKE();
					mv.STORE("preparedStatement");
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.LOADConst(1);
					mv.LOAD("id");
					mv.INTERFACE(PreparedStatement.class, "setLong").parameter(int.class, clazzID).INVOKE();
				}
				{
					mv.line();
					mv.LOAD("preparedStatement");
					mv.INTERFACE(PreparedStatement.class, "executeQuery").reTurn(ResultSet.class).INVOKE();
					mv.STORE("resultSet");
				}
				mv.line();
				Label whileStart = mv.codeNewLabel();
				Label whileCause = mv.codeNewLabel();
				Label whileEnd = mv.codeNewLabel();
				mv.GOTO(whileCause);
				mv.visitLabel(whileStart);
				{
					mv.line();
					mv.LOAD("datas");
					mv.LOAD("this");
					mv.GETFIELD("mapper", this.mapClazz);
					mv.LOAD("resultSet");
					mv.VIRTUAL(this.mapClazz, "map").parameter(ResultSet.class).reTurn(this.targetClazz).INVOKE();
					mv.INTERFACE(List.class, "add").parameter(Object.class).reTurn(boolean.class).INVOKE();
					mv.POP();
				}
				mv.line();
				mv.visitLabel(whileCause);
				{
					mv.LOAD("resultSet");
					mv.INTERFACE(ResultSet.class, "next").reTurn(boolean.class).INVOKE();
					mv.IFNE(whileStart);
				}
				mv.visitLabel(whileEnd);
				{
					mv.line();
					mv.LOAD("datas");
					mv.LOADConst(0);
					mv.INTERFACE(List.class, "get").parameter(int.class).reTurn(Object.class).INVOKE();
					mv.CHECKCAST(this.targetClazz);
					mv.RETURNTop();
				}
			});
	}

	private void listJdbc(String clazzTarget) {
		cw.method(ACC_PUBLIC, "listJdbc")
			.parameter("start", int.class)
			.parameter("max", int.class)
			.reTurn(GenericClazz.generic(List.class, clazzTarget))
			.tHrow(SQLException.class)
			.code(mv -> {
				mv.define("datas", GenericClazz.generic(List.class, clazzTarget));
				mv.define("resultSet", ResultSet.class);
				{
					mv.line();
					mv.NEW(ArrayList.class);
					mv.DUP();
					mv.SPECIAL(ArrayList.class, "<init>").INVOKE();
					mv.STORE("datas");
				}
				{
					mv.line();
					mv.LOAD("this");
					mv.GETFIELD("conn", Connection.class);
					mv.LOADConst("SELECT * FROM user");
					mv.INTERFACE(Connection.class, "prepareStatement")
						.parameter(String.class)
						.reTurn(PreparedStatement.class)
						.INVOKE();
					mv.INTERFACE(PreparedStatement.class, "executeQuery").reTurn(ResultSet.class).INVOKE();
					mv.STORE("resultSet");
				}
				mv.line();
				Label whileStart = mv.codeNewLabel();
				Label whileCause = mv.codeNewLabel();
				Label whileEnd = mv.codeNewLabel();
				mv.GOTO(whileCause);
				mv.visitLabel(whileStart);
				{
					mv.line();
					mv.LOAD("datas");
					mv.LOAD("this");
					mv.GETFIELD("mapper", this.mapClazz);
					mv.LOAD("resultSet");
					mv.VIRTUAL(this.mapClazz, "map").parameter(ResultSet.class).reTurn(this.targetClazz).INVOKE();
					mv.INTERFACE(List.class, "add").parameter(Object.class).reTurn(boolean.class).INVOKE();
					mv.POP();
				}
				mv.line();
				mv.visitLabel(whileCause);
				{
					mv.LOAD("resultSet");
					mv.INTERFACE(ResultSet.class, "next").reTurn(boolean.class).INVOKE();
					mv.IFNE(whileStart);
				}
				mv.visitLabel(whileEnd);
				{
					mv.line();
					mv.LOAD("datas");
					mv.RETURNTop();
				}
			});
	}

	private void setConnection() {
		cw.method(ACC_PUBLIC, "setConnection").parameter("conn", Connection.class).code(mv -> {
			{
				mv.line();
				mv.LOAD("this");
				mv.LOAD("conn");
				mv.PUTFIELD("conn", Connection.class);
				mv.line();
				mv.RETURN();
			}
		});
	}

	private void init() {
		cw.method(ACC_PUBLIC, "<init>").code(mv -> {
			{
				mv.line();
				mv.LOAD("this");
				mv.SPECIAL(Object.class, "<init>").INVOKE();
			}
			{
				mv.line();
				mv.LOAD("this");
				mv.NEW(this.mapClazz);
				mv.DUP();
				mv.SPECIAL(this.mapClazz, "<init>").INVOKE();
				mv.PUTFIELD("mapper", this.mapClazz);
			}
			{
				mv.line();
				mv.RETURN();
			}
		});
	}

}
