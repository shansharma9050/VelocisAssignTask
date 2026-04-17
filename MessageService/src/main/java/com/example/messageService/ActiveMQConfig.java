package com.example.messageService;

import java.util.Arrays;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMQConfig {

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory =
                new ActiveMQConnectionFactory("tcp://localhost:61616");

        factory.setTrustedPackages(Arrays.asList(
                "com.example.messageService.model"
        ));

        return factory;
    }
}
