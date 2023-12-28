package com.progettoTAASS.Reservation.repository;
import com.progettoTAASS.Reservation.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);

}
