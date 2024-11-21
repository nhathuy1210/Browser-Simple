package Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class HistoryService {
    private Map<String, String> history;  // URL, Display Title

    public HistoryService() {
        history = new LinkedHashMap<>();  // Maintains insertion order
    }

    public void addToHistory(String url) {
        String displayTitle = formatHistoryTitle(url);
        history.put(url, displayTitle);
    }

    public Map<String, String> getHistoryWithTitles() {
        return new LinkedHashMap<>(history);
    }

    public void clearHistory() {
        history.clear();
    }

    private String formatHistoryTitle(String url) {
        if (url.contains("google.com/search")) {
            try {
                String query = extractSearchQuery(url);
                return query + " - Search";
            } catch (Exception e) {
                return url;
            }
        }
        return url;
    }

    private String extractSearchQuery(String url) {
        int startIndex = url.indexOf("q=") + 2;
        int endIndex = url.indexOf("&", startIndex);
        if (endIndex == -1) {
            endIndex = url.length();
        }
        String query = url.substring(startIndex, endIndex);
        return URLDecoder.decode(query, StandardCharsets.UTF_8);
    }
}
