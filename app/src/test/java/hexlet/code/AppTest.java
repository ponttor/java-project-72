package hexlet.code;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {
    @Test
    void mainPrintsGreeting() {
        var output = new ByteArrayOutputStream();
        var originalOut = System.out;

        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        try {
            App.main(new String[]{});
        } finally {
            System.setOut(originalOut);
        }

        assertEquals("Hello, World!" + System.lineSeparator(), output.toString(StandardCharsets.UTF_8));
    }
}
