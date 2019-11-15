package ai.ksense.jddl.schema;

import java.util.List;

public class Index {
    private final String name;
    private final List<String> columnsName;

    public Index(String name, List<String> columnsName) {
        this.name = name;
        this.columnsName = columnsName;
    }

    public String getName() {
        return name;
    }

    public List<String> getColumnsName() {
        return columnsName;
    }
}
