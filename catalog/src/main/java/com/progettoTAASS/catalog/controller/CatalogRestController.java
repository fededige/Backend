package com.progettoTAASS.catalog.controller;

import com.progettoTAASS.catalog.model.Book;
import com.progettoTAASS.catalog.model.User;
import com.progettoTAASS.catalog.repository.BookRepository;
import com.progettoTAASS.catalog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/")
@RestController
public class CatalogRestController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    //==========================================================
//    JUST FOR TESTING - REMOVE

    //TODO: REMOVE THIS, JUST FOR TESTING
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> users = userRepository.findAll();
        return !users.isEmpty() ? ResponseEntity.ok(users) : ResponseEntity.notFound().build();
    }

    /**
     * This method saves a user in DB. TODO: menage rabbitMq
     * @return ResponseEntity< User >
     */
    @PostMapping(value = "/insertUser", consumes = "application/json")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        System.out.println(user);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * This method removes a user from DB. TODO: menage rabbitMq
     */
    @DeleteMapping("/removeUser/{id}")
    public void removeUser(@PathVariable int id) {
        User userToRemove = userRepository.findById(id);
        userRepository.delete(userToRemove);
    }

    //==========================================================




    private static final int LIMIT = 10;

    //TODO: REMOVE THIS, JUST FOR TESTING
    @GetMapping("/getAllBook")
    public ResponseEntity<List<Book>> getAllBook(){
        List<Book> books = bookRepository.findAll();
        return !books.isEmpty() ? ResponseEntity.ok(books) : ResponseEntity.notFound().build();
    }

    /**
     * This method is used to get a book by ID
     * @param id the book id
     * @return Book
     */
    @GetMapping("/getBook/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id){
        Book book = bookRepository.findById(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    /**
     * This method is used to get a book by OWNER_ID
     * @param owner_id the owner id
     * @return ResponseEntity< List<Book> >
     */
    @GetMapping("/getBookByOwner/{owner}")
    public ResponseEntity<List<Book>> getBooksByOwner(@PathVariable int owner_id){
        List<Book> books = bookRepository.findByOwner(userRepository.findById(owner_id));
        return !books.isEmpty() ? ResponseEntity.ok(books) : ResponseEntity.notFound().build();
    }

    /**
     * This method get the LIMIT books most read
     * @return ResponseEntity< List<Book> >
     */
    @GetMapping("/getBestsellers")
    public ResponseEntity<List<Book>> getMostReadEver(){
        Page<Book> mostRead = bookRepository.findAll(PageRequest.of(0, LIMIT, Sort.by(Sort.Order.asc("timesRead"))));
        System.out.println(mostRead);
        //se è vuota non serve ritornare una lista vuota
        return !mostRead.isEmpty() ? ResponseEntity.ok(mostRead.getContent()) : ResponseEntity.notFound().build();
    }

    /**
     * This method get the LIMIT books most read THIS MONTH
     * @return ResponseEntity< List<Book> >
     */
    @GetMapping("/getMonthTrend")
    public ResponseEntity<List<Book>> getMostReadThisMonth(){
        Page<Book> mostReadThisMonth = bookRepository.findAll(PageRequest.of(0, LIMIT, Sort.by(Sort.Order.asc("timesReadThisMonth"))));

        //se è vuota non serve ritornare una lista vuota
        return !mostReadThisMonth.isEmpty() ? ResponseEntity.ok(mostReadThisMonth.getContent()) : ResponseEntity.notFound().build();
    }


    /**
     * This method saves a book in DB. TODO: menage rabbitMq
     * @return ResponseEntity< Book >
     */
    @PostMapping(value = "/insert", consumes = "application/json")
    public ResponseEntity<Book> saveBook(@RequestBody Book book){
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook);
    }

    /**
     * This method removes a book from DB. TODO: menage rabbitMq
     */
    @DeleteMapping("/remove/{id}")
    public void removeBook(@PathVariable int id) {
        Book bookToRemove = bookRepository.findById(id);
        bookRepository.delete(bookToRemove);
    }

}