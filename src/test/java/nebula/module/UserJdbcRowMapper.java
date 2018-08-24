package nebula.module;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserJdbcRowMapper implements JdbcRowMapper<User> {

	@Override
	public User map(ResultSet rs) throws SQLException {
		return new User(rs.getLong("id"), rs.getString("name"), rs.getString("description"));
	}

}
