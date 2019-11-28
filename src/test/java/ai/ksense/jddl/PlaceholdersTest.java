package ai.ksense.jddl;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PlaceholdersTest {
    @Test
    public void testApply() {
        HashMap<String, String> variables = new HashMap<>();
        variables.put("var", "val1");
        variables.put("new_var", "val2");
        Placeholders placeholders = new Placeholders(variables);
        Assert.assertEquals(
                "1-val1-2-val2-3-default",
                placeholders.apply("1-${var}-2-${new_var}-3-${with_default:default}", false)
        );
    }
}