package com.restaurant.chatbot.repository;

import com.restaurant.chatbot.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuRepository extends MongoRepository<Menu, String> {
}
