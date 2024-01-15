package com.progettoTAASS.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.review.model.User;
import com.progettoTAASS.review.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableRabbit
public class ReviewReceiver {
    private final UserRepository userRepository;


    @Autowired
    public ReviewReceiver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.reviewUser.name}")
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
        User checkExistingUser = userRepository.findByUsername(userReceived.getUsername());
        System.out.println("\ncheckExistingUser: " + checkExistingUser);
        if (checkExistingUser != null){
            userRepository.delete(checkExistingUser);
        } else {
            userRepository.save(userReceived);
        }
    }
}
