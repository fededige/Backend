package com.progettoTAASS.Reservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        if(checkExistingUser != null){
            userRepository.save(checkExistingUser);
        }
        else {
            userRepository.save(userReceived);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.catalogReservation.name}")
    public void receiveBook(@Payload String message) {
        ObjectMapper o = new ObjectMapper();
        // convert message received to jsonNode
        JsonNode messageObj;
        try {
             messageObj = o.readTree(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String title = messageObj.get("title").textValue();
        String author = messageObj.get("author").textValue();
        boolean av = messageObj.get("available").asBoolean();

        User owner;
        Date date;
        String dDate = messageObj.get("publishingDate").textValue();
        DateFormat df;
        if (Character.isDigit(dDate.charAt(0))){ // check date input format (if it starts with a number then if in yyyy-MM-dd format
             df = new SimpleDateFormat("yyyy-MM-dd");
        }
        else {
            df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        }

        try {
            owner = o.readValue(messageObj.get("owner").toString(), User.class);
            date = df.parse(dDate);
        } catch (JsonProcessingException | ParseException e) {
            throw new RuntimeException(e);
        }

        Book bookReceived = new Book();
        bookReceived.setTitle(title);
        bookReceived.setAuthor(author);
        bookReceived.setAvailable(av);
        bookReceived.setOwner(userRepository.findUserByUsername(owner.getUsername()));
        bookReceived.setPublishingDate(date);

        Book checkExistingBook = bookRepository.findAllByAuthorAndPublishingDateAndTitleAndOwner(bookReceived.getAuthor(), bookReceived.getPublishingDate(), bookReceived.getTitle(), bookReceived.getOwner());
        System.out.println("checkExistingBook" + checkExistingBook);
        if (checkExistingBook != null){
            bookRepository.delete(checkExistingBook);
        } else {
            bookRepository.save(bookReceived);
        }
    }
}
