package nebula.module;

import org.jdbi.v3.core.statement.Update;

public interface Binder<T> {
	void bind(Update update, T pet);
}
