package nebula.module.user;

public class DefineType {
	long id;
	String name;
	String input;
	String superType;
	String description;
	int size;
	int digits;
	boolean required;
	String columnName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getSuperType() {
		return superType;
	}

	public void setSuperType(String superType) {
		this.superType = superType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDigits() {
		return digits;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public DefineType(long id, String name, String input, String superType, String description, int size, int digits,
			boolean required, String columnName) {
		super();
		this.id = id;
		this.name = name;
		this.input = input;
		this.superType = superType;
		this.description = description;
		this.size = size;
		this.digits = digits;
		this.required = required;
		this.columnName = columnName;
	}

}
