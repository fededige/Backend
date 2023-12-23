package com.progettoTAASS.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "coins")
    private int coins;

    @Override
    public String toString() { return "id: " + this.id + " - email: " + this.email + " - username: " + this.username + " - coins: " + this.coins; }
}
