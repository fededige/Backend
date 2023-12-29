package com.progettoTAASS.catalog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public User(String username) {
        this.username = username;
    }

    public String toString() {
        return "User [" +
            "\n\tid=" + id +
            "\n\tusername=" + username + "]";
    }
}
