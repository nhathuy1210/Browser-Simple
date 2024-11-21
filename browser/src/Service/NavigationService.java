package Service;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import Utils.URLValidator;

public class NavigationService {
    private WebEngine webEngine;

    public NavigationService(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public void loadUrl(String url) {
        String validUrl = URLValidator.validateUrl(url);
        webEngine.load(validUrl);
    }

    public void goBack() {
        WebHistory history = webEngine.getHistory();
        if (history.getCurrentIndex() > 0) {
            history.go(-1);
        }
    }

    public void goForward() {
        WebHistory history = webEngine.getHistory();
        if (history.getCurrentIndex() < history.getEntries().size() - 1) {
            history.go(1);
        }
    }

    public void refresh() {
        webEngine.reload();
    }
}
