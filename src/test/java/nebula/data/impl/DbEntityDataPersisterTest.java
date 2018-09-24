package nebula.data.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nebula.data.DataSession;
import nebula.data.DataStore;
import nebula.data.Entity;
import nebula.data.TransactionCaller;
import nebula.lang.SystemTypeLoader;

public class DbEntityDataPersisterTest extends TestCase {

	protected void setUp() throws Exception {

		TypeDatastore typeDatastore = new TypeDatastore(new SystemTypeLoader());

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testDefine() throws Exception {

	}
}
