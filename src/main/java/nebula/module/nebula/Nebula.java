package nebula.module.nebula;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.jooby.Env;
import org.jooby.Err;
import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.Router;
import org.jooby.Status;

import com.google.inject.Binder;
import com.typesafe.config.Config;

import cn.sj1.nebula.data.PageList;
import cn.sj1.nebula.data.Repository;
import cn.sj1.nebula.data.jdbc.JdbcRepositoryFactory;
import cn.sj1.nebula.data.jdbc.PersistentEntity;
import cn.sj1.nebula.data.jdbc.PersistentProperty;
import cn.sj1.nebula.data.query.Condition;
import cn.sj1.nebula.data.query.OrderBy;
import cn.sj1.nebula.data.query.OrderByOp;
import views.Field;
import views.Layout;

public class Nebula implements Jooby.Module {
	JdbcRepositoryFactory repositoryFactory;

	public Nebula() {
		super();
		System.out.println("make nebula");
	}

	public void configure(Env env, Config conf, Binder binder) {
//	    Key<DataSource> dskey = Key.get(DataSource.class, Names.named(name));
		DataSource dataSource = env.get(DataSource.class)
			.orElseThrow(() -> new NoSuchElementException("DataSource missing: " + DataSource.class));

		binder.bind(Nebula.class).toInstance(this);
		env.set(Nebula.class, this);

		try {
			this.repositoryFactory = new JdbcRepositoryFactory(dataSource.getConnection());

//			env.set(Nebula.class, this);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> void require(Env env, Class<T> clazz) {
		PersistentEntity clazzDefinition = repositoryFactory.build(clazz);
		String resourceName = clazzDefinition.getName();
		Router router = env.router();

		Class<T> actualClazz = repositoryFactory.getEntityImplClass(clazzDefinition);

		Repository<T> objectRepository = repositoryFactory.getRepository(clazzDefinition);
		objectRepository.init();

		final Layout simpleListView = buildSimpleListView(clazzDefinition);
		final Layout editView = buildEditView(clazzDefinition);
		final Layout showView = buildShowView(clazzDefinition);
		final Layout createView = buildCreateView(clazzDefinition);

		router.get("/views/" + resourceName, (req) -> {
			return simpleListView;
		});
		
		router.get("/views/" + resourceName + "/list", (req) -> {
			return simpleListView;
		});
		
		router.get("/views/" + resourceName + "/edit", (req) -> {
			return editView;
		});
		
		router.get("/views/" + resourceName + "/show", (req) -> {
			return showView;
		});
		
		router.get("/views/" + resourceName + "/create", (req) -> {
			return createView;
		});

		router.path("/api/" + resourceName, () -> {
			router.get((req, rsp) -> {
				Condition condition = ListUtil.filter(clazzDefinition, req.param("filter").value());
				OrderBy orderBy = orderby(clazzDefinition, req.param("sort").value());

				String map = req.param("range").value();
				String[] ra = map.substring(1, map.length() - 1).split(",");
				int start = Integer.parseInt(ra[0]);
				int max = Integer.parseInt(ra[1]);
				PageList<?> users = objectRepository.list(condition, orderBy, start, max);

				if (users.size() > 0) {
					rsp.header("Content-Range", "item " + users.getStart() + "-" + users.getMax() + "/" + (users.getTotalSize()));
				} else {
					rsp.header("Content-Range", "item 0-0/0");
				}

				rsp.send(users);
			});

			router.get("/:id", req -> {
				long id = req.param("id").longValue();

				T pet = objectRepository.findById(id);
				if (pet == null) {
					throw new Err(Status.NOT_FOUND);
				}
				return pet;
			});

			router.put("/:id", req -> {
				T inputObject;
				try {
					inputObject = req.body(actualClazz);
				} catch (Exception e) {
					throw new Err(Status.BAD_REQUEST, "input data has bad format");
				}

				T updatedObject = objectRepository.update(inputObject);
				if (updatedObject == null) {
					throw new Err(Status.NOT_FOUND);
				}
				return updatedObject;
			});

			router.post(req -> {
				T inputObject;
				try {
					inputObject = req.body(clazz);
				} catch (Exception e) {
					throw new Err(Status.BAD_REQUEST, "input data has bad format");
				}

				T insertedObject = objectRepository.insert(inputObject);
				return insertedObject;
			});

			router.put(req -> {
				T inputObject;
				try {
					inputObject = req.body(actualClazz);
				} catch (Exception e) {
					throw new Err(Status.BAD_REQUEST, "input data has bad format");
				}
				T updatedObject = objectRepository.update(inputObject);
				if (updatedObject == null) {
					throw new Err(Status.NOT_FOUND);
				}
				return updatedObject;
			});

			router.delete("/:id", req -> {
				long id = req.param("id").longValue();

				if (objectRepository.deleteById(id) < 1) {
					throw new Err(Status.NOT_FOUND);
				}
				return Results.noContent();
			});
		});
	}

	private Layout buildCreateView(PersistentEntity clazzDefinition) {
		List<Field> fields = new ArrayList<>();
		for (PersistentProperty c : clazzDefinition.getFields()) {
			fields.add(new Field(c.getFieldName(), "TextInput"));
		}
		final Layout view = new Layout(fields.toArray(new Field[0]));
		return view;
	}

	private Layout buildShowView(PersistentEntity clazzDefinition) {
		List<Field> fields = new ArrayList<>();
		for (PersistentProperty c : clazzDefinition.getFields()) {
			fields.add(new Field(c.getFieldName(), "TextField"));
		}
		final Layout view = new Layout(fields.toArray(new Field[0]));
		return view;
	}

	private Layout buildEditView(PersistentEntity clazzDefinition) {
		List<Field> fields = new ArrayList<>();
		for (PersistentProperty c : clazzDefinition.getFields()) {
			fields.add(new Field(c.getFieldName(), "TextInput"));
		}
		final Layout view = new Layout(fields.toArray(new Field[0]));
		return view;
	}

	private Layout buildSimpleListView(PersistentEntity clazzDefinition) {
		List<Field> fields = new ArrayList<>();
		for (PersistentProperty c : clazzDefinition.getFields()) {
			fields.add(new Field(c.getFieldName(), "TextField"));
		}
		final Layout view = new Layout(fields.toArray(new Field[0]));
		return view;
	}

	private OrderBy orderby(PersistentEntity clazzDefinition, String sort) {
		String[] aSort = sort.substring(1, sort.length() - 1).split(",");
		String orderbyName = aSort[0];
		orderbyName = orderbyName.substring(1, orderbyName.length() - 1);
		String strOp = aSort[1];
		strOp = strOp.substring(1, strOp.length() - 1);
		OrderByOp op = OrderByOp.valueOf(strOp);
		OrderBy orderBy = OrderBy.empty().andOrderBy(orderbyName, op);
		return orderBy;
	}
}
