package com.progettoTAASS.Reservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.Reservation.model.Book;
import com.progettoTAASS.Reservation.model.User;
import com.progettoTAASS.Reservation.repository.BookRepository;
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

    private final BookRepository bookRepository;

    @Autowired
    public ReservationReceiver(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.reservation.name}")
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
            userRepository.delete(checkExistingUser);
        } else {
            userRepository.save(userReceived);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.catalogReservation.name}")
    public void receiveBook(@Payload String message) {
        ObjectMapper o = new ObjectMapper();
        System.out.println("receiveBook message: " + message);
        Book bookReceived;
        try {
            bookReceived = o.readValue(message, Book.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("bookReceived: " + bookReceived);
        System.out.println("bookReceived.getTitle()" + bookReceived.getTitle());
        System.out.println("bookReceived.getOwner()" + bookReceived.getOwner());
        Book checkExistingBook = bookRepository.findAllByAuthorAndPublishingDateAndTitle(bookReceived.getAuthor(), bookReceived.getPublishingDate(), bookReceived.getTitle());
        System.out.println("\ncheckExistingBook: " + checkExistingBook);
        if (checkExistingBook != null){
            bookRepository.delete(checkExistingBook);
        } else {
            bookRepository.save(bookReceived);
        }
    }
}
