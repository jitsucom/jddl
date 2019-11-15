package ai.ksense.jddl.schema;

public class ColumnBuilder {
    private final String name;
    private final String type;
    private boolean isNotNull;
    private String options;
    private String defaultValue;
    private boolean primaryKey;

    ColumnBuilder(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Column build() {
        return new Column(name, type, isNotNull, defaultValue, options);
    }

    public ColumnBuilder notNull(boolean notNull) {
        isNotNull = notNull;
        return this;
    }

    public ColumnBuilder options(String options) {
        this.options = options;
        return this;
    }

    public ColumnBuilder defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ColumnBuilder primaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public ColumnBuilder defaultValueIsNull() {
        return defaultValue("NULL");
    }
}
