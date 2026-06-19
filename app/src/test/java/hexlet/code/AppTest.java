package hexlet.code;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    @Test
    void rootRouteRendersHomePage() throws Exception {
        var app = App.getApp();
        app.start(0);

        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + app.port() + "/"))
                .GET()
                .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertTrue(response.body().contains("Анализатор страниц"));
            assertTrue(response.body().contains("Бесплатно проверяйте сайты на SEO пригодность"));
            assertTrue(response.body().contains("https://cdn.jsdelivr.net/npm/bootstrap"));
            assertTrue(response.body().contains("action=\"/urls\""));
        } finally {
            app.stop();
        }
    }
}
