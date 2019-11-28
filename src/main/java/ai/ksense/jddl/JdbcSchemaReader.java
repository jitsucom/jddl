package ai.ksense.jddl;

import ai.ksense.jddl.schema.*;
import com.google.common.base.Strings;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JdbcSchemaReader  {
    private Connection connection;

    public JdbcSchemaReader(Connection connection) {
        this.connection = connection;
    }

    public DBSchema getSchema() {
        List<Table> tables = new ArrayList<>();
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                final TableBuilder table = getTable(connection.getMetaData(), tableName);
                getIndexes(connection.getMetaData(), tableName).forEach(table::addIndex);
                tables.add(table.build());
            }
        } catch (SQLException e) {
            throw new JDDLException("Can't read table schema from JDBC connection", e);
        }
        return new DBSchema(tables);
    }

    private List<Index> getIndexes(DatabaseMetaData md, String tableName) {
        return Collections.emptyList();
    }


    private TableBuilder getTable(DatabaseMetaData metaData, String tableName) throws SQLException {
        ResultSet columnsRs = metaData.getColumns(null, null, tableName, null);
        List<Column> columns = new ArrayList<>();
        while (columnsRs.next()) {
            String columnName = columnsRs.getString("COLUMN_NAME");
            String datatype = columnsRs.getString("DATA_TYPE");
            String columnSize = columnsRs.getString("COLUMN_SIZE");
            columns.add(new Column(columnName, toDataType(datatype, columnSize)));
        }
        final TableBuilder table = DBSchemaBuilder.table(tableName);
        columns.forEach(table::addColumn);
        return table;
    }

    private String toDataType(String datatype, String columnSize) {
        return Strings.isNullOrEmpty(columnSize) ? datatype : String.format("%s(%s)", datatype, columnSize);
    }
}
