package com.progettoTAASS.catalog.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    public String toString() {
        return "User [" +
            "\n\tid=" + id +
            "\n\tusername=" + username +
            "\n\temail=" + email + "]";
    }
}
