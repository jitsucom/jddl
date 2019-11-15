package ai.ksense.jddl.schema;

import ai.ksense.jddl.DDLStatement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.*;
import java.util.stream.Collectors;

public class Table {
    @JsonProperty(value = "table", required = true)
    private String name;
    @JsonProperty(value = "columns", required = false)
    private List<Column> columns;
    private Map<String, Column> columnsByName;

    public Table() {
    }

    public Table(String name, List<Column> columns) {
        this.name = name;
        setColumns(columns);
    }

    public Column getColumnByName(String column) {
        return columnsByName.get(column.toUpperCase());
    }

    public String getName() {
        return name;
    }

    @JsonSetter
    public void setColumns(List<Column> columns) {
        this.columns = columns;
        columnsByName = columns.stream().collect(Collectors.toMap(c -> c.getName().toUpperCase(), c -> c));
    }

    public List<Column> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    public List<DDLStatement> createTable(boolean includeCreateTable) {
        ArrayList<DDLStatement> statements = new ArrayList<>(includeCreateTable ? Collections.singletonList(new DDLStatement.CreateTable(name)) : Collections.emptyList());
        statements.addAll(columns.stream().map(c -> c.toAddColumnStatement(name)).collect(Collectors.toList()));
        return statements;
    }
}
