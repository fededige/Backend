package com.progettoTAASS.Reservation.repository;

import com.progettoTAASS.Reservation.model.Book;
import com.progettoTAASS.Reservation.model.Reservation;
import com.progettoTAASS.Reservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Reservation findById(int id);
    List<Reservation> findAllByReservationUser(User user);

    List<Reservation> findAllByBook_Owner(User user);
}


