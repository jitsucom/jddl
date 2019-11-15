package ai.ksense.jddl;

import ai.ksense.jddl.schema.Table;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JdbcSchemaReaderTest extends H2Test {
    @Test
    public void test() throws IOException, SQLException {
        try (Connection connection = getConnection()) {
            createTables(connection);
        }
        try (Connection connection = getConnection()) {
            List<Table> tables = new JdbcSchemaReader(connection).getSchema().getTables();
            Assert.assertEquals(1, tables.size());
            Table table = tables.iterator().next();
            Assert.assertEquals(4, table.getColumns().size());
        }
    }

    private void createTables(Connection connection) {
        List<Table> tables = read("/jddl_v1.yml");
        for (Table table : tables) {
            List<DDLStatement> statements = table.createTable(true);
            statements.forEach(System.out::println);
            statements.forEach(st -> {
                try {
                    connection.createStatement().execute(st.toSQLStatement());
                } catch (SQLException e) {
                    throw new JDDLException("Can't execute '" + st.toSQLStatement() + "'", e);
                }
            });
        }
    }



    public List<Table> read(String classpath) {
        try (InputStreamReader r = new InputStreamReader(getClass().getResourceAsStream(classpath))) {
            return new YAMLTablesFactory(r).getSchema().getTables();
        } catch (IOException e) {
            throw new JDDLException(e.getMessage(), e);
        }
    }

}