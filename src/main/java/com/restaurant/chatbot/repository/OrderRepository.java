package com.restaurant.chatbot.repository;

import com.restaurant.chatbot.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}