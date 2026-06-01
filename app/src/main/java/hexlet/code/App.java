package hexlet.code;

import io.javalin.Javalin;

public class App {
    public static Javalin getApp() {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.routes.get("/", ctx -> ctx.result("Hello World"));
        });
        return app;
    }

    public static void main(String[] args) {
        var app = getApp();
        var port = Integer.parseInt(System.getenv("PORT"));
        app.start(port);
    }
}
