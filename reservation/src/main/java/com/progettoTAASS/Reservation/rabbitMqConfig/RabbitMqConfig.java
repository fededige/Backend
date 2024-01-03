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

    @Value("${rabbitmq.queue.catalog.name}")
    private String catalogQueue;

    @Value("${rabbitmq.queue.reservation.name}")
    private String reservationQueue;

    @Value("${rabbitmq.queue.catalogReservation.name}")
    private String catalogReservationQueue;

    @Value("${rabbitmq.queue.reservationCatalog.name}")
    private String reservationCatalogQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.catalog.key}")
    private String routingCatalogKey;

    @Value("${rabbitmq.routing.reservation.key}")
    private String routingReservationKey;

    @Value("${rabbitmq.routing.catalogReservation.key}")
    private String routingCatalogReservationKey;

    @Value("${rabbitmq.routing.reservationCatalog.key}")
    private String routingReservationCatalogKey;

    @Bean
    public Queue catalogQueue(){
        return new Queue(catalogQueue);
    }

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