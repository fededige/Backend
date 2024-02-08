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

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/")
@RestController
public class CatalogController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CatalogSender catalogSender;

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

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<String>> getAllBook(){
        List<Book> books = bookRepository.findAll();
        List<String> ret = new ArrayList<>();
        for (Book book : books){
            ret.add(Book.serializeBook(book));
        }
        return !books.isEmpty() ? ResponseEntity.ok(ret) : ResponseEntity.notFound().build();
    }

    /**
     * This method is used to get a book by ID
     * @param id the book id
     * @return Book
     */
    @GetMapping("/getBook/{id}")
    public ResponseEntity<String> getBookById(@PathVariable int id){
        Book book = bookRepository.findById(id);
        return book != null ? ResponseEntity.ok(Book.serializeBook(book)) : ResponseEntity.notFound().build();
    }

    /**
     * This method is used to get a book by OWNER_ID
     * @param owner_id the owner id
     * @return ResponseEntity< List<Book> >
     */
    @GetMapping("/getBookByOwner/{owner_id}")
    public ResponseEntity<List<String>> getBooksByOwner(@PathVariable int owner_id){
        List<Book> books = bookRepository.findByOwner(userRepository.findById(owner_id));
        List<String> ret = new ArrayList<>();
        for (Book book : books){
            ret.add(Book.serializeBook(book));
        }
        return !books.isEmpty() ? ResponseEntity.ok(ret) : ResponseEntity.notFound().build();
    }

    /**
     * This method get the LIMIT books most read
     * @return ResponseEntity< List<Book> >
     */
    @GetMapping("/getBestsellers")
    public ResponseEntity<List<String>> getMostReadEver(){
        Page<Book> mostRead = bookRepository.findAll(PageRequest.of(0, LIMIT, Sort.by(Sort.Order.desc("timesRead"))));
        System.out.println(mostRead);
        List<String> ret = new ArrayList<>();
        for (Book book : mostRead){
            ret.add(Book.serializeBook(book));
        }
        //se è vuota non serve ritornare una lista vuota
        return !mostRead.isEmpty() ? ResponseEntity.ok(ret) : ResponseEntity.notFound().build();
    }

    /**
     * This method get the LIMIT books most read THIS MONTH
     * @return ResponseEntity< List<Book> >
     */
    @GetMapping("/getMonthTrend")
    public ResponseEntity<List<String>> getMostReadThisMonth(){
        Page<Book> mostReadThisMonth = bookRepository.findAll(PageRequest.of(0, LIMIT, Sort.by(Sort.Order.desc("timesReadThisMonth"))));
        List<String> ret = new ArrayList<>();
        for (Book book : mostReadThisMonth){
            ret.add(Book.serializeBook(book));
        }
        //se è vuota non serve ritornare una lista vuota
        return !mostReadThisMonth.isEmpty() ? ResponseEntity.ok(ret) : ResponseEntity.notFound().build();
    }

    @GetMapping("/getBySearchWord")
    public ResponseEntity<List<String>> getBooksBySearchWord(@RequestParam String search){
        List<Book> searchByTitle = bookRepository.findByTitleContainingIgnoreCase(search);
        List<Book> searchByAuthor = bookRepository.findByAuthorContainingIgnoreCase(search);
        List<String> ret = new ArrayList<>();

        for(Book book: searchByAuthor){
            if(!searchByTitle.contains(book)){
                searchByTitle.add(book);
            }
        }

        for (Book book : searchByTitle){
            ret.add(Book.serializeBook(book));
        }

        return ResponseEntity.ok(ret);
    }


    /**
     * This method saves a book in DB.
     * @return ResponseEntity< Book >
     */
    @PostMapping(value = "/insert", consumes = "application/json")
    public ResponseEntity<String> saveBook(@RequestBody Book book){
        System.out.println(book);
        book.setTimesRead(0);
        book.setTimesReadThisMonth(0);
        book.setAvailable(true);
        book.setOwner(userRepository.findUserByUsername(book.getOwner().getUsername()));
        if(book.getPlot() != null){
            book.setPlot(book.getPlot().length() < 2040 ? book.getPlot() : book.getPlot().substring(0, 2040));
        }
        System.out.println(book);
        Book savedBook = bookRepository.save(book);
        System.out.println(savedBook);
        catalogSender.sendBook(book);
        return ResponseEntity.ok(Book.serializeBook(savedBook));
    }

    /**
     * This method removes a book from DB.
     */
    @DeleteMapping("/remove/{id}")
    public void removeBook(@PathVariable int id) {
        Book bookToRemove = bookRepository.findById(id);
        bookRepository.delete(bookToRemove);
        catalogSender.sendBook(bookToRemove);
    }

}