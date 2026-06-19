package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class App {
    private static final String DEFAULT_DATABASE_URL = "jdbc:h2:mem:project";

    private static HikariDataSource createDataSource() {
        var config = new HikariConfig();
        var jdbcDatabaseUrl = System.getenv().getOrDefault("JDBC_DATABASE_URL", DEFAULT_DATABASE_URL);
        config.setJdbcUrl(jdbcDatabaseUrl);
        return new HikariDataSource(config);
    }

    private static void initializeDatabase(HikariDataSource dataSource) {
        var schema = App.class.getClassLoader().getResourceAsStream("schema.sql");
        if (schema == null) {
            throw new IllegalStateException("schema.sql not found");
        }

        try (schema;
             var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            var sql = new String(schema.readAllBytes(), StandardCharsets.UTF_8);
            statement.execute(sql);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    public static Javalin getApp() {
        var dataSource = createDataSource();
        initializeDatabase(dataSource);
        BaseRepository.setDataSource(dataSource);

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.events.serverStopped(dataSource::close);
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
            config.routes.get("/", ctx -> ctx.render("index.jte"));
        });
        return app;
    }

    public static void main(String[] args) {
        var app = getApp();
        var port = Integer.parseInt(System.getenv("PORT"));
        app.start(port);
    }
}
