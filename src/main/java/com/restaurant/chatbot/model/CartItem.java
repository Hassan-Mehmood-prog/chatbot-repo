package com.restaurant.chatbot.model;

import lombok.Data;

@Data
public class CartItem {
    private String itemName;
    private int quantity;
    private int price;
}
