package com.progettoTAASS.user.model;

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

    @Column(name = "coins")
    private int coins;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User() {}

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getCoins() {
        return this.coins;
    }

    public void setCoins(int coins) { this.coins = coins; }

    @Override
    public String toString() { return "id: " + this.id + " - email: " + this.email + " - username: " + this.username + " - coins: " + this.coins; }
}
