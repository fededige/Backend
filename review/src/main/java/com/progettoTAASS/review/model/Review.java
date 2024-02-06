package com.progettoTAASS.review.model;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "evaluation")
    private int evaluation;

    @Column(name = "date")
    private Date datePublished;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    public static String serializeReview(Review review) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode tree = objectMapper.valueToTree(review);
        tree.put("writer", objectMapper.valueToTree(review.getWriter()));
        tree.put("reservation", objectMapper.valueToTree(Reservation.serializeReservation(review.getReservation())));
        tree.put("date", review.getDatePublished().toString());
        try {
            String incorrectJson = objectMapper.writeValueAsString(tree);
            return incorrectJson.replaceAll(
                    "(?<=\\{|, ?)([a-zA-Z]+?): ?(?![ \\{\\[])(.+?)(?=,|})", "\"$1\": \"$2\"");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
