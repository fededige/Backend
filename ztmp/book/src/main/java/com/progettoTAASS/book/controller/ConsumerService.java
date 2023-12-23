package com.progettoTAASS.book.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.book.model.Book;
import com.progettoTAASS.book.repository.BookRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class ConsumerService {
    private final BookRepository bookRepository;

    @Autowired
    public ConsumerService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void receivedMessage(@Payload String message) {
        ObjectMapper o = new ObjectMapper();
        System.out.println("\nmessaggio non convertito: " + message);
        try {
            Book b = o.readValue(message, Book.class);
            System.out.println("\nbook['title']: " + b.getTitle());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
