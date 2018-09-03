package nebula.module.systemTables;

import java.sql.Connection;
import java.sql.SQLException;

import nebula.data.jdbc.JdbcRepository;
import nebula.data.jdbc.PageList;

public class SystemTableJdbcRepository implements JdbcRepository<SystemTable> {
	Connection conn;

	@Override
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void initJdbc() throws SQLException {
		conn.createStatement().executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES");
//		
//				.map(new SystemTable.Mapper())
//				.list();
	}

	@Override
	public PageList<SystemTable> listJdbc(int start, int max) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemTable findByIdJdbc(Object... keys) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemTable insertJdbc(SystemTable pet) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemTable updateJdbc(SystemTable pet) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteJdbc(Object... keys) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
