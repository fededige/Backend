package com.progettoTAASS.Reservation.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "publishing_date")
    private Date publishingDate;

    @Column(name = "available")
    public boolean available;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    public static String serializeBook(Book book){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode tree = objectMapper.valueToTree(book);
        tree.put("owner", objectMapper.valueToTree(book.getOwner()));
        try {
            String incorrectJson = objectMapper.writeValueAsString(tree);
            return incorrectJson.replaceAll(
                    "(?<=\\{|, ?)([a-zA-Z]+?): ?(?![ \\{\\[])(.+?)(?=,|})", "\"$1\": \"$2\"");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

