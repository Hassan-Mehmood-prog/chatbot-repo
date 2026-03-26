package com.restaurant.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.restaurant.chatbot.model.Message;
import com.restaurant.chatbot.model.Order;
import com.restaurant.chatbot.repository.MessageRepository;
import com.restaurant.chatbot.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final OrderRepository orderRepository;
    private final MessageRepository messageRepository;

    public String getReply(String userMessage) {
        String lower = userMessage.toLowerCase();
        String reply = "";

        // MENU
        if(lower.contains("menu")){
            reply = "🍔 Zinger Burger - 500\n🍕 Pizza - 800\n🥤 Drink - 150";
        }

        // ORDER
        else if(lower.contains("order")){
            Order order = new Order();
            order.setItems(userMessage);
            order.setStatus("PLACED");
            orderRepository.save(order);

            reply = "✅ Order placed!";
        }

        // AI / fallback
        else {
            reply = callAIWithFallback(userMessage);
        }

        // Save every message
        Message msg = new Message();
        msg.setUserMessage(userMessage);
        msg.setBotReply(reply);
        messageRepository.save(msg);

        return reply;
    }

    private String callAIWithFallback(String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "https://api.openai.com/v1/chat/completions";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String safeMessage = message.replace("\"", "\\\"");

            String body = """
        {
          "model": "gpt-3.5-turbo",
          "messages": [
            {"role": "system", "content": "You are a friendly restaurant assistant. Answer politely."},
            {"role": "user", "content": "%s"}
          ]
        }
        """.formatted(safeMessage);

            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // ✅ Parse JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String aiReply = root.path("choices").get(0).path("message").path("content").asText();

            return aiReply;

        } catch (Exception e) {
            e.printStackTrace();
            String[] fallbackReplies = {
                    "Hi! I'm here to help you. 🍔 Check our menu or place an order!",
                    "Sorry, my AI brain is taking a short break. 😅 But I can help with your order!",
                    "Hello! I can't reach AI right now, but I can suggest our delicious items!"
            };
            Random rand = new Random();
            return fallbackReplies[rand.nextInt(fallbackReplies.length)];
        }
    }
}