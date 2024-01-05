package com.progettoTAASS.review.repository;

import com.progettoTAASS.review.model.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
}
