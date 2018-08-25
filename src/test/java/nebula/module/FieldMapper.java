package nebula.module;

import nebula.jdbc.builders.schema.ColumnDefination;

class FieldMapper {
	String pojoName;
	String pojoType;
	ColumnDefination column;

	public FieldMapper(String javaname, String javatype, ColumnDefination column) {
		super();
		this.pojoName = javaname;
		this.pojoType = javatype;
		this.column = column;
	}

	public FieldMapper(String javaname, Class<?> javatype, ColumnDefination column) {
		super();
		this.pojoName = javaname;
		this.pojoType = javatype.getName();
		this.column = column;
	}

}