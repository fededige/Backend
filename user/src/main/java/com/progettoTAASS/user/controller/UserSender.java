package com.progettoTAASS.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.progettoTAASS.user.model.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserSender {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

//        @Value("${spring.rabbitmq.template.exchange}")
//        private String exchange;
//
//        @Value("${spring.rabbitmq.template.routingkey}")
//        private String routingkey;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.catalog.key}")
    private String routingCatalogKey;

    @Value("${rabbitmq.routing.reservation.key}")
    private String routingReservationKey;

    @Value("${rabbitmq.routing.reviewUser.key}")
    private String routingReviewUserKey;

    public void sendNewUser(User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("username", user.getUsername());
            rootNode.put("email", user.getEmail());
            String userJson = objectMapper.writeValueAsString(rootNode);

            rabbitTemplate.convertAndSend(exchange, routingCatalogKey, userJson);
            rabbitTemplate.convertAndSend(exchange, routingReservationKey, userJson);
            rabbitTemplate.convertAndSend(exchange, routingReviewUserKey, userJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
