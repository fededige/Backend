package com.progettoTAASS.review.repository;

import com.progettoTAASS.review.model.Review;
import com.progettoTAASS.review.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    Review findByDatePublishedAndWriter(Date date, User writer);
    List<Review> findAllByWriter(User writer);
    List<Review> findAllByReservation_Owner(User writer);
}
