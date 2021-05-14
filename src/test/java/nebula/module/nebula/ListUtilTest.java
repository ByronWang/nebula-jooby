package nebula.module.nebula;

import static cn.sj1.nebula.jdbc.builders.schema.ColumnDefinition.INTEGER;
import static cn.sj1.nebula.jdbc.builders.schema.ColumnDefinition.VARCHAR;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.sj1.nebula.data.jdbc.EntityORMappingDefinition;
import cn.sj1.nebula.data.jdbc.EntityORMappingDefinitionList;
import cn.sj1.nebula.data.jdbc.FieldList;
import cn.sj1.nebula.data.query.CommonSQLConditionVisitor;
import cn.sj1.nebula.data.query.Condition;
import nebula.module.User;

public class ListUtilTest {

	@Test
	public void test() {
		FieldList clazzFields = new FieldList();
		clazzFields.push(new EntityORMappingDefinition("id", "getId", long.class, INTEGER("ID")));
		clazzFields.push(new EntityORMappingDefinition("name", "getName", String.class, VARCHAR("NAME")));
		clazzFields.push(new EntityORMappingDefinition("age", "getAge", int.class, INTEGER("age")));
		clazzFields.push(new EntityORMappingDefinition("description", "getDescription", String.class, VARCHAR("description")));
		EntityORMappingDefinitionList clazzDefinition = new EntityORMappingDefinitionList(User.class.getSimpleName(), User.class.getName(), User.class.getSimpleName(), clazzFields);

		assertEquals("NAME = 'myname'", str(ListUtil.filter(clazzDefinition, " {\"name\":\"myname\"}")));
//		assertEquals("NAME = 'my\"name'", str(ListUtil.filter(clazzDefinition, " {\"name\":\"my\\\"name\"}")));
		assertEquals("NAME = '34' AND (NAME LIKE '%er%' OR description LIKE '%er%')", str(ListUtil.filter(clazzDefinition, " {\"name\":\"34\",\"q\":\"er\"}")));
		assertEquals("NAME = '34' AND age = 15", str(ListUtil.filter(clazzDefinition, " {\"name\":\"34\",\"age\":15}")));
//		assertEquals("NAME = '34'", str(ListUtil.filter(clazzDefinition, " {\"name\":\"34\",\"q\":\"er\",\"q\":\"er\",\"q\":\"er\",\"q\":\"er\"}")));
	}

	private String str(Condition c) {
		CommonSQLConditionVisitor conditionVisitorImpl = new CommonSQLConditionVisitor();
		c.accept(conditionVisitorImpl);
		return conditionVisitorImpl.toString();
	}
}
