package ai.ksense.jddl.schema;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBSchemaBuilder {
    private List<Table> tables = new ArrayList<>();

    public static ColumnBuilder column(String name, String type) {
        return new ColumnBuilder(name, type);
    }

    public static ColumnBuilder column(String name, String type, Object typeParams) {
        return column(name, String.format("%s(%s)", type, typeParams));
    }

    public static TableBuilder table(String tableName) {
        return new TableBuilder(tableName);
    }

    private DBSchemaBuilder addTable(TableBuilder table) {
        return addTable(table.build());
    }

    public static DBSchemaBuilder schema() {
        return new DBSchemaBuilder();
    }

    public static DBSchemaBuilder schema(Table table) {
        return schema().addTable(table);
    }

    public static IndexBuilder index(String ... columns) {
        return new IndexBuilder(Arrays.asList(columns));
    }

    public static DBSchemaBuilder schema(TableBuilder table) {
        return schema().addTable(table);
    }

    public DBSchemaBuilder addTable(Table table) {
        tables.add(table);
        return this;
    }

    public DBSchema build() {
        return new DBSchema(tables);
    }

}
