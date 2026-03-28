package com.restaurant.chatbot.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "menu")
public class Menu {
    @Id
    private String id;
    private String text; // OCR text

    public String getContent() {

        // Simple parsing logic to extract menu items from OCR text
        StringBuilder content = new StringBuilder();
        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) continue; // Skip empty lines
            content.append(line.trim()).append("\n");
        }
        return content.toString().trim();
    }
}