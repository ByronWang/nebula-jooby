package nebula.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserJdbcRepository implements JdbcRepository<User> {
	private Connection conn;
	private UserJdbcRowMapper mapper = new UserJdbcRowMapper();

	@Override
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	@Override
	public List<User> listJdbc(int start, int max) throws SQLException {
		List<User> datas = new ArrayList<>();

		ResultSet resultSet = conn.prepareStatement("SELECT * FROM user").executeQuery();
		while (resultSet.next()) {
			datas.add(mapper.map(resultSet));
		}
		return datas;
	}

	@Override
	public User findByIdJdbc(long id) throws SQLException {
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		List<User> datas;
		datas = new ArrayList<>();
		preparedStatement = conn.prepareStatement("SELECT * FROM user ORDER BY name WHERE id=?");

		preparedStatement.setLong(1, id);

		resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			datas.add(mapper.map(resultSet));
		}
		return datas.get(0);
	}

	@Override
	public boolean insertJdbc(User data) throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO user(id, name,description) VALUES (?,?,?)");

		preparedStatement.setLong(1, data.getId());
		preparedStatement.setString(2, data.getName());
		preparedStatement.setString(3, data.getDescription());

		return preparedStatement.execute();
	}

	@Override
	public boolean updateJdbc(User data) throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement("UPDATE user SET name=?,description=? WHERE id=?");

		preparedStatement.setLong(3, data.getId());
		preparedStatement.setString(1, data.getName());
		preparedStatement.setString(2, data.getDescription());

		return preparedStatement.execute();
	}

	@Override
	public boolean deleteJdbc(long id) throws SQLException {
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement("DELETE user WHERE id=?");

		preparedStatement.setLong(1, id);

		return preparedStatement.execute();
	}

}
