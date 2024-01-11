package com.progettoTAASS.review.repository;

import com.progettoTAASS.review.model.Reservation;
import com.progettoTAASS.review.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    Reservation findByDateAndUserReservation(Date date, User userReservation);
}
