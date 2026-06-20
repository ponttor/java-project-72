package hexlet.code.controller;

import hexlet.code.NamedRoutes;
import hexlet.code.dto.IndexPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.UrlUtils;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.net.MalformedURLException;
import java.util.Map;

public class UrlsController {
    public static void index(Context ctx) {
        var page = new UrlsPage(UrlRepository.findAll());
        ctx.render("urls/index.jte", Map.of("page", page));
    }

    public static void create(Context ctx) {
        var rawUrl = ctx.formParam("url");
        String normalizedUrl;

        try {
            normalizedUrl = UrlUtils.normalize(rawUrl == null ? "" : rawUrl);
        } catch (IllegalArgumentException | MalformedURLException e) {
            ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
            var page = new IndexPage(rawUrl == null ? "" : rawUrl);
            page.setFlash("Некорректный URL");
            page.setFlashType("danger");
            ctx.render("index.jte", Map.of("page", page));
            return;
        }

        var existingUrl = UrlRepository.findByName(normalizedUrl);
        var url = existingUrl.orElseGet(() -> UrlRepository.save(new Url(normalizedUrl)));
        var flash = existingUrl.isPresent()
            ? "Страница уже существует"
            : "Страница успешно добавлена";

        ctx.sessionAttribute("flash", flash);
        ctx.sessionAttribute("flashType", flash.equals("Страница успешно добавлена") ? "success" : "warning");
        ctx.redirect(NamedRoutes.urlPath(url.getId()));
    }

    public static void show(Context ctx) {
        var id = Long.valueOf(ctx.pathParam("id"));
        var url = UrlRepository.find(id).orElseThrow(() -> new RuntimeException("URL not found"));
        var page = new UrlPage(url);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/show.jte", Map.of("page", page));
    }
}
