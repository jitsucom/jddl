package ai.ksense.jddl;

import com.google.common.io.CharStreams;

import javax.imageio.IIOException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JDDL {
    private final DatabaseSchema expectedSchema;
    private final Connection connection;
    private final DatabaseSchema actualSchema;

    public JDDL(Reader ymlReader, Connection connection) throws IOException {
        this(new YAMLTablesFactory(ymlReader).getSchema(), connection);
    }

    public JDDL(Reader ymlReader, Connection connection, Map<String, String> placeholders) throws IOException {
        this(new YAMLTablesFactory(placehold(ymlReader, placeholders)).getSchema(), connection);
    }

    private static Reader placehold(Reader ymlReader, Map<String, String> placeholders) throws IOException {
        return new StringReader(new Placeholders(placeholders).apply(CharStreams.toString(ymlReader), true));
    }

    public JDDL(DatabaseSchema expectedSchema, Connection connection) {
        this.expectedSchema = expectedSchema;
        this.connection = connection;
        this.actualSchema = new JdbcSchemaReader(connection).getSchema();
    }

    private List<TableStatement> generatePatchInternal() {
        List<TableStatement> statements = new ArrayList<>();
        for (Table expectedTable : expectedSchema.getTables()) {
            Table actualTable = actualSchema.getTableByName(expectedTable.getName());
            if (actualTable == null) {
                statements.add(new TableStatement.CreateTable(expectedTable.getName()));
                actualTable = new Table(expectedTable.getName(), Collections.emptyList());
            }
            statements.addAll(new TableDiffGenerator(actualTable, expectedTable).diff());
        }
        return statements;
    }

    public List<String> generatePatch() {
        return generatePatchInternal().stream().map(TableStatement::toSQLStatement).collect(Collectors.toList());
    }

    public void applyChanges() {
        generatePatchInternal().forEach(statementModel -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute(statementModel.toSQLStatement());
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }


}
