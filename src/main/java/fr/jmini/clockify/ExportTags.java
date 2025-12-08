package fr.jmini.clockify;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Function;

import fr.jmini.clockify.model.Tag;
import fr.jmini.clockify.model.User;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "export-tags", description = "Export tags data from the clockify backend", mixinStandardHelpOptions = true)
class ExportTags implements Runnable {

    private static final String TAGS_FILE = "tags.json";

    private static final Locale LOCALE = Locale.FRANCE;

    @Spec
    private CommandSpec spec;

    @Option(names = { "-w", "--working-folder" }, description = "Path to the working folder")
    private String workingFolder;

    @Option(names = { "--force" }, description = "Download the file, even if it already exist")
    private Boolean force;

    @Override
    public void run() {
        try {
            executeCommand();
        } catch (IOException e) {
            throw new RuntimeException("Error excuting the command", e);
        }
    }

    private void executeCommand() throws IOException {
        Function<String, RuntimeException> createException = CliUtil.createException(spec);
        Path root = CliUtil.root(createException, workingFolder);
        Path configFile = CliUtil.configFile(createException, root);
        Properties config = CliUtil.configProperties(configFile);
        if (!config.containsKey(CliUtil.CONFIG_API_KEY)) {
            throw createException.apply(String.format("Entry '%s' is missing in the '%s' file", CliUtil.CONFIG_API_KEY, configFile));
        }

        DateProvider dateProvider = LocalDate::now;
        String apiKey = config.getProperty(CliUtil.CONFIG_API_KEY);
        ClockifyClient client = new ClockifyHttpClient(apiKey);
        executeCommand(client, dateProvider, createException, root, force == Boolean.TRUE);
    }

    static void executeCommand(ClockifyClient client, DateProvider dateProvider, Function<String, RuntimeException> exceptionCreator, Path root, boolean replaceExisting) throws IOException {
        User user = client.getCurrentUser();
        exportTags(client, root, user.getActiveWorkspace(), user.getId(), replaceExisting);
    }

    static void exportTags(ClockifyClient client, Path root, String workspaceId, String userId, boolean replaceExisting) throws IOException {
        Path data = CliUtil.dataFolder(root);
        Files.createDirectories(data);
        Path file = data.resolve(TAGS_FILE);
        if (replaceExisting || !Files.exists(file)) {
            List<Tag> entries = client.getTags(workspaceId, userId);
            Files.writeString(file, JSON.toJson(entries));
        }
    }

    static List<Tag> readFromFile(Path root) throws IOException {
        Path data = CliUtil.dataFolder(root);
        Path file = data.resolve(TAGS_FILE);
        if (Files.exists(file)) {
            String content = Files.readString(file);
            return JSON.deserializeTags(content);
        }
        return List.of();
    }
}