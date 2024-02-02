package com.progettoTAASS.Reservation.rabbitMqConfig;
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

    @Value("${rabbitmq.queue.reservation.name}")
    private String reservationQueue;

    @Value("${rabbitmq.queue.catalogReservation.name}")
    private String catalogReservationQueue;

    @Value("${rabbitmq.queue.reservationCatalog.name}")
    private String reservationCatalogQueue;

    @Value("${rabbitmq.queue.reviewReservation.name}")
    private String reviewReservationQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.reservation.key}")
    private String routingReservationKey;

    @Value("${rabbitmq.routing.catalogReservation.key}")
    private String routingCatalogReservationKey;

    @Value("${rabbitmq.routing.reservationCatalog.key}")
    private String routingReservationCatalogKey;

    @Value("${rabbitmq.routing.reviewReservation.key}")
    private String routingReviewReservationKey;


    @Bean
    public Queue reservationQueue(){
        return new Queue(reservationQueue);
    }

    @Bean
    public Queue catalogReservationQueue(){
        return new Queue(catalogReservationQueue);
    }

    @Bean
    public Queue reservationCatalogQueue(){
        return new Queue(reservationCatalogQueue);
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
    public Binding reservationBinding(){
        return BindingBuilder
                .bind(reservationQueue())
                .to(exchange())
                .with(routingReservationKey);
    }
    @Bean
    public Binding catalogReservationBinding(){
        return BindingBuilder
                .bind(catalogReservationQueue())
                .to(exchange())
                .with(routingCatalogReservationKey);
    }

    @Bean
    public Binding reservationCatalogBinding(){
        return BindingBuilder
                .bind(reservationCatalogQueue())
                .to(exchange())
                .with(routingReservationCatalogKey);
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