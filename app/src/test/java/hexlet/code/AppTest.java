package hexlet.code;

import hexlet.code.repository.UrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    @BeforeEach
    void setUp() {
        System.setProperty("JDBC_DATABASE_URL", "jdbc:h2:mem:" + UUID.randomUUID());
    }

    @AfterEach
    void tearDown() {
        System.clearProperty("JDBC_DATABASE_URL");
    }

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

    @Test
    void storesNormalizedUrlAndRedirectsToUrlPage() throws Exception {
        var app = App.getApp();
        app.start(0);

        try {
            var client = newClient();
            var response = postUrl(client, app.port(), "https://some-domain.org:8080/example/path");

            assertEquals(302, response.statusCode());
            var url = UrlRepository.findByName("https://some-domain.org:8080").orElseThrow();

            var location = response.headers().firstValue("location").orElseThrow();
            assertEquals(NamedRoutes.urlPath(url.getId()), location);
            var page = get(client, app.port(), location);

            assertTrue(page.body().contains("Страница успешно добавлена"));
            assertTrue(page.body().contains("https://some-domain.org:8080"));
            assertTrue(page.body().contains("action=\"" + NamedRoutes.urlChecksPath(url.getId()) + "\""));
            assertTrue(page.body().contains("data-test=\"checks\""));
        } finally {
            app.stop();
        }
    }

    @Test
    void redirectsToExistingUrlPageWhenUrlAlreadyExists() throws Exception {
        var app = App.getApp();
        app.start(0);

        try {
            var client = newClient();
            postUrl(client, app.port(), "https://some-domain.org/example/path");
            var existingUrl = UrlRepository.findByName("https://some-domain.org").orElseThrow();
            var response = postUrl(client, app.port(), "https://some-domain.org/another/path");

            assertEquals(302, response.statusCode());

            var location = response.headers().firstValue("location").orElseThrow();
            assertEquals(NamedRoutes.urlPath(existingUrl.getId()), location);
            var page = get(client, app.port(), location);

            assertTrue(page.body().contains("Страница уже существует"));
            assertTrue(page.body().contains("https://some-domain.org"));
        } finally {
            app.stop();
        }
    }

    @Test
    void rendersUrlsListPage() throws Exception {
        var app = App.getApp();
        app.start(0);

        try {
            var client = newClient();
            postUrl(client, app.port(), "https://first-domain.org/path");
            postUrl(client, app.port(), "https://second-domain.org/path");

            var page = get(client, app.port(), "/urls");

            assertEquals(200, page.statusCode());
            assertTrue(page.body().contains("Сайты"));
            assertTrue(page.body().contains("https://first-domain.org"));
            assertTrue(page.body().contains("https://second-domain.org"));
            assertTrue(page.body().contains("href=\"/urls/"));
        } finally {
            app.stop();
        }
    }

    @Test
    void rendersFormWithErrorForInvalidUrl() throws Exception {
        var app = App.getApp();
        app.start(0);

        try {
            var client = newClient();
            var response = postUrl(client, app.port(), "invalid-url");

            assertEquals(422, response.statusCode());
            assertTrue(response.body().contains("Некорректный URL"));
            assertTrue(response.body().contains("value=\"invalid-url\""));
        } finally {
            app.stop();
        }
    }

    private static HttpClient newClient() {
        return HttpClient.newBuilder()
            .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
            .build();
    }

    private static HttpResponse<String> postUrl(HttpClient client, int port, String url) throws Exception {
        var body = "url=" + URLEncoder.encode(url, StandardCharsets.UTF_8);
        var request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + "/urls"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> get(HttpClient client, int port, String path) throws Exception {
        var request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + path))
            .GET()
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
