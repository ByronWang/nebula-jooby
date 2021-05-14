package nebula.module;

import cn.sj1.nebula.jdbc.builders.schema.ColumnDefinition;

public class FieldMapper {
	final boolean primaryKey;
	final String fieldName;
	final String getname;
	final Class<?> pojoClazz;
	final ColumnDefinition column;

	public FieldMapper(boolean primaryKey, String javaname, String getname, Class<?> fieldClazz,
			ColumnDefinition column) {
		super();
		this.primaryKey = primaryKey;
		this.fieldName = javaname;
		this.pojoClazz = fieldClazz;
		this.getname = getname;
		this.column = column;
		if(primaryKey) this.column.primarykey();
	}

	public FieldMapper(String javaname, String getname, Class<?> fieldClazz, ColumnDefinition column) {
		this(false, javaname, getname, fieldClazz, column);
	}

}