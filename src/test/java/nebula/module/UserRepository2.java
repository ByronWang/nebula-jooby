package nebula.module;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;

public class UserRepository2 implements Repository<User> {
	Jdbi jdbi;

	public UserRepository2(Jdbi jdbi) {
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
			Update update = handle.createUpdate("INSERT INTO user(id, name) VALUES (:id, :name)");
			bind(update, pet);
			return update.execute();
		}) == 1;
	}

	@Override
	public boolean update(User pet) {
		return jdbi.withHandle(handle -> {
			Update update = handle.createUpdate("UPDATE user SET name=:name WHERE id=:id");
			bind(update, pet);
			return update.execute();
		}) == 1;
	}

	private void bind(Update update, User pet) {
		update.bind("id", pet.getId());
		update.bind("name", pet.getName());
	}

	@Override
	public boolean delete(long id) {
		return jdbi.withHandle(handle -> {
			return handle.createUpdate("DELETE user WHERE id=:id").bind("id", id).execute();
		}) == 1;
	}

}
