package model;

import java.util.ArrayList;
import java.util.List;

public class History {
    private List<String> urls;
    
    public History() {
        urls = new ArrayList<>();
    }
    
    public void addEntry(String url) {
        urls.add(url);
    }
    
    public List<String> getHistory() {
        return new ArrayList<>(urls);
    }
    
    public void clearHistory() {
        urls.clear();
    }
}
