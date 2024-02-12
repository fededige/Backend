package com.progettoTAASS.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.user.model.User;
import com.progettoTAASS.user.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class UserReceiver {
    private final UserRepository userRepository;

    @Autowired
    private UserSender userSender;

    @Autowired
    public UserReceiver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.reservationUser.name}")
    public void receiveUser(@Payload String message) {
        ObjectMapper o = new ObjectMapper();
        System.out.println("receiveUser message: " + message);
        User userReceived;
        try {
            userReceived = o.readValue(message, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("userReceived: " + userReceived);
        System.out.println("userReceived.getUsername()" + userReceived.getUsername());
        User checkExistingUser = userRepository.findUserByUsername(userReceived.getUsername());
        System.out.println("\ncheckExistingUser: " + checkExistingUser);
        if (checkExistingUser != null){
            checkExistingUser.setCoins(userReceived.getCoins());
            User savedUser = userRepository.save(checkExistingUser);
            userSender.sendNewUser(savedUser);
        } else {
            System.out.println("ERROR USER receiveUser");
        }
    }
}
