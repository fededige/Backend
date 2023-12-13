package com.progettoTAASS.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.book.repository.BookRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class RabbitMqReceiver {

    ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "deliveredPreparations")
    public void markDeliveredPreparation(@Payload String message) {
        System.out.println("\n\n[R] Received <" + message + ">");

        try {
            String mess = objectMapper.readValue(message, String.class);
            System.out.println("\n\n\nMessaggi da Author:\n <" + mess + ">\n\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}