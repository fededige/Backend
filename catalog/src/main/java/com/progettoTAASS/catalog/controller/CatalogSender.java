package com.progettoTAASS.catalog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.progettoTAASS.catalog.model.Book;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CatalogSender {
        private final RabbitTemplate rabbitTemplate;

        @Autowired
        public CatalogSender(RabbitTemplate rabbitTemplate) {
            this.rabbitTemplate = rabbitTemplate;
        }

        @Value("${rabbitmq.exchange.name}")
        private String exchange;

        @Value("${rabbitmq.routing.catalogReservation.key}")
        private String routingCatalogReservationKey;

        public void sendBook(Book book) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ObjectNode rootNode = objectMapper.createObjectNode();
                rootNode.put("title", book.getTitle());
                rootNode.put("author", book.getAuthor());
                rootNode.put("publishingDate", String.valueOf(book.getPublishingDate()));
                rootNode.put("available", book.isAvailable());
                rootNode.put("owner", objectMapper.valueToTree(book.getOwner()));
                String bookJson = objectMapper.writeValueAsString(rootNode);

                rabbitTemplate.convertAndSend(exchange, routingCatalogReservationKey, bookJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

}
