package com.progettoTAASS.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "date")
    private Date published;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Reservation reservation;
}
