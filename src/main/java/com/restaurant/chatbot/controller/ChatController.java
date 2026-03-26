package com.restaurant.chatbot.controller;

import com.restaurant.chatbot.dto.ChatRequest;
import com.restaurant.chatbot.dto.ChatResponse;
import com.restaurant.chatbot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public Map<String,String> chat(@RequestBody Map<String,String> request){
        String reply = chatService.getReply(request.get("message"));
        return Map.of("reply", reply);
    }
}