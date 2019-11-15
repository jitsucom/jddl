package ai.ksense.jddl;

import ai.ksense.jddl.schema.DBSchema;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * Reads schema from YAML format. The format documentation: https://githib.com/ksense-co/jddl
 */
public class YAMLTablesFactory {
    private static ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());
    static {
        YAML_MAPPER.setVisibility(YAML_MAPPER.getDeserializationConfig().getDefaultVisibilityChecker()
                .withCreatorVisibility(JsonAutoDetect.Visibility.ANY)
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
    }

    private Reader reader;

    public YAMLTablesFactory(Reader reader) {
        this.reader = reader;
    }

    public DBSchema getSchema() {
        try {
            return new DBSchema(YAML_MAPPER.readValue(reader, DBSchema.class).getTables());
        } catch (IOException e) {
            throw new JDDLException("Can't read DDL: " + e.getMessage(), e);
        }
    }

}
