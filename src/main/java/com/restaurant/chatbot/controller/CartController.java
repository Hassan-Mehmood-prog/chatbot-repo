package com.restaurant.chatbot.controller;

import com.restaurant.chatbot.model.CartItem;
import com.restaurant.chatbot.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ✅ ADD
    @PostMapping("/add")
    public String add(@RequestParam String item, @RequestParam int price) {
        return cartService.addToCart(item, price);
    }

    // ✅ VIEW CART
    @GetMapping
    public List<CartItem> getCart() {
        return cartService.getCart();
    }

    // ✅ DELETE
    @DeleteMapping("/remove")
    public String remove(@RequestParam String item) {
        return cartService.removeItem(item);
    }

    // ✅ BILL
    @GetMapping("/bill")
    public String bill() {
        return cartService.getBill();
    }

    // ✅ CONFIRM
    @PostMapping("/confirm")
    public String confirm() {
        List<CartItem> items = cartService.confirmOrder();
        return "✅ Order Confirmed: " + items.toString();
    }
}