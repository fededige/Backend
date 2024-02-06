package com.progettoTAASS.catalog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "publishing_date")
    private Date publishingDate;

    @Nullable
    @Column(name = "genre")
    private BookGenresEnum genre;

    @Nullable
    @Column(name = "cover", columnDefinition="text")
    private String cover;

    @Nullable
    @Column(name = "plot", length = 2048)
    private String plot;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "condition")
    private BookConditionsEnum condition;

    @Nullable
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
