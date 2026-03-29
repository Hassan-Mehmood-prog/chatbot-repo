package com.restaurant.chatbot.service;

import com.restaurant.chatbot.model.Message;
import com.restaurant.chatbot.model.Order;
import com.restaurant.chatbot.repository.MessageRepository;
import com.restaurant.chatbot.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OrderRepository orderRepository;
    private final MessageRepository messageRepository;

    // 🔥 STATIC MENU (RULE BASED)
    private static final Map<String, String> menuMap = new HashMap<>();

    static {
        menuMap.put("burger", "500");
        menuMap.put("pizza", "800");
        menuMap.put("fries", "300");
        menuMap.put("drink", "150");
    }

    public String getReply(String userMessage) {

        String lower = userMessage.toLowerCase();
        String reply = "";

        // 🔥 SHOW MENU
        if (lower.contains("menu")) {
            reply = "📋 Menu:\n";
            for (String item : menuMap.keySet()) {
                reply += "🍽 " + item + " - " + menuMap.get(item) + "\n";
            }
        }

        // 🔥 PRICE QUERY
        else if (lower.contains("price")) {

            boolean found = false;

            for (String item : menuMap.keySet()) {
                if (lower.contains(item)) {
                    reply = "💰 " + item + " price is: " + menuMap.get(item);
                    found = true;
                    break;
                }
            }

            if (!found) {
                reply = "❌ Item not found in menu";
            }
        }

        // 🔥 ORDER LOGIC
        else {

            boolean found = false;

            for (String item : menuMap.keySet()) {

                if (lower.contains(item)) {

                    found = true;

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

        // 🔥 SAVE MESSAGE
        Message msg = new Message();
        msg.setUserMessage(userMessage);
        msg.setBotReply(reply);
        messageRepository.save(msg);

        return reply;
    }
}