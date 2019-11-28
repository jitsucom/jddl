package ai.ksense.jddl.schema;

import java.util.ArrayList;
import java.util.List;

public class TableBuilder {
    private List<Column> columns = new ArrayList<>();

    private final String name;

    TableBuilder(String name) {
        this.name = name;
    }

    public TableBuilder addColumn(Column column) {
        columns.add(column);
        return this;
    }

    public TableBuilder addColumn(ColumnBuilder column) {
        columns.add(column.build());
        return this;
    }

    public TableBuilder addIndex(Index index) {
        return this;
    }
    public TableBuilder addIndex(IndexBuilder index) {
        return this;
    }
    public TableBuilder primaryKey(String ... columns) {
        return this;
    }

    public Table build() {
        return new Table(name, columns);
    }
}
