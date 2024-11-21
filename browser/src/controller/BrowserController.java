package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import Service.HistoryService;
import config.BrowserConfig;
import java.util.List;
import java.util.Map;

public class BrowserController {
    @FXML private TabPane tabPane;          
    @FXML private TextField addressBar;     
    @FXML private Button backButton;        
    @FXML private Button forwardButton;     
    @FXML private Button refreshButton;     
    @FXML private Button homeButton;        
    @FXML private Menu historyMenu;         // Thêm menu History
    
    private HistoryService historyService = new HistoryService();

    @FXML
    public void initialize() {
        setupEventHandlers();
        setupHistoryMenu();        // Thêm setup history menu
        createNewTab();
        updateNavigationButtons();
    }

    private void setupHistoryMenu() {
        historyMenu.setOnShowing(e -> updateHistoryItems());
    }

    private void updateHistoryItems() {
        historyMenu.getItems().removeIf(item -> 
            !(item instanceof SeparatorMenuItem) && 
            !item.getText().equals("Clear History"));
        
        Map<String, String> history = historyService.getHistoryWithTitles();
        for(Map.Entry<String, String> entry : history.entrySet()) {
            String url = entry.getKey();
            String title = entry.getValue();
            MenuItem item = new MenuItem(title);
            item.setOnAction(e -> {
                TabController tabController = new TabController();
                Tab tab = tabController.getTab();
                tab.setUserData(tabController);
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);
                tabController.loadUrl(url);
            });
            historyMenu.getItems().add(item);
        }
    }


    private void setupEventHandlers() {
        backButton.setOnAction(e -> {
            TabController currentTab = getCurrentTabController();
            if (currentTab != null) {
                currentTab.goBack();
                updateNavigationButtons();
            }
        });

        forwardButton.setOnAction(e -> {
            TabController currentTab = getCurrentTabController();
            if (currentTab != null) {
                currentTab.goForward();
                updateNavigationButtons();
            }
        });

        refreshButton.setOnAction(e -> {
            TabController currentTab = getCurrentTabController();
            if (currentTab != null) {
                currentTab.refresh();
            }
        });

        homeButton.setOnAction(e -> loadHomePage());

        addressBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loadUrl();
            }
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                TabController controller = (TabController) newTab.getUserData();
                if (controller != null && controller.getCurrentUrl() != null) {
                    addressBar.setText(controller.getCurrentUrl());
                }
                updateNavigationButtons();
            }
        });
    }

    @FXML
    public void createNewTab() {
        TabController tabController = new TabController();
        Tab tab = tabController.getTab();
        
        tabController.setNavigationCallback(() -> updateNavigationButtons());
        
        tabController.setUrlChangeCallback(url -> {
            if (tab.isSelected()) {
                addressBar.setText(url);
            }
        });
        
        // Add history callback
        tabController.setHistoryCallback(url -> {
            historyService.addToHistory(url);
            updateHistoryItems();
        });

        tab.setUserData(tabController);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

        tab.setOnCloseRequest(e -> {
            if (tabPane.getTabs().size() == 1) {
                createNewTab();
            }
        });

        loadHomePage();
    }

    @FXML
    public void clearHistory() {
        historyService.clearHistory();
        updateHistoryItems();
    }

    private void loadUrl() {
        String url = addressBar.getText().trim();
        if (!url.isEmpty()) {
            TabController currentTab = getCurrentTabController();
            if (currentTab != null) {
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }
                currentTab.loadUrl(url);
                historyService.addToHistory(url);
                updateHistoryItems();  // Cập nhật menu history
            }
        }
    }

    private void loadHomePage() {
        addressBar.setText(BrowserConfig.HOME_PAGE);
        loadUrl();
    }

    private TabController getCurrentTabController() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab != null) {
            return (TabController) currentTab.getUserData();
        }
        return null;
    }

    private void updateNavigationButtons() {
        TabController currentTab = getCurrentTabController();
        if (currentTab != null) {
            backButton.setDisable(!currentTab.canGoBack());
            forwardButton.setDisable(!currentTab.canGoForward());
        } else {
            backButton.setDisable(true);
            forwardButton.setDisable(true);
        }
    }

    @FXML
    public void closeCurrentTab() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab != null) {
            tabPane.getTabs().remove(currentTab);
            if (tabPane.getTabs().isEmpty()) {
                createNewTab();
            }
        }
    }

    @FXML
    public void exit() {
        System.exit(0);
    }
}
