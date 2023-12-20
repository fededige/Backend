package com.progettoTAASS.catalog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.progettoTAASS.catalog.enums.BookConditionsEnum;
import com.progettoTAASS.catalog.enums.BookGenresEnum;
import com.progettoTAASS.catalog.enums.BookLoanDurationsEnum;
import jakarta.persistence.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;


import java.util.Date;

@Indexed
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @FullTextField()
    @Column(name = "title")
    private String title;

    @FullTextField()
    @Column(name = "author")
    private String author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "publishing_date")
    private Date publishingDate;

    @Column(name = "genre")
    private BookGenresEnum genre;

    @Column(name = "cover")
    private byte[] cover;

    @Column(name = "plot")
    private String plot;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "condition")
    private BookConditionsEnum condition;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "loan_duration")
    private BookLoanDurationsEnum loan_duration;

    public String toString() {
        return "Book [" +
            "\n\tid=" + id +
            "\n\ttitle=" + title +
            "\n\tauthor=" + author +
            "\n\tpublishingDate=" + publishingDate +
            "\n\tplot= \n\t\"" + plot + "\"\n" + 
            "\n\towner=" + owner +
            "\n\tloan_duration=" + loan_duration +
            "\n\tpublisher=" + publisher + "]";
    }

//    public int getId() {
//        return id;
//    }
}
