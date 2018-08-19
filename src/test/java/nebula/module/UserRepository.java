package nebula.module;

import java.util.List;

import org.jdbi.v3.core.Jdbi;

public class UserRepository implements Repository<User> {
	Jdbi jdbi;

	public UserRepository(Jdbi jdbi) {
		this.jdbi = jdbi;
	}

	@Override
	public List<User> list(int start, int max) {
		return jdbi.withHandle(handle -> {
			return handle.createQuery("SELECT * FROM user").map(new UserMapper()).list();
		});
	}

	@Override
	public User findById(long id) {
		return jdbi.withHandle(handle -> {
			return handle.createQuery("SELECT * FROM user ORDER BY name WHERE id=:id")
				.bind("id", id)
				.map(new UserMapper())
				.findOnly();
		});
	}

	@Override
	public boolean insert(User pet) {
		return jdbi.withHandle(handle -> {
			return handle.createUpdate("INSERT INTO user(id, name) VALUES (:id, :name)")
				.bind("id", pet.getId())
				.bind("name", pet.getName())
				.execute();
		}) == 1;
	}

	@Override
	public boolean update(User pet) {
		return jdbi.withHandle(handle -> {
			return handle.createUpdate("UPDATE user SET name=:name WHERE id=:id")
				.bind("id", pet.getId())
				.bind("name", pet.getName())
				.execute();
		}) == 1;
	}

	@Override
	public boolean delete(long id) {
		return jdbi.withHandle(handle -> {
			return handle.createUpdate("DELETE user WHERE id=:id")
				.bind("id", id)
				.execute();
		}) == 1;
	}

}
