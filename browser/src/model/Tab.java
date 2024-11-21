package model;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Tab {
    private String title;
    private String url;
    private WebView webView;
    private WebEngine webEngine;
    
    public Tab() {
        webView = new WebView();
        webEngine = webView.getEngine();
        title = "New Tab";
    }
    
    public WebView getWebView() {
        return webView;
    }
    
    public WebEngine getWebEngine() {
        return webEngine;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}
