package com.progettoTAASS.author.repository;

import com.progettoTAASS.author.model.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findByName(String name);
}
