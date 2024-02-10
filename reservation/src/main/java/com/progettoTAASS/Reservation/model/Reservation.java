package com.progettoTAASS.Reservation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "reservation")
    public class Reservation {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;

//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Column(name = "date")
        private Date date;

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "book", nullable = false)
        private Book book;

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "reservation_user", nullable = false)
        private User reservationUser;

    public Reservation(Date date, Book book, User user) {
        this.date = date;
        this.book = book;
        this.reservationUser = user;
    }

    public static String serializeReservation(Reservation newRes) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode tree = objectMapper.valueToTree(newRes);
        tree.put("book", objectMapper.valueToTree(Book.serializeBook(newRes.getBook())));
        tree.put("reservationUser", objectMapper.valueToTree(newRes.getReservationUser()));
        tree.put("date", newRes.getDate().toString());
        try {
            String incorrectJson = objectMapper.writeValueAsString(tree);
            System.out.println(incorrectJson);
            incorrectJson = incorrectJson.replaceAll(
                    "(?<=\\{|, ?)([a-zA-Z]+?): ?(?![ \\{\\[])(.+?)(?=,|})", "\"$1\": \"$2\"");
            System.out.println(incorrectJson);
            incorrectJson = incorrectJson.replace("\\","");
            System.out.println(incorrectJson);
            incorrectJson = incorrectJson.replace("\"book\":\"","\"book\":");
            System.out.println(incorrectJson);
            return incorrectJson.replace("}\",\"reservationUser\":","},\"reservationUser\":");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
