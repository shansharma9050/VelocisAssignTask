package com.example.messageService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.example.messageService.model.ChatMessage;

@Service
public class ChatProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(ChatMessage message) {

        System.out.println("Sending to MQ: " + message.getMessage());

        jmsTemplate.convertAndSend("chat.topic", message);
    }

}