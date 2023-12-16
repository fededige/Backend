package com.progettoTAASS.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqReceiver {

    ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "testQueue")
    public void testMessage(@Payload String message) {
        System.out.println("\n\n[R] Received <" + message + ">");

        try {
            String mess = objectMapper.readValue(message, String.class);
            System.out.println("\n\n\nMessaggi da Author:\n <" + mess + ">\n\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}