package com.progettoTAASS.review.repository;

import com.progettoTAASS.review.model.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
}
