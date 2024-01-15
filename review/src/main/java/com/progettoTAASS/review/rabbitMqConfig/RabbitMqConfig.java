package com.progettoTAASS.review.rabbitMqConfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.queue.reviewUser.name}")
    private String reviewUserQueue;

    @Value("${rabbitmq.queue.reviewReservation.name}")
    private String reviewReservationQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.reviewUser.key}")
    private String routingReviewUserKey;

    @Value("${rabbitmq.routing.reviewReservation.key}")
    private String routingReviewReservationKey;

    @Bean
    public Queue reviewUserQueue(){
        return new Queue(reviewUserQueue);
    }

    @Bean
    public Queue reviewReservationQueue(){
        return new Queue(reviewReservationQueue);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding reviewUserBinding(){
        return BindingBuilder
                .bind(reviewUserQueue())
                .to(exchange())
                .with(routingReviewUserKey);
    }

    @Bean
    public Binding reviewReservationBinding(){
        return BindingBuilder
                .bind(reviewReservationQueue())
                .to(exchange())
                .with(routingReviewReservationKey);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
