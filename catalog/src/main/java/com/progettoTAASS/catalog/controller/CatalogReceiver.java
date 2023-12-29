package com.progettoTAASS.catalog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

//    @RabbitListener(queues = "userQueue")
//    public void receivedMessage(@Payload String message) {
//        ObjectMapper o = new ObjectMapper();
//        User receivedUser = null;
//        try {
//            receivedUser = o.readValue(message, User.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("\nutente inserito" + receivedUser);
//
//        User checkExistingUser = userRepository.findUserByUsername(receivedUser.getUsername());
//        System.out.println("\ncheckExistingUser: " + checkExistingUser);
//        if (checkExistingUser != null){
//            userRepository.delete(checkExistingUser);
//        } else {
//            userRepository.save(receivedUser);
//        }
//    }

    @RabbitListener(queues = "${rabbitmq.queue.catalog.name}")
    public void receivedMessage(@Payload String message) {
        ObjectMapper o = new ObjectMapper();
        System.out.println("CatalogReceiver message: " + message);
        User userReceived;
        try {
            userReceived = o.readValue(message, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        User checkExistingUser = userRepository.findUserByUsername(userReceived.getUsername());
        System.out.println("\ncheckExistingUser: " + checkExistingUser);
        if (checkExistingUser != null){
            userRepository.delete(checkExistingUser);
        } else {
            userRepository.save(userReceived);
        }
    }
}
