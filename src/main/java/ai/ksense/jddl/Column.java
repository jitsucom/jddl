package ai.ksense.jddl;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Column {
    @JsonProperty(value = "name", required = true)
    private final String name;
    @JsonProperty(value = "type", required = true)
    private final String type;
    @JsonProperty(value = "non-null", required = false, defaultValue = "false")
    private boolean isNotNull;
    @JsonProperty(value = "options", required = false, defaultValue = "")
    private String options = null;
    @JsonProperty(value = "default", required = false)
    private String defaultValue = null;

    public Column(String name, String type) {
        this.name = name;
        this.type = type;
    }

    private Column() { name = type = null; }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public String getOptions() {
        return options;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public Column setNotNull(boolean notNull) {
        isNotNull = notNull;
        return this;
    }

    public Column setOptions(String options) {
        this.options = options;
        return this;
    }

    public Column setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public TableStatement toAddColumnStatement(String tableName) {
        return new TableStatement.AddColumn(tableName, this);
    }
}
