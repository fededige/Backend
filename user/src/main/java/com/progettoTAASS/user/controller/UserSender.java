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

        @Value("${spring.rabbitmq.template.exchange}")
        private String exchange;

        @Value("${spring.rabbitmq.template.routingkey}")
        private String routingkey;

        public void sendNewUser(User user) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ObjectNode rootNode = objectMapper.createObjectNode();
                rootNode.put("username", user.getUsername());
                String userJson = objectMapper.writeValueAsString(rootNode);

                rabbitTemplate.convertAndSend(exchange,routingkey, userJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

}
