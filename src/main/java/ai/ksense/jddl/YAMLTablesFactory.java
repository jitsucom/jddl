package ai.ksense.jddl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class YAMLTablesFactory implements SchemaReader {
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

    @Override
    public DatabaseSchema getSchema() {
        try {
            return new DatabaseSchema(YAML_MAPPER.readValue(reader, DatabaseSchema.class).getTables());
        } catch (IOException e) {
            throw new RuntimeException("Can't read DDL: " + e.getMessage(), e);
        }
    }

}
