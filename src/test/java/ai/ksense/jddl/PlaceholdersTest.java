package ai.ksense.jddl;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class PlaceholdersTest {
    @Test
    public void testApply() {
        Placeholders placeholders = new Placeholders(Map.of("var", "val1", "new_var", "val2"));
        Assert.assertEquals(
                "1-val1-2-val2-3-default",
                placeholders.apply("1-${var}-2-${new_var}-3-${with_default:default}", false)
        );
    }
}