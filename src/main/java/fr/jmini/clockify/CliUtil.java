package fr.jmini.clockify;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Function;

import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class CliUtil {

    static final String CONFIG_API_KEY = "apiKey";

    public static Function<String, RuntimeException> createException(CommandSpec spec) {
        return (String msg) -> new ParameterException(spec.commandLine(), msg);
    }

    public static Path root(Function<String, RuntimeException> exceptionCreator, String workingFolder) {
        Path root;
        if (workingFolder == null) {
            root = Paths.get("");
        } else {
            root = Paths.get(workingFolder);
            if (!Files.isDirectory(root)) {
                throw exceptionCreator.apply(String.format("Invalid value '%s' for option '--working-folder': directory does not exist", workingFolder));
            }
        }
        return root;
    }

    public static Properties configProperties(Path configFile) throws IOException {
        try (InputStream is = new FileInputStream(configFile.toFile())) {
            Properties properties = new Properties();
            properties.load(is);
            return properties;
        }
    }

    public static Path configFile(Function<String, RuntimeException> exceptionCreator, Path root) {
        Path configFile = root.resolve("config.properties");
        if (!Files.isRegularFile(configFile)) {
            throw exceptionCreator.apply(String.format("Configuration file '%s' does not exist", configFile.toAbsolutePath()));
        }
        return configFile;
    }

    public static Path dataFolder(Path root) {
        return root.resolve("data");
    }

    private CliUtil() {
    }
}
