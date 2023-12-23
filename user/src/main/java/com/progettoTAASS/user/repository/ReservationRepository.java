package com.progettoTAASS.user.repository;

import com.progettoTAASS.user.model.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    List<Reservation> findReservationByOwnerId(int id);
    List<Reservation> findReservationByUserReservationId(int id);
}
