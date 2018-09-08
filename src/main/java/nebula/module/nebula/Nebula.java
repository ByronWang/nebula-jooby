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

import nebula.data.jdbc.ClazzDefinition;
import nebula.data.jdbc.FieldMapper;
import nebula.data.jdbc.PageList;
import nebula.data.jdbc.Repository;
import nebula.data.jdbc.RepositoryFactory;
import nebula.data.query.Condition;
import nebula.data.query.OrderBy;
import nebula.data.query.OrderByOp;
import views.DyncView;
import views.Field;

public class Nebula implements Jooby.Module {
	RepositoryFactory repositoryFactory;

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
			this.repositoryFactory = new RepositoryFactory(dataSource.getConnection());

//			env.set(Nebula.class, this);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> void require(Env env, Class<T> clazz) {
		String resourceName = clazz.getSimpleName();
		Router router = env.router();
		ClazzDefinition clazzDefinition = repositoryFactory.build(clazz);

		Class<T> clazzExtends = repositoryFactory.getClazzExtend(clazzDefinition);

		Repository<T> objectRepository = repositoryFactory.getRepository(clazzDefinition);
		objectRepository.init();

		List<Field> fields = new ArrayList<>();
		for (FieldMapper c : clazzDefinition.getFields()) {
			fields.add(new Field(c.getFieldName(), "TextInput"));
		}
		final DyncView view = new DyncView(fields.toArray(new Field[0]));

		router.get("/views/" + resourceName, (req) -> {
			return view;
		});

		router.path("/api/" + resourceName, () -> {
			router.get((req, rsp) -> {
//				String filter = req.param("filter").value();// filter={}&range=[0,9]&sort=["lastModification","ASC"]
				Condition condition = ListUtil.filter(clazzDefinition, req.param("filter").value());
//				String[] strConditions = filter.substring(1, filter.length() - 1).split(",");
//				for (String set : strConditions) {
//					String[] sets = set.split(":");
//					
//				}

				String sort = req.param("sort").value();
				String[] aSort = sort.substring(1, sort.length() - 1).split(",");
				String orderbyName = aSort[0];
				orderbyName = orderbyName.substring(1, orderbyName.length() - 1);
				String strOp = aSort[1];
				strOp = strOp.substring(1, strOp.length() - 1);
				OrderByOp op = OrderByOp.valueOf(strOp);
				OrderBy orderBy = OrderBy.empty().andOrderBy(orderbyName, op);

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
					inputObject = req.body(clazzExtends);
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
					inputObject = req.body(clazzExtends);
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

				if (objectRepository.delete(id) < 1) {
					throw new Err(Status.NOT_FOUND);
				}
				return Results.noContent();
			});
		});
	}
}
