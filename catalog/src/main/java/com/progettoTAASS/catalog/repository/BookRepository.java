package com.progettoTAASS.catalog.repository;

import com.progettoTAASS.catalog.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    Book findById(int id);


}
