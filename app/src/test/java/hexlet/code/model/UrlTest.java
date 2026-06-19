package hexlet.code.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UrlTest {
    @Test
    void createsUrlWithNameAndTimestamp() {
        var url = new Url("https://example.com");

        assertEquals("https://example.com", url.getName());
        assertNotNull(url.getCreatedAt());
    }
}
