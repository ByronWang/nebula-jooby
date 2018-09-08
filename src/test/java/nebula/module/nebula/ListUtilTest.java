package nebula.module.nebula;

import static nebula.jdbc.builders.schema.ColumnDefinition.INTEGER;
import static nebula.jdbc.builders.schema.ColumnDefinition.VARCHAR;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nebula.data.jdbc.ClazzDefinition;
import nebula.data.jdbc.FieldList;
import nebula.data.jdbc.FieldMapper;
import nebula.data.query.CommonSQLConditionVisitor;
import nebula.data.query.Condition;
import nebula.module.User;

public class ListUtilTest {

	@Test
	public void test() {
		FieldList clazzFields = new FieldList();
		clazzFields.push(new FieldMapper("id", "getId", long.class, INTEGER("ID")));
		clazzFields.push(new FieldMapper("name", "getName", String.class, VARCHAR("NAME")));
		clazzFields.push(new FieldMapper("age", "getAge", int.class, INTEGER("age")));
		clazzFields.push(new FieldMapper("description", "getDescription", String.class, VARCHAR("description")));
		ClazzDefinition clazzDefinition = new ClazzDefinition(User.class.getSimpleName(), User.class.getName(), User.class.getSimpleName(),
				clazzFields);

		assertEquals("name = 'myname'", str(ListUtil.filter(clazzDefinition, " {\"name\":\"myname\"}")));
//		assertEquals("name = 'my\"name'", str(ListUtil.filter(clazzDefinition, " {\"name\":\"my\\\"name\"}")));
		assertEquals("name = '34'", str(ListUtil.filter(clazzDefinition, " {\"name\":\"34\",\"q\":\"er\"}")));
		assertEquals("name = '34' AND age = 15", str(ListUtil.filter(clazzDefinition, " {\"name\":\"34\",\"age\":15}")));
//		assertEquals("name = '34'", str(ListUtil.filter(clazzDefinition, " {\"name\":\"34\",\"q\":\"er\",\"q\":\"er\",\"q\":\"er\",\"q\":\"er\"}")));
	}

	private String str(Condition c) {
		CommonSQLConditionVisitor conditionVisitorImpl = new CommonSQLConditionVisitor();
		c.accept(conditionVisitorImpl);
		return conditionVisitorImpl.toString();
	}
}
