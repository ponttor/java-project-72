package hexlet.code;

import io.javalin.Javalin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class AppTest {
    @Test
    void getAppReturnsJavalinInstance() {
        var app = App.getApp();

        assertInstanceOf(Javalin.class, app);
    }
}
