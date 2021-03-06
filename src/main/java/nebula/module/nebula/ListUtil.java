package nebula.module.nebula;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdbi.v3.core.mapper.reflect.FieldMapper;

import cn.sj1.nebula.data.jdbc.FieldList;
import cn.sj1.nebula.data.jdbc.PersistentEntity;
import cn.sj1.nebula.data.jdbc.PersistentProperty;
import cn.sj1.nebula.data.query.Condition;
import cn.sj1.nebula.data.query.ConditionOp;

public class ListUtil {

	// {"name":"34","q":"er"}
	static final String NAME = "\\\"([^\\\"]*)\\\"";
	static final String STRING = "\\\"[^\\\"]*\\\"";
	static final String INT = "([-|\\.|\\d]+)";
	static final String INTorSTRING = "((?:" + STRING + ")|" + INT + ")";

	static final Pattern filterPattern = Pattern.compile(String.format(",?%1$s:%2$s", NAME, INTorSTRING));

	public static Condition filter(PersistentEntity clazz, String filter) {
		Matcher m = filterPattern.matcher(filter);
		FieldList fields = clazz.getFields();
		Condition condition = Condition.empty();
		while (m.find()) {
			String[] namePairs = m.group(1).split("_");
			String name = namePairs[0];
			String value = m.group(2);
			if (fields.containsKey(name)) {
				PersistentProperty f = fields.get(name);
				if (f.getPojoClazz() == String.class) {
					value = value.substring(1, value.length() - 1).replace("\\\"", "\"");
					ConditionOp op = ConditionOp.EQ;
					if (namePairs.length > 1) op = ConditionOp.valueOf(namePairs[1].toUpperCase());
					condition = condition.and(Condition.field(f.getColumn().name()).condition(op, value));
				} else {
					condition = condition.and(Condition.field(f.getColumn().name()).condition(ConditionOp.EQ, Integer.parseInt(value)));
				}
			} else if ("q".equals(name)) {
				Condition conditionQ = Condition.empty();
				value = value.substring(1, value.length() - 1).replace("\\\"", "\"");
				for (PersistentProperty f : fields) {
					if (f.getPojoClazz() == String.class) {
						conditionQ = conditionQ.or(Condition.field(f.getColumn().name()).condition(ConditionOp.CONTAIN, value));
					}
				}
				condition = condition.and(conditionQ);
			}
		}
		return condition;
	}

//	Pattern p = Pattern.compile(
//			visit("cw.visit", TYPE.INT, TYPE.ACCESS, TYPE.CLASSNAME, TYPE.STRING, TYPE.CLASSNAME, TYPE.CLAZZARRAY));
//	Matcher m = p.matcher(source);
//	while (m.find()) {
//		return m.group(3).replaceAll("\"", "").replaceAll("/", ".");
//	}
//	return null;
}
