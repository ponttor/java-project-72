package hexlet.code.util;

import java.net.MalformedURLException;
import java.net.URI;

public class UrlUtils {
    public static String normalize(String rawUrl) throws MalformedURLException {
        var url = URI.create(rawUrl.trim()).toURL();
        if (url.getHost() == null || url.getHost().isBlank()) {
            throw new MalformedURLException("Host is missing");
        }

        var port = url.getPort();
        var normalizedUrl = url.getProtocol() + "://" + url.getHost();
        return port == -1 ? normalizedUrl : normalizedUrl + ":" + port;
    }
}
