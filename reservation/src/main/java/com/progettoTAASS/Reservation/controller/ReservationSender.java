package com.progettoTAASS.Reservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.progettoTAASS.Reservation.model.Book;
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

}
