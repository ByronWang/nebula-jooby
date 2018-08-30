package nebula.module.user;

import java.util.NoSuchElementException;

import org.jooby.Env;
import org.jooby.Jooby;

import com.google.inject.Binder;
import com.typesafe.config.Config;

public class UserModule implements Jooby.Module {
	public void configure(Env env, Config conf, Binder binder) {
//		final Holder holder = new Holder();
		/** Create table + users: */
		Nebula nebula = env.get(Nebula.class)
			.orElseThrow(() -> new NoSuchElementException("Nebula missing: " + Nebula.class));
		nebula.require(env, SimpleUser.class);
		nebula.require(env, Page.class);
		nebula.require(env, DefineType.class);
	}

}