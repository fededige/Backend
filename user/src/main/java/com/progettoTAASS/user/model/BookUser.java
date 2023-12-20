package com.progettoTAASS.user.model;

import com.progettoTAASS.user.enums.BookUserTypesEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "book_user")
public class BookUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "type")
    private BookUserTypesEnum type;
}
