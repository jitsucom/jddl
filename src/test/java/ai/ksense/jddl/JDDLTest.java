package ai.ksense.jddl;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class JDDLTest extends H2Test {

    @Test
    public void test() throws Exception {
        DatabaseSchema schema_v1 = getSchema("/jddl_v1.yml");
        DatabaseSchema schema_v2 = getSchema("/jddl_v2.yml");
        try (Connection connection = getConnection()) {
            List<String> patch = new JDDL(schema_v1, connection).generatePatch();
            patch.forEach(System.out::println);
            Assert.assertEquals(5, patch.size());
            new JDDL(schema_v1, connection).applyChanges();
            connection.commit();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("credit_score_default", "1");
        try (Connection connection = getConnection()) {
            List<String> patch = new JDDL(getReader("/jddl_v2.yml"), connection, map).generatePatch();
            patch.forEach(System.out::println);
            Assert.assertEquals(3, patch.size());
            new JDDL(getReader("/jddl_v2.yml"), connection).applyChanges();
        }

        try (Connection connection = getConnection()) {
            List<String> patch = new JDDL(schema_v2, connection).generatePatch();
            Assert.assertEquals(0, patch.size());
        }
    }
    public DatabaseSchema getSchema(String classpath) {
        try (InputStreamReader r = getReader(classpath)) {
            return new YAMLTablesFactory(r).getSchema();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private InputStreamReader getReader(String classpath) {
        return new InputStreamReader(getClass().getResourceAsStream(classpath));
    }

}