package com.example.messageService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.messageService.model.ChatMessage;

@Component
public class ChatConsumer {

    @Autowired
    private SimpMessagingTemplate template;

    @JmsListener(destination = "chat.topic")
    public void receive(ChatMessage message) {

        System.out.println("Received from MQ: " + message.getMessage()+"============>"+message.getReceiverEmail());

        template.convertAndSendToUser(message.getReceiverEmail(),"/queue/messages",message);
    }
}