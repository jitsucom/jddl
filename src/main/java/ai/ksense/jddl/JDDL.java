package ai.ksense.jddl;

import ai.ksense.jddl.schema.DBSchema;
import ai.ksense.jddl.schema.Table;

import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JDDL {
    private final DBSchema expectedSchema;
    private final DBSchema actualSchema;
    private final DDLSyntaxSettings ddlSyntaxSettings = new DDLSyntaxSettings();


    public JDDL(Reader ymlReader, Connection connection)  {
        this(new YAMLTablesFactory(ymlReader).getSchema(), connection);
    }

    public JDDL(DBSchema expectedSchema, Connection connection) {
        this.expectedSchema = expectedSchema;
        this.actualSchema = new JdbcSchemaReader(connection).getSchema();
    }

    public JDDL(DBSchema expectedSchema, DBSchema actualSchema) {
        this.expectedSchema = expectedSchema;
        this.actualSchema = actualSchema;
    }

    private List<DDLStatement> generatePatchInternal() {
        List<DDLStatement> statements = new ArrayList<>();
        for (Table expectedTable : expectedSchema.getTables()) {
            Table actualTable = actualSchema.getTableByName(expectedTable.getName());
            if (actualTable == null) {
                statements.add(new DDLStatement.CreateTable(expectedTable.getName()));
                actualTable = new Table(expectedTable.getName(), Collections.emptyList());
            }
            statements.addAll(new TableDiffGenerator(actualTable, expectedTable).diff());
        }
        return statements;
    }

    public List<String> generatePatch(Map<String, String> placeholders) {
        Function<String, String> placeholdersExecutor = getPlaceholdersExecutor(placeholders);
        return generatePatchInternal().stream()
                .map(ddlStatement -> ddlStatement.toSQLStatement(ddlSyntaxSettings))
                .map(placeholdersExecutor)
                .collect(Collectors.toList());
    }

    private Function<String, String> getPlaceholdersExecutor(Map<String, String> placeholders) {
        return placeholders == null || placeholders.isEmpty() ?
                    s -> s : new Placeholders(placeholders)::apply;
    }

    public List<String> generatePatch() {
        return generatePatch(Collections.emptyMap());
    }

    public void applyChanges(Connection connection) {
        applyChanges(Collections.emptyMap(), connection);

    }

    public void applyChanges(Map<String, String> placeholders, Connection connection) {
        Function<String, String> placeholdersExecutor = getPlaceholdersExecutor(placeholders);
        generatePatchInternal().forEach(statementModel -> {
            String sql = placeholdersExecutor.apply(statementModel.toSQLStatement(ddlSyntaxSettings));
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            } catch (SQLException e) {
                throw new JDDLException("Can't execute '" + sql + "'", e);
            }
        });
    }

    public DDLSyntaxSettings getDdlSyntaxSettings() {
        return ddlSyntaxSettings;
    }
}
