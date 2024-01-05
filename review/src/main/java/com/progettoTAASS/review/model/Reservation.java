package com.progettoTAASS.review.model;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "user_reservetion_id", nullable = false)
    private User userReservation;

    public Reservation(String title, String author, User owner, Date date, User userReservation) {
        this.title = title;
        this.author = author;
        this.owner = owner;
        this.date = date;
        this.userReservation = userReservation;
    }

    public static Object serializeReservation(Reservation reservation) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        ObjectNode tree = objectMapper.valueToTree(reservation);
        tree.put("owner", objectMapper.valueToTree(reservation.getOwner()));
        tree.put("userReservation", objectMapper.valueToTree(reservation.getUserReservation()));
        tree.put("date", reservation.getDate().toString());
        try {
            return objectMapper.writeValueAsString(tree);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
