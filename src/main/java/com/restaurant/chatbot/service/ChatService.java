package com.restaurant.chatbot.service;

import com.restaurant.chatbot.model.Message;
import com.restaurant.chatbot.model.Menu;
import com.restaurant.chatbot.model.Order;
import com.restaurant.chatbot.repository.MessageRepository;
import com.restaurant.chatbot.repository.MenuRepository;
import com.restaurant.chatbot.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OrderRepository orderRepository;
    private final MessageRepository messageRepository;
    private final MenuRepository menuRepository; // 🔥 NEW

    public String getReply(String userMessage) {

        String lower = userMessage.toLowerCase();
        String reply = "";

        // 🔥 GET MENU FROM DB (OCR DATA)
        List<Menu> menus = menuRepository.findAll();

        Map<String, String> menuMap = new HashMap<>();

        for (Menu menu : menus) {

            // 🔥 STEP 7: PARSING OCR TEXT
            String[] lines = menu.getContent().split("\n");

            for (String line : lines) {
                if (line.contains("-")) {
                    String[] parts = line.split("-");
                    menuMap.put(parts[0].trim().toLowerCase(),
                            parts[1].trim());
                }
            }
        }

        // 🔥 STEP 6: CHAT LOGIC


        if (lower.contains("menu")) {
            reply = "📋 Menu:\n";
            for (String item : menuMap.keySet()) {
                reply += item + " - " + menuMap.get(item) + "\n";
            }

        } else {

            boolean found = false;

            for (String item : menuMap.keySet()) {

                if (lower.contains(item)) {

                    found = true;

                    // 🔥 ORDER SAVE (same as your old logic)
                    Order order = new Order();
                    order.setItems(item);
                    order.setStatus("PLACED");
                    orderRepository.save(order);

                    reply = "✅ " + item + " order placed! Price: " + menuMap.get(item);
                    break;
                }
            }

            if (!found) {
                reply = "❌ Item not found in menu";
            }
        }

        // 🔥 SAVE MESSAGE (same as before)
        Message msg = new Message();
        msg.setUserMessage(userMessage);
        msg.setBotReply(reply);
        messageRepository.save(msg);

        return reply;
    }
}