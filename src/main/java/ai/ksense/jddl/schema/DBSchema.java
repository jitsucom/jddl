package ai.ksense.jddl.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Contains schema DDL
 */
public class DBSchema {
    private DBSchema() {
    }

    @JsonProperty(value = "tables", required = true)
    private List<Table> tables;
    @JsonIgnore
    private Map<String, Table> tablesByName;

    public DBSchema(List<Table> tables) {
        this.tables = tables;
        tablesByName = tables.stream().collect(Collectors.toMap(t -> t.getName().toUpperCase(), t -> t));
    }

    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public Table getTableByName(String name) {
        return tablesByName.get(name.toUpperCase());
    }
}
