package com.restaurant.chatbot.repository;

import com.restaurant.chatbot.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}