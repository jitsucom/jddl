package ai.ksense.jddl.schema;

import java.util.List;

public class IndexBuilder {
    private List<String> columnsName;
    private String name;

    IndexBuilder(String name, List<String> columnsName) {
        this.columnsName = columnsName;
        this.name = name;
    }

    IndexBuilder(List<String> columnsName) {
        this.columnsName = columnsName;
        this.name = String.join("_", columnsName);
    }

    public Index build() {
        return new Index(name, columnsName);
    }
}
