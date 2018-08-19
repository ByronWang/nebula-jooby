package nebula.module;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class UserMapper implements RowMapper<User> {

	@Override
	public User map(ResultSet rs, StatementContext ctx) throws SQLException {
		return new User(rs.getInt("id"), rs.getString("name"));
	}

}
