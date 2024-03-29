package ai.ksense.jddl;

import ai.ksense.jddl.schema.Column;

public abstract class DDLStatement {
    protected final String tableName;

    protected DDLStatement(String tableName) {
        this.tableName = tableName;
    }

    public abstract String toSQLStatement(DDLSyntaxSettings syntaxSettings);

    @Override
    public String toString() {
        return toSQLStatement(new DDLSyntaxSettings());
    }

    public static class CreateTable extends DDLStatement {

        public CreateTable(String tableName) {
            super(tableName);
        }

        @Override
        public String toSQLStatement(DDLSyntaxSettings syntax) {
            return String.format("CREATE TABLE %s ()", syntax.id(tableName));
        }
    }

    public static class DeleteColumn extends DDLStatement {
        private final String columnName;

        protected DeleteColumn(String tableName, String columnName) {
            super(tableName);
            this.columnName = columnName;
        }

        @Override
        public String toSQLStatement(DDLSyntaxSettings syntax) {
            return String.format("ALTER TABLE %s DROP COLUMN %s;", syntax.id(tableName), syntax.id(columnName));
        }
    }

    public static class AddColumn extends DDLStatement {
        private final Column column;

        public AddColumn(String tableName, Column column) {
            super(tableName);
            this.column = column;
        }

        @Override
        public String toSQLStatement(DDLSyntaxSettings syntax) {
            StringBuilder b = new StringBuilder("ALTER TABLE ")
                    .append(syntax.id(tableName)).append(" ADD ").append(" ")
                    .append(syntax.id(column.getName())).append(" ")
                    .append(column.getType());
            if (column.getDefaultValue() != null) {
                b.append(" DEFAULT '").append(column.getDefaultValue()).append("'");
            }
            if (column.isNotNull()) {
                b.append(" NOT NULL");
            }
            if (column.getOptions() != null) {
                b.append(" ").append(column.getOptions());
            }
            return b.toString();
        }
    }
}
