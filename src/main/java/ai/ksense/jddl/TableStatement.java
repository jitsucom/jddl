package ai.ksense.jddl;

public abstract class TableStatement {
    protected final String tableName;

    protected TableStatement(String tableName) {
        this.tableName = tableName;
    }

    public abstract String toSQLStatement();

    @Override
    public String toString() {
        return toSQLStatement();
    }

    static class CreateTable extends TableStatement {

        protected CreateTable(String tableName) {
            super(tableName);
        }

        @Override
        public String toSQLStatement() {
            return String.format("CREATE TABLE %s ()", tableName);
        }
    }

    static class DeleteColumn extends TableStatement {
        private final String columnName;

        protected DeleteColumn(String tableName, String columnName) {
            super(tableName);
            this.columnName = columnName;
        }

        @Override
        public String toSQLStatement() {
            return String.format("ALTER TABLE `%s` DROP COLUMN `%s`;", tableName, columnName);
        }
    }

    static class AddColumn extends TableStatement {
        private final Column column;

        protected AddColumn(String tableName, Column column) {
            super(tableName);
            this.column = column;
        }

        @Override
        public String toSQLStatement() {
            StringBuilder b = new StringBuilder("ALTER TABLE `")
                    .append(tableName).append("` ADD ").append(" `")
                    .append(column.getName()).append("` ")
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
