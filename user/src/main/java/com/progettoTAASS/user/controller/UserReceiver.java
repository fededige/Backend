package com.progettoTAASS.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.user.model.Reservation;
import com.progettoTAASS.user.model.User;
import com.progettoTAASS.user.repository.ReservationRepository;
import com.progettoTAASS.user.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableRabbit
public class UserReceiver {
    private final ReservationRepository reservationRepository;


    @Autowired
    public UserReceiver(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.reservationUser.name}")
    public void receiveUser(@Payload String message) {
        System.out.println("message received: " + message);
        ObjectMapper o = new ObjectMapper();
        // convert message received to jsonNode
        JsonNode messageObj;
        try {
             messageObj = o.readTree(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("messageObj received: " + messageObj);

        String title = messageObj.get("title").textValue();
        String author = messageObj.get("author").textValue();
        User owner;
        Date date;
        User userReservation;
        String dDate = messageObj.get("date").textValue();
        System.out.println("date received: " + dDate);
        DateFormat df;
        if (Character.isDigit(dDate.charAt(0))){ // check date input format (if it starts with a number then if in yyyy-MM-dd format
             df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }
        else {
            df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        }

        try {
            date = df.parse(dDate);
            owner = o.readValue(messageObj.get("owner").toString(), User.class);
            userReservation = o.readValue(messageObj.get("userReservation").toString(), User.class);
        } catch (JsonProcessingException | ParseException e) {
            throw new RuntimeException(e);
        }


        Reservation newReservation = new Reservation(title, author, owner, date, userReservation);

        reservationRepository.save(newReservation);

    }
}
