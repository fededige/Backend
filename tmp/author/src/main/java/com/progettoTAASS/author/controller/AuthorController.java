package com.progettoTAASS.author.controller;

import com.progettoTAASS.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.progettoTAASS.author.model.Author;
import com.progettoTAASS.author.model.Book;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") // per accettare le richieste del frontend
@RestController
@RequestMapping("/")
public class AuthorController {
    private final ProducerService producerService;

    @Autowired
    AuthorRepository repository;

    @Autowired
    public AuthorController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/authors")
    public List<Author> getAllAuthors() {
        System.out.println("Get all Authors...");

        List<Author> authors = new ArrayList<>();
        repository.findAll().forEach(authors::add);

        return authors;
    }

    @PostMapping("/authors/insert")
    public Author insertAuthor(@RequestBody Author author) {
        System.out.println("Insert book " + author.getName() + "...");

        Author _author = repository.save(new Author(author.getName()));
        return _author;
    }

    @PostMapping("/produce")
    public ResponseEntity<String> sendMessage(@RequestBody Book book) {
        producerService.sendMessage(book);
        System.out.println("book sent: " + book);
        String response = "\nmessaggio ricevuto";
        return ResponseEntity.ok(response);
    }
}
