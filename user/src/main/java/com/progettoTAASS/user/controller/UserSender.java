package com.progettoTAASS.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            ObjectMapper o = new ObjectMapper();
            String userJson = "";
            try {
                userJson = o.writeValueAsString(user);
                System.out.println(userJson);
                rabbitTemplate.convertAndSend(exchange,routingkey, userJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

}
