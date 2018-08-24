package nebula.module.definedTables;

import java.util.List;

public class DefinedTable extends DefinedTableThin {

	private List<ColumnDefinition> columns;

	public List<ColumnDefinition> getColumns() {
		return columns;
	}

	public DefinedTable(String name, List<ColumnDefinition> columns) {
		super(name);
		this.columns = columns;
	}

}
