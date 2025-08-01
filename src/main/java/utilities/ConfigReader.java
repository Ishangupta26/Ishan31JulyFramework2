package utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;

public class ConfigReader {

    private static config.ExecutionConfig executionConfig;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("config.yml")) {
            if (inputStream == null) {
                throw new RuntimeException("config.yml file not found in resources.");
            }

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            executionConfig = mapper.readValue(inputStream, config.ExecutionConfig.class);
            System.out.println("ConfigReader.executionConfig:\n" +
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(executionConfig));


        } catch (Exception e) {
            throw new RuntimeException("Failed to read config.yml: " + e.getMessage(), e);
        }
    }

    public static config.ExecutionConfig getExecutionConfig() {
        return executionConfig;
    }
}
