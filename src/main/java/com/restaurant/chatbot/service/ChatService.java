package com.restaurant.chatbot.service;

import com.restaurant.chatbot.model.Message;
import com.restaurant.chatbot.model.Order;
import com.restaurant.chatbot.repository.MessageRepository;
import com.restaurant.chatbot.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Integer.parseInt;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OrderRepository orderRepository;
    private final MessageRepository messageRepository;
    private final CartService cartService;

    // 🔥 MENU
    private static final Map<String, Integer> menuMap = new HashMap<>();
    // 🔥 TEMP CART (memory)
            private final Map<String, Integer> cart = new HashMap<>();

    static {
        menuMap.put("zinger burger", 300);
        menuMap.put("monster burger", 500);
        menuMap.put("chicken patty burger", 280);
        menuMap.put("grilled patty burger", 400);

        menuMap.put("chicken tikka pizza", 500);
        menuMap.put("special creamy pizza", 550);

        menuMap.put("french fries", 150);
        menuMap.put("masala fries", 150);

        menuMap.put("drink", 150);
        menuMap.put("zinger shawarma", 300);
    }


    public String getReply(String userMessage) {

        String lower = userMessage.toLowerCase();
        String reply = "";

        // ✅ GREETING
        if (lower.contains("hi") || lower.contains("hello") || lower.contains("assalam")) {
            reply = "👋 Welcome!\nType 'menu' to see items";
        }

        // ✅ CATEGORY (BURGER / PIZZA)
        else if (lower.contains("burger")) {

            reply = "🍔 Available Burgers:\n";

            for (String item : menuMap.keySet()) {
                if (item.contains("burger")) {
                    reply += "👉 " + item + " - Rs " + menuMap.get(item) + "\n";
                }
            }
        }

        else if (lower.contains("pizza")) {

            reply = "🍕 Available Pizzas:\n";

            for (String item : menuMap.keySet()) {
                if (item.contains("pizza")) {
                    reply += "👉 " + item + " - Rs " + menuMap.get(item) + "\n";
                }
            }
        }

        // ✅ ADD TO CART (MULTIPLE ITEMS)
        else if (lower.contains("order") || lower.contains("add")) {

            boolean found = false;

            for (String item : menuMap.keySet()) {

                if (lower.contains(item)) {

                    cart.put(item, cart.getOrDefault(item, 0) + 1);
                    found = true;
                }
            }

            if (found) {

                reply = "🛒 Items added to cart:\n";

                int total = 0;

                for (String item : cart.keySet()) {
                    int qty = cart.get(item);
                    int price = menuMap.get(item) * qty;
                    total += price;

                    reply += "👉 " + item + " x" + qty + " = Rs " + price + "\n";
                }

                reply += "💰 Current Total: Rs " + total +
                        "\nType 'confirm' to place order";
            } else {
                reply = "❌ Item not found";
            }
        }

        // ✅ CONFIRM ORDER
        else if (lower.contains("confirm")) {

            if (cart.isEmpty()) {
                reply = "❌ Cart is empty";
            } else {

                int total = 0;

                for (String item : cart.keySet()) {

                    Order order = new Order();
                    order.setItems(item + " x" + cart.get(item));
                    order.setStatus("PLACED");
                    cartService.addToCart(item, Integer.parseInt(String.valueOf(menuMap.get(item))));                    total += menuMap.get(item) * cart.get(item);
                }

                cart.clear();

                reply = "✅ Order placed successfully!\n💰 Total Bill: Rs " + total;
            }
        }

        // ✅ BILL CHECK
        else if (lower.contains("bill")) {

            if (cart.isEmpty()) {
                reply = "🧾 Cart is empty";
            } else {

                int total = 0;
                reply = "🧾 Your Bill:\n";

                for (String item : cart.keySet()) {
                    int qty = cart.get(item);
                    int price = menuMap.get(item) * qty;
                    total += price;

                    reply += "👉 " + item + " x" + qty + " = Rs " + price + "\n";
                }

                reply += "💰 Total: Rs " + total;
            }
        }

        // ✅ MENU
        else if (lower.contains("menu")) {

            reply = "📋 Full Menu:\n";

            for (String item : menuMap.keySet()) {
                reply += "👉 " + item + " - Rs " + menuMap.get(item) + "\n";
            }
        }

        else {
            reply = "🤖 Type 'menu' or 'burger' or 'pizza'";
        }

        // SAVE MESSAGE
        Message msg = new Message();
        msg.setUserMessage(userMessage);
        msg.setBotReply(reply);
        messageRepository.save(msg);

        return reply;
    }

    public Map<String, Integer> getCart() {
        return cart;
    }
}