package ai.ksense.jddl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class DatabaseSchema {
    private DatabaseSchema() {
    }

    @JsonProperty(value = "tables", required = true)
    private List<Table> tables;
    @JsonIgnore
    private Map<String, Table> tablesByName;

    public DatabaseSchema(List<Table> tables) {
        this.tables = tables;
        tablesByName = tables.stream().collect(Collectors.toMap(t -> t.getName().toUpperCase(), t -> t));
    }

    List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    Table getTableByName(String name) {
        return tablesByName.get(name.toUpperCase());
    }
}
