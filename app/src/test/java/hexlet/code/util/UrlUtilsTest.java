package hexlet.code.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlUtilsTest {
    @Test
    void normalizesUrlWithoutPort() throws Exception {
        var url = UrlUtils.normalize("https://some-domain.org/example/path");

        assertEquals("https://some-domain.org", url);
    }

    @Test
    void normalizesUrlWithPort() throws Exception {
        var url = UrlUtils.normalize("https://some-domain.org:8080/example/path");

        assertEquals("https://some-domain.org:8080", url);
    }

    @Test
    void throwsForInvalidUrl() {
        assertThrows(IllegalArgumentException.class, () -> UrlUtils.normalize("invalid-url"));
    }
}
