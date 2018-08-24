package nebula.module;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcRowMapper<T> {
	T map(ResultSet rs) throws SQLException;
}
