package com.progettoTAASS.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.catalog.model.User;
import com.progettoTAASS.catalog.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class CatalogReceiver {
    private final UserRepository userRepository;

    @Autowired
    public CatalogReceiver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void receivedMessage(@Payload String message) {
        ObjectMapper o = new ObjectMapper();
        System.out.println("\nmessaggio non convertito: " + message);
        String username = (message.split("me\":\"")[1]).split("\",\"e")[0];
        System.out.println("\n\nusername from split: " + username + "\n");
        User receivedUser = new User(username);
        System.out.println("\nutente inserito" + receivedUser);
        userRepository.save(receivedUser);
    }
}
