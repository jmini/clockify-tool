package fr.jmini.clockify;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import picocli.CommandLine;

class AppTest {
    @Test
    void mainOutput() throws Exception {
        runTest("main-help-output.txt", "--help");
    }

    @Test
    void exportOutput() throws Exception {
        runTest("export-help-output.txt", "export", "--help");
    }

    @Test
    void exportProjectsOutput() throws Exception {
        runTest("export-projects-help-output.txt", "export-projects", "--help");
    }

    @Test
    void exportTagsOutput() throws Exception {
        runTest("export-tags-help-output.txt", "export-tags", "--help");
    }

    private void runTest(String name, String... args) throws IOException {
        StringWriter sw = new StringWriter();
        CommandLine cmd = new CommandLine(new App());
        cmd.setOut(new PrintWriter(sw));

        assertThat(cmd.execute(args))
                .as("exit code")
                .isZero();
        InputStream inputStream = AppTest.class.getResourceAsStream("/" + name);
        String expectedOutput;
        if (inputStream == null) {
            expectedOutput = "!! content missing, becasue file not found!";
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                expectedOutput = reader.lines()
                        .collect(Collectors.joining("\n")) + "\n";
            }
        }
        Path path = Paths.get("src/test/resources/" + name);
        Files.write(path, sw.toString()
                .getBytes(StandardCharsets.UTF_8));
        assertThat(sw.toString())
                .as("output")
                .isEqualTo(expectedOutput);
    }
}
