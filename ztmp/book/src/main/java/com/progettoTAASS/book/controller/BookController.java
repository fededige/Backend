package com.progettoTAASS.book.controller;
import com.progettoTAASS.book.model.Book;
import com.progettoTAASS.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") // per accettare le richieste del frontend
@RestController
@RequestMapping("/")
public class BookController {

    @Autowired
    BookRepository repository;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        System.out.println("Get all Books...");

        List<Book> books = new ArrayList<>();
        repository.findAll().forEach(books::add);

        return books;
    }

    @GetMapping("/books/{title}")
    public List<Book> findByTitle(@PathVariable String title) {
        System.out.println("Get all book called" + title + "...");

        List<Book> books =  repository.findByTitle(title);

        return books;
    }

    @PostMapping("/books/insert")
    public Book insertBook(@RequestBody Book book) {
        System.out.println("Insert book " + book.getTitle() + "...");

        Book _book = repository.save(new Book(book.getTitle(), book.getNumPages()));
        return _book;
    }

}
