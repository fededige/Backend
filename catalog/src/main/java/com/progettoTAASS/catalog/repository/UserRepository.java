package com.progettoTAASS.catalog.repository;

import com.progettoTAASS.catalog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(int id);
    Page<User> findAll(Pageable pageable);
    User findUserByUsername(String username);
}
