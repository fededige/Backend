package com.progettoTAASS.author.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.progettoTAASS.author.model.Book;

@Component
public class ProducerService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routingkey}")
    private String routingkey;

    public void sendMessage(Book book) {
        ObjectMapper o = new ObjectMapper();
        String bookJson = "";
        try {
            bookJson = o.writeValueAsString(book);
            System.out.println(bookJson);
            rabbitTemplate.convertAndSend(exchange,routingkey, bookJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
