package com.progettoTAASS.user.repository;

import com.progettoTAASS.user.model.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    List<Review> findReviewByReservationId(int id);

    List<Review> findReviewByWriterId(int id);
}
