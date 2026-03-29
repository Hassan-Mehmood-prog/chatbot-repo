// Updated ChatService.java

import java.util.HashMap;
import java.util.Map;

public class ChatService {
    private static final Map<String, String> menuData = new HashMap<>();

    static {
        // Hardcoded menu data
        menuData.put("1", "Pizza - $10");
        menuData.put("2", "Burger - $5");
        menuData.put("3", "Pasta - $7");
        // Add more items as needed
    }

    public String getResponse(String input) {
        // Simple rule-based response
        if (menuData.containsKey(input)) {
            return "You selected: " + menuData.get(input);
        } else {
            return "Please select a valid menu option.";
        }
    }
}