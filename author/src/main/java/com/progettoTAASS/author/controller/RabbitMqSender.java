package com.progettoTAASS.author.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Queue messageQueue;

    public RabbitMqSender(RabbitTemplate rabbitTemplate, Queue messageQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageQueue = messageQueue;
    }

    public void send(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            String mess = objectMapper.writeValueAsString(message);

            try {
                this.rabbitTemplate.convertAndSend(this.messageQueue.getName(), mess);
                System.out.println("\n\n\n[S] Sent '" + mess + "'\n\n\n");
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
