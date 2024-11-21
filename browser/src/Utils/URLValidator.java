package Utils;

import java.net.URL;

public class URLValidator {
    public static String validateUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        try {
            new URL(url);
            return url;
        } catch (Exception e) {
            return "https://www.google.com/search?q=" + url;
        }
    }
}
