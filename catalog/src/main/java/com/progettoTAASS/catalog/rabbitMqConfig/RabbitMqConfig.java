package com.progettoTAASS.catalog.rabbitMqConfig;

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
//    @Value("${spring.rabbitmq.host}")
//    String host;
//
//    @Value("${spring.rabbitmq.username}")
//    String username;
//
//    @Value("${spring.rabbitmq.password}")
//    String password;

//    @Bean
//    CachingConnectionFactory connectionFactory() {
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
//        cachingConnectionFactory.setUsername(username);
//        cachingConnectionFactory.setPassword(password);
//        return cachingConnectionFactory;
//    }

    @Value("${rabbitmq.queue.catalog.name}")
    private String catalogQueue;

    @Value("${rabbitmq.queue.reservation.name}")
    private String reservationQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.catalog.key}")
    private String routingCatalogKey;

    @Value("${rabbitmq.routing.reservation.key}")
    private String routingReservationKey;

    @Bean
    public Queue catalogQueue(){
        return new Queue(catalogQueue);
    }

    @Bean
    public Queue reservationQueue(){
        return new Queue(reservationQueue);
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



//package com.progettoTAASS.catalog.rabbitMqConfig;
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMqConfig {
//    @Value("${spring.rabbitmq.template.default-receive-queue}")
//    private String queue;
//
//    @Value("${spring.rabbitmq.template.exchange}")
//    private String exchange;
//
//    @Value("${spring.rabbitmq.template.routing-key}")
//    private String routingKey;
//
//    @Value("${spring.rabbitmq.username}")
//    private String username;
//
//    @Value("${spring.rabbitmq.password}")
//    private String password;
//
//    @Value("${spring.rabbitmq.host}")
//    private String host;
//
//    @Bean
//    Queue queue() {
//        return new Queue(queue, true);
//    }
//
//    @Bean
//    Exchange myExchange() {
//        return ExchangeBuilder.directExchange(exchange).durable(true).build();
//    }
//
//    @Bean
//    Binding binding() {
//        return BindingBuilder
//                .bind(queue())
//                .to(myExchange())
//                .with(routingKey)
//                .noargs();
//    }
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
//        cachingConnectionFactory.setUsername(username);
//        cachingConnectionFactory.setPassword(password);
//        return cachingConnectionFactory;
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        //rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }
//}
