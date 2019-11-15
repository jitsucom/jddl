package ai.ksense.jddl.schema;

import ai.ksense.jddl.DDLStatement;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Column {
    @JsonProperty(value = "name", required = true)
    private final String name;
    @JsonProperty(value = "type", required = true)
    private final String type;
    @JsonProperty(value = "non-null", required = false, defaultValue = "false")
    private final boolean isNotNull;
    @JsonProperty(value = "options", required = false, defaultValue = "")
    private final String options;
    @JsonProperty(value = "default", required = false)
    private final String defaultValue;

    public Column(String name, String type, boolean isNotNull) {
        this(name, type, isNotNull, null, null);
    }

    public Column(String name, String type) {
        this(name, type, false, null, null);
    }

    public Column(String name, String type, String defaultValue) {
        this(name, type, false, defaultValue, null);
    }

    public Column(String name, String type, boolean isNotNull, String defaultValue) {
        this(name, type, isNotNull, defaultValue, null);
    }

    public Column(String name, String type, boolean isNotNull, String defaultValue, String options) {
        this.name = name;
        this.type = type;
        this.isNotNull = isNotNull;
        this.options = options;
        this.defaultValue = defaultValue;
    }

    /**
     * C constructor for Jackson reflection
     */
    private Column() {
        name = type = options = defaultValue = null;
        isNotNull = false;
    }

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


    public DDLStatement toAddColumnStatement(String tableName) {
        return new DDLStatement.AddColumn(tableName, this);
    }
}
