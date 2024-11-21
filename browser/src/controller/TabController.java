package controller;

import javafx.scene.control.Tab;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker.State;
import java.util.function.Consumer;

public class TabController {
	private Tab tab;                         // Quản lý tab hiện tại
	private WebView webView;                 // Component hiển thị web
	private WebEngine webEngine;             // Engine xử lý web
	private String currentTitle;             // Tiêu đề trang web hiện tại
	private String currentUrl;               // URL hiện tại
	private Consumer<String> urlChangeCallback;  // Callback khi URL thay đổi
	private Consumer<String> historyCallback;

    public TabController() {
        initializeTab();
        setupWebView();
        setupListeners();
    }
    
    // Khởi tạo tab cơ bản với tiêu đề mặc định
    private void initializeTab() {
        tab = new Tab("New Tab");
        tab.setClosable(true);
    }

    private void setupWebView() {
        webView = new WebView();
        webEngine = webView.getEngine();
        tab.setContent(webView);
        
        
    }

    private void setupListeners() {
    	// Listener theo dõi thay đổi tiêu đề
        webEngine.titleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
            	//Khi tiêu đề thay đổi, cập nhật biến currentTitle
                currentTitle = newValue;
                //Đồng thời cập nhật tên tab hiển thị tiêu đề mới
                tab.setText(newValue);
            }
        });

        // Listener theo dõi thay đổi URL
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
        	//Theo dõi sự thay đổi của URL trang web
            if (newValue != null) {
            	//Khi URL thay đổi, cập nhật biến currentUrl
                currentUrl = newValue;
                //Gọi callback để thông báo URL mới cho BrowserController cập nhật thanh địa chỉ
                if (urlChangeCallback != null) {
                    urlChangeCallback.accept(newValue);
                }
            }
        });

        // Listener theo dõi trạng thái tải trang
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == State.SUCCEEDED) {
            	//Lấy tiêu đề trang
                String title = webEngine.getTitle();
                if (title != null && !title.isEmpty()) {
                	//Nếu có tiêu đề thì hiển thị
                    tab.setText(title);
                } else {
                	//Nếu không có thì hiển thị "New Tab"
                    tab.setText("New Tab");
                }
            } else if (newValue == State.FAILED) {
                tab.setText("Error");
            }
        });
        
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentUrl = newValue;
                if (urlChangeCallback != null) {
                    urlChangeCallback.accept(newValue);
                }
                // Add to history whenever URL changes
                if (historyCallback != null) {
                    historyCallback.accept(newValue);
                }
            }
        });
    }
    
    // Thiết lập callback khi URL thay đổi
    public void setUrlChangeCallback(Consumer<String> callback) {
        this.urlChangeCallback = callback;
    }

    public Tab getTab() {
        return tab;
    }

    public WebView getWebView() {
        return webView;
    }

    public WebEngine getWebEngine() {
        return webEngine;
    }

    public String getCurrentTitle() {
        return currentTitle;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }
    
    // Tải URL mới
    public void loadUrl(String url) {
        webEngine.load(url);
    }
    
    // Quay lại trang trước
    public void goBack() {
        if (webEngine.getHistory().getCurrentIndex() > 0) {
            webEngine.getHistory().go(-1);
        }
    }
    
    // Tiến tới trang sau
    public void goForward() {
        if (webEngine.getHistory().getCurrentIndex() < webEngine.getHistory().getEntries().size() - 1) {
            webEngine.getHistory().go(1);
        }
    }
    
    // Tải lại trang hiện tại
    public void refresh() {
        webEngine.reload();
    }
    
    // Dừng tải trang
    public void stop() {
        webEngine.getLoadWorker().cancel();
    }
    
    // Kiểm tra có thể quay lại
    public boolean canGoBack() {
        return webEngine.getHistory().getCurrentIndex() > 0;
    }
    
    // Kiểm tra có thể tiến tới
    public boolean canGoForward() {
        return webEngine.getHistory().getCurrentIndex() < webEngine.getHistory().getEntries().size() - 1;
    }
    
    public void setHistoryCallback(Consumer<String> callback) {
        this.historyCallback = callback;
    }
}