package nebula.module;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.core.statement.Update;

public class UserMapper implements RowMapper<User>, Binder<User> {

	@Override
	public User map(ResultSet rs, StatementContext ctx) throws SQLException {
		return new User(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
	}

	@Override
	public void bind(Update update, User pet) {
		update.bind("id", pet.getId());
		update.bind("name", pet.getName());
		update.bind("description", pet.getDescription());
	}
}
