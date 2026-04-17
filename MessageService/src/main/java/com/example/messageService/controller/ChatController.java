package com.example.messageService.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.messageService.model.ChatMessage;
import com.example.messageService.service.ChatProducer;

@Controller
@RequestMapping("/message")
public class ChatController {

    @Autowired
    private ChatProducer producer;

    @GetMapping("/chatting")
    public String chatPage() {
        return "chat";
    }

    @MessageMapping("/send")
    public void sendMessage(ChatMessage message, Principal principal) {

        if (principal == null) {
            System.out.println("❌ Principal is NULL");
            return;
        }

        String senderEmail = principal.getName();   

        System.out.println("Sender Email ==============>: " + senderEmail);

        message.setSenderEmail(senderEmail);

        producer.send(message);
    }

    @PostMapping("/send")
    @ResponseBody
    public void send(@RequestBody ChatMessage message) {
        producer.send(message);
    }
}