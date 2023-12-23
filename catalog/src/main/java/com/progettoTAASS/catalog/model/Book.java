package com.progettoTAASS.catalog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.progettoTAASS.catalog.enums.BookConditionsEnum;
import com.progettoTAASS.catalog.enums.BookGenresEnum;
import com.progettoTAASS.catalog.enums.BookLoanDurationsEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "publishing_date")
    private Date publishingDate;

    @Column(name = "genre")
    private BookGenresEnum genre;

    @Nullable
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
    private BookLoanDurationsEnum loanDuration;

    @Column(name = "times_read")
    private int timesRead;

    @Column(name = "times_read_this_month")
    private int timesReadThisMonth;

    @Column(name = "available")
    private boolean available;


    public String toString() {
        return "Book [" +
            "\n\tid=" + id +
            "\n\ttitle=" + title +
            "\n\tauthor=" + author +
            "\n\tpublishingDate=" + publishingDate +
            "\n\tplot= \n\t\"" + plot + "\"\n" + 
            "\n\towner=" + owner +
            "\n\ttimesRead=" + timesRead +
            "\n\tloan_duration=" + loanDuration +
            "\n\tavailable=" + available +
            "\n\tpublisher=" + publisher + "]";
    }

//    public void setId(int id) {
//        this.id = id;
//    }
}
