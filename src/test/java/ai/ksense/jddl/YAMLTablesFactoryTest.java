package ai.ksense.jddl;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class YAMLTablesFactoryTest {

    @Test
    public void testYML() throws IOException {
        try (InputStreamReader r = new InputStreamReader(getClass().getResourceAsStream("/jddl_v1.yml"))) {
            List<Table> tables = new YAMLTablesFactory(r).getSchema().getTables();
            Assert.assertEquals(1, tables.size());
            Table table = tables.iterator().next();
            Assert.assertEquals(4, table.getColumns().size());
        }

        try (InputStreamReader r = new InputStreamReader(getClass().getResourceAsStream("/jddl_v2.yml"))) {
            List<Table> tables = new YAMLTablesFactory(r).getSchema().getTables();
            Assert.assertEquals(1, tables.size());
            Table table = tables.iterator().next();
            Assert.assertEquals(5, table.getColumns().size());
        }
    }

}