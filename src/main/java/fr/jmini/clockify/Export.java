package fr.jmini.clockify;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.jmini.clockify.model.TimeEntry;
import fr.jmini.clockify.model.User;
import fr.jmini.clockify.model.WeekHolder;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "export", description = "Export data from the clockify backend", mixinStandardHelpOptions = true)
class Export implements Runnable {

    private static final Locale LOCALE = Locale.FRANCE;

    private static final ObjectMapper MAPPER = createMapper();

    @Spec
    private CommandSpec spec;

    @Option(names = { "-f", "--from" }, required = true, description = "From week (Example value: '2022-23')")
    private String from;

    @Option(names = { "-t", "--to" }, description = "To week (Example value: '2022-24')")
    private String to;

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
        executeCommand(client, dateProvider, createException, root, force == Boolean.TRUE, from, to);
    }

    static void executeCommand(ClockifyClient client, DateProvider dateProvider, Function<String, RuntimeException> exceptionCreator, Path root, boolean replaceExisting, String fromRaw, String toRaw) throws IOException {
        WeekHolder fromWeek = parse(exceptionCreator, "--from", fromRaw);
        WeekHolder toWeek;
        if (toRaw != null) {
            toWeek = parse(exceptionCreator, "--to", toRaw);
        } else {
            toWeek = defaultToWeek(dateProvider);
        }
        List<WeekHolder> list = calculateFromTo(exceptionCreator, fromWeek, toWeek);

        User user = client.getCurrentUser();
        for (WeekHolder h : list) {
            exportWeek(client, root, h, user.getActiveWorkspace(), user.getId(), replaceExisting);
        }
    }

    static WeekHolder parse(Function<String, RuntimeException> exceptionCreator, String optionName, String value) {
        if (!value.matches("[0-9]{4}-[0-9]{2}")) {
            throw exceptionCreator.apply(String.format("Invalid value '%s' for option '%s': partern should be YYYY-WW", value, optionName));
        }
        Integer year = Integer.valueOf(value.substring(0, 4));
        Integer weekNumber = Integer.valueOf(value.substring(5));
        return new WeekHolder(year, weekNumber);
    }

    static WeekHolder defaultToWeek(DateProvider dateProvider) {
        WeekHolder toWeek;
        LocalDate now = dateProvider.get();
        toWeek = new WeekHolder(now.getYear(), now.get(WeekFields.of(LOCALE)
                .weekOfWeekBasedYear()));
        return toWeek;
    }

    static List<WeekHolder> calculateFromTo(Function<String, RuntimeException> exceptionCreator, WeekHolder fromWeek, WeekHolder toWeek) {
        if (toWeek.isAfter(fromWeek)) {
            throw exceptionCreator.apply(String.format("To value '%s' must be after from '%s'", toWeek.getName(), fromWeek.getName()));
        }

        List<WeekHolder> result = new ArrayList<>();
        WeekHolder h = fromWeek;
        while (!toWeek.isAfter(h)) {
            result.add(h);
            h = h.next();
        }
        return result;
    }

    static void exportWeek(ClockifyClient client, Path root, WeekHolder h, String workspaceId, String userId, boolean replaceExisting) throws IOException {
        Path data = CliUtil.dataFolder(root);
        Files.createDirectories(data);
        Path file = data.resolve(h.getName() + ".json");
        if (replaceExisting || !Files.exists(file)) {
            String fromValue = convertDatetime(h);
            String toValue = convertDatetime(h.next());
            List<TimeEntry> entries = client.getTimeEntries(workspaceId, userId, fromValue, toValue);
            Files.writeString(file, MAPPER.writeValueAsString(entries));
        }
    }

    static String convertDatetime(WeekHolder h) {
        ZonedDateTime ldt = ZonedDateTime.now(ZoneOffset.UTC)
                .withYear(h.getYear())
                .with(WeekFields.of(LOCALE)
                        .getFirstDayOfWeek())
                .with(WeekFields.of(LOCALE)
                        .weekOfWeekBasedYear(), h.getWeekNumber())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .truncatedTo(ChronoUnit.MILLIS);
        return DateTimeFormatter.ISO_INSTANT.format(ldt);
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        return mapper;
    }

}