package com.progettoTAASS.catalog.repository;

import com.progettoTAASS.catalog.model.Book;
import com.progettoTAASS.catalog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findById(int id);
    Page<Book> findAll(Pageable pageable);
    List<Book> findByOwner(User owner);



}
