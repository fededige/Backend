package com.progettoTAASS.catalog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.catalog.model.Book;
import com.progettoTAASS.catalog.model.User;
import com.progettoTAASS.catalog.repository.BookRepository;
import com.progettoTAASS.catalog.repository.UserRepository;
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
public class CatalogReceiver {
    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    @Autowired
    public CatalogReceiver(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.catalog.name}")
    public void receiveUser(@Payload String message) {
        ObjectMapper o = new ObjectMapper();
        System.out.println("CatalogReceiver message: " + message);
        User userReceived;
        try {
            userReceived = o.readValue(message, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        User checkExistingUser = userRepository.findUserByUsername(userReceived.getUsername());
        System.out.println("\ncheckExistingUser: " + checkExistingUser);
        if(checkExistingUser != null){
            checkExistingUser.setCoins(userReceived.getCoins());
            userRepository.save(checkExistingUser);
        }
        else {
            userRepository.save(userReceived);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.reservationCatalog.name}")
    public void updateBook(@Payload String message) {
        ObjectMapper o = new ObjectMapper();
        System.out.println("CatalogReceiver message: " + message);

        JsonNode messageObj;
        try {
            messageObj = o.readTree(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String title = messageObj.get("title").textValue();
        String author = messageObj.get("author").textValue();
        Date date;
        String dDate = messageObj.get("publishingDate").textValue();
        DateFormat df;
        if (Character.isDigit(dDate.charAt(0))){ // check date input format (if it starts with a number then if in yyyy-MM-dd format
            df = new SimpleDateFormat("yyyy-MM-dd");
        }
        else {
            df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        }
        User owner;
        try {
            owner = o.readValue(messageObj.get("owner").toString(), User.class);
            date = df.parse(dDate);
        } catch (JsonProcessingException | ParseException e) {
            throw new RuntimeException(e);
        }


        Book bookToUpdate = bookRepository.findAllByAuthorAndPublishingDateAndTitleAndOwner(author, date, title, userRepository.findUserByUsername(owner.getUsername()));

        if (bookToUpdate != null){
            boolean isAvailable = messageObj.get("available").asBoolean();
            bookToUpdate.setAvailable(isAvailable);
            if(!isAvailable){
                bookToUpdate.setTimesRead(bookToUpdate.getTimesRead() + 1);
                bookToUpdate.setTimesReadThisMonth(bookToUpdate.getTimesReadThisMonth() + 1);
            }
            Book updatedBook = bookRepository.save(bookToUpdate);
            System.out.println("\nbookToUpdate: " + bookToUpdate);
            System.out.println("\nupdatedBook: " + updatedBook);

        } else {
            System.out.println("bookToUpdate was not found in DB");
        }
    }
}
