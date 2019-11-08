package ai.ksense.jddl;

import com.google.common.base.Strings;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcSchemaReader implements SchemaReader {
    private Connection connection;

    public JdbcSchemaReader(Connection connection) {
        this.connection = connection;
    }

    @Override
    public DatabaseSchema getSchema() {
        List<Table> tables = new ArrayList<>();
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tables.add(getTable(connection.getMetaData(), tableName));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return new DatabaseSchema(tables);
    }

    private Table getTable(DatabaseMetaData metaData, String tableName) throws SQLException {
        ResultSet columnsRs = metaData.getColumns(null, null, tableName, null);
        List<Column> columns = new ArrayList<>();
        while (columnsRs.next()) {
            String columnName = columnsRs.getString("COLUMN_NAME");
            String datatype = columnsRs.getString("DATA_TYPE");
            String columnSize = columnsRs.getString("COLUMN_SIZE");
            columns.add(new Column(columnName, toDataType(datatype, columnSize)));
        }
        return new Table(tableName, columns);
    }

    private String toDataType(String datatype, String columnSize) {
        return Strings.isNullOrEmpty(columnSize) ? datatype : String.format("%s(%s)", datatype, columnSize);
    }
}
