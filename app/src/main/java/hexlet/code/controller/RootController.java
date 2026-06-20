package hexlet.code.controller;

import hexlet.code.dto.IndexPage;
import io.javalin.http.Context;

import java.util.Map;

public class RootController {
    public static void index(Context ctx) {
        var page = new IndexPage("");
        ctx.render("index.jte", Map.of("page", page));
    }
}
