package Utils;

import javafx.scene.web.WebEngine;

public class WebUtils {
    public static void configureWebEngine(WebEngine engine) {
        engine.setUserAgent(config.BrowserConfig.USER_AGENT);
        engine.setJavaScriptEnabled(true);
    }
}
