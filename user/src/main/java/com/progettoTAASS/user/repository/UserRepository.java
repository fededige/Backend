package com.progettoTAASS.user.repository;

import com.progettoTAASS.user.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findDistinctFirstById(int id);
}
