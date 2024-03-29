package com.progettoTAASS.Reservation.repository;

import com.progettoTAASS.Reservation.model.Book;
import com.progettoTAASS.Reservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface BookRepository extends CrudRepository<Book, Integer> {

    Book findById(int id);
    Book findAllByAuthorAndPublishingDateAndTitleAndOwner(String author, Date publishingDate, String title, User owner);
}

