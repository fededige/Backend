package com.progettoTAASS.Reservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.Reservation.model.User;
import com.progettoTAASS.Reservation.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class ReservationReceiver {
    private final UserRepository userRepository;

    @Autowired
    public ReservationReceiver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.reservation.name}")
    public void receivedMessage(@Payload String message) {
        ObjectMapper o = new ObjectMapper();
        System.out.println("CatalogReceiver message: " + message);
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
            userRepository.delete(checkExistingUser);
        } else {
            userRepository.save(userReceived);
        }
    }
}
