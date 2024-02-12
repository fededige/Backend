package com.progettoTAASS.user.rabbitMqConfig;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.queue.catalog.name}")
    private String catalogQueue;

    @Value("${rabbitmq.queue.reservation.name}")
    private String reservationQueue;

    @Value("${rabbitmq.queue.reviewUser.name}")
    private String reviewUserQueue;

    @Value("${rabbitmq.queue.reservationUser.name}")
    private String reservationUserQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.catalog.key}")
    private String routingCatalogKey;

    @Value("${rabbitmq.routing.reservation.key}")
    private String routingReservationKey;

    @Value("${rabbitmq.routing.reviewUser.key}")
    private String routingReviewUserKey;

    @Value("${rabbitmq.routing.reservationUser.key}")
    private String routingReservationUserKey;

    @Bean
    public Queue catalogQueue(){
        return new Queue(catalogQueue);
    }

    @Bean
    public Queue reservationQueue(){
        return new Queue(reservationQueue);
    }

    @Bean
    public Queue reviewUserQueue(){
        return new Queue(reviewUserQueue);
    }

    @Bean
    public Queue reservationUserQueue(){
        return new Queue(reservationUserQueue);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding catalogBinding(){
        return BindingBuilder
                .bind(catalogQueue())
                .to(exchange())
                .with(routingCatalogKey);
    }

    @Bean
    public Binding reservationBinding(){
        return BindingBuilder
                .bind(reservationQueue())
                .to(exchange())
                .with(routingReservationKey);
    }

    @Bean
    public Binding reviewUserBinding(){
        return BindingBuilder
                .bind(reviewUserQueue())
                .to(exchange())
                .with(routingReviewUserKey);
    }

    @Bean
    public Binding reservationUserBinding(){
        return BindingBuilder
                .bind(reservationUserQueue())
                .to(exchange())
                .with(routingReservationUserKey);
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
