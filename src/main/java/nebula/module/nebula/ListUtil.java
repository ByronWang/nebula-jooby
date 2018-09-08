package nebula.module.nebula;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nebula.data.jdbc.ClazzDefinition;
import nebula.data.jdbc.FieldList;
import nebula.data.jdbc.FieldMapper;
import nebula.data.query.Condition;

public class ListUtil {

	// {"name":"34","q":"er"}
	static final String NAME = "\\\"([^\\\"]*)\\\"";
	static final String STRING = "\\\"[^\\\"]*\\\"";
	static final String INT = "([-|\\.|\\d]+)";
	static final String INTorSTRING = "((?:" + STRING + ")|" + INT + ")";

	static final Pattern filterPattern = Pattern.compile(String.format(",?%1$s:%2$s", NAME, INTorSTRING));

	public static Condition filter(ClazzDefinition clazz, String filter) {
		Matcher m = filterPattern.matcher(filter);
		FieldList fields = clazz.getFields();
		Condition condition = Condition.empty();
		while (m.find()) {
			String name = m.group(1);
			String value = m.group(2);
			System.out.println(name + " " + value);
			if (fields.containsKey(name)) {
				FieldMapper field = fields.get(name);
				if (field.getPojoClazz() == String.class) {
					value = value.substring(1, value.length()-1).replace("\\\"", "\"");
					condition = condition.and(Condition.field(name).contain(value));
				} else {
					condition = condition.and(Condition.field(name).eq(Integer.parseInt(value)));
				}
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
