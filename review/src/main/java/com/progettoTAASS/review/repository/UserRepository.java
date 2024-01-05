package com.progettoTAASS.review.repository;

import com.progettoTAASS.review.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
