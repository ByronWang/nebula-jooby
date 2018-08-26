package nebula.module.definedTables;

public class ColumnDefinition {

	private String name;
	private int datatype;
	private String typeName;
	private int size = -1;
	private int digits = -1;
	private boolean nullable = true;
	private boolean autoIncrements = false;

	public String getColumnName() {
		return name;
	}

	public int getDataType() {
		return datatype;
	}

	public int getSize() {
		return size;
	}

	public int getDigits() {
		return digits;
	}

	public boolean isNullable() {
		return nullable;
	}

	public boolean isAutoIncrment() {
		return autoIncrements;
	}

	public ColumnDefinition(String columnName, int dataType, String typeName, int size, int decimalDigits,
			boolean isNullable, boolean isAutoIncrment) {
		super();
		this.name = columnName;
		this.datatype = dataType;
		this.typeName = typeName;
		this.size = size;
		this.digits = decimalDigits;
		this.nullable = isNullable;
		this.autoIncrements = isAutoIncrment;
	}

	public String getTypeName() {
		return typeName;
	}

}
