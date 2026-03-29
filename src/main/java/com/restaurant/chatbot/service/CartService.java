package com.restaurant.chatbot.service;

import com.restaurant.chatbot.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    private final Map<String, CartItem> cart = new HashMap<>();

    // ✅ ADD ITEM
    public String addToCart(String item, int price) {

        if (cart.containsKey(item)) {
            CartItem existing = cart.get(item);
            existing.setQuantity(existing.getQuantity() + 1);
        } else {
            CartItem newItem = new CartItem();
            newItem.setItemName(item);
            newItem.setQuantity(1);
            newItem.setPrice(price);
            cart.put(item, newItem);
        }

        return "🛒 Added to cart: " + item;
    }

    // ✅ GET CART
    public List<CartItem> getCart() {
        return new ArrayList<>(cart.values());
    }

    // ✅ DELETE ITEM
    public String removeItem(String item) {
        if (cart.containsKey(item)) {
            cart.remove(item);
            return "❌ Removed: " + item;
        }
        return "Item not in cart";
    }

    // ✅ BILL
    public String getBill() {
        if (cart.isEmpty()) return "Cart is empty";

        int total = 0;
        StringBuilder bill = new StringBuilder("🧾 Bill:\n");

        for (CartItem item : cart.values()) {
            int itemTotal = item.getPrice() * item.getQuantity();
            total += itemTotal;

            bill.append(item.getItemName())
                    .append(" x").append(item.getQuantity())
                    .append(" = Rs ").append(itemTotal)
                    .append("\n");
        }

        bill.append("\n💰 Total: Rs ").append(total);

        return bill.toString();
    }

    // ✅ CONFIRM ORDER
    public List<CartItem> confirmOrder() {
        List<CartItem> items = new ArrayList<>(cart.values());
        cart.clear();
        return items;
    }
}