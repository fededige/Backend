package com.progettoTAASS.catalog.repository;

import com.progettoTAASS.catalog.model.Book;
import com.progettoTAASS.catalog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findById(int id);
    Page<Book> findAll(Pageable pageable);

    Page<Book> findBookByAvailable(boolean available, Pageable pageable);
    List<Book> findByOwner(User owner);
    Book findAllByAuthorAndPublishingDateAndTitleAndOwner(String author, Date publishingDate, String title, User owner);



    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);

}
