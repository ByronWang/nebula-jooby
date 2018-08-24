package nebula.module;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface JdbcRepository<T> extends Repository<T> {
	void setConnection(Connection conn);

	@Override
	default List<T> list(int start, int max) {
		try {
			return listJdbc(start, max);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	default T findById(long id) {
		try {
			return findByIdJdbc(id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	default boolean insert(T pet) {
		try {
			return insertJdbc(pet);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	default boolean update(T pet) {
		try {
			return updateJdbc(pet);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	default boolean delete(long id) {
		try {
			return deleteJdbc(id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	List<T> listJdbc(int start, int max) throws SQLException;

	T findByIdJdbc(long id) throws SQLException;

	boolean insertJdbc(T pet) throws SQLException;

	boolean updateJdbc(T pet) throws SQLException;

	boolean deleteJdbc(long id) throws SQLException;

}
