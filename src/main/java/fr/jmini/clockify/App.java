package fr.jmini.clockify;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = App.APPLICATION_NAME, mixinStandardHelpOptions = true, versionProvider = fr.jmini.clockify.PropertiesVersionProvider.class, subcommands = {
        Export.class,
})
public class App {

    public static final String APPLICATION_NAME = "clockify-tool";

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}