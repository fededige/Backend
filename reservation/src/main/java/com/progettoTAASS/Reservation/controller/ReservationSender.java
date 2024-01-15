package com.progettoTAASS.Reservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.progettoTAASS.Reservation.model.Book;
import com.progettoTAASS.Reservation.model.Reservation;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReservationSender {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ReservationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.reservationCatalog.key}")
    private String routingReservationCatalogKey;

    @Value("${rabbitmq.routing.reviewReservation.key}")
    private String routingReviewReservationKey;



    public void sendUpdatedBook(Book book) {
        ObjectMapper objectMapper = new ObjectMapper();
            try {
                ObjectNode rootNode = objectMapper.createObjectNode();
                rootNode.put("title", book.getTitle());
                rootNode.put("author", book.getAuthor());
                rootNode.put("publishingDate", book.getPublishingDate().toString());
                rootNode.put("available", book.isAvailable());
                rootNode.put("owner", objectMapper.valueToTree(book.getOwner()));
                String bookJson = objectMapper.writeValueAsString(rootNode);

                rabbitTemplate.convertAndSend(exchange, routingReservationCatalogKey, bookJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
    }

    public void sendNewReservation(Reservation reservation) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("title", reservation.getBook().getTitle());
            rootNode.put("author", reservation.getBook().getAuthor());
            rootNode.put("date", reservation.getDate().toString());
            rootNode.put("owner", objectMapper.valueToTree(reservation.getBook().getOwner()));
            rootNode.put("userReservation", objectMapper.valueToTree(reservation.getReservationUser()));
            String reservationJson = objectMapper.writeValueAsString(rootNode);

//            rabbitTemplate.convertAndSend(exchange, routingReservationUserKey, reservationJson);
            rabbitTemplate.convertAndSend(exchange, routingReviewReservationKey, reservationJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
