package com.progettoTAASS.Reservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.Reservation.model.Reservation;
import com.progettoTAASS.Reservation.model.User;
import com.progettoTAASS.Reservation.repository.BookRepository;
import com.progettoTAASS.Reservation.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import com.progettoTAASS.Reservation.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import com.progettoTAASS.Reservation.repository.ReservationRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RequestMapping("/")
@RestController
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationSender reservationSender;

    /* Book is returned
        and becomes available again*/
    @PutMapping("/BookAvailable/{id}")
    public ResponseEntity<String> makeBookAvailable(@PathVariable int id){
        Reservation reservation = reservationRepository.findById(id);
        Book book = reservation.getBook();
        book.setAvailable(true);
        //bookRepository.save(book)
        reservation.setBook(book);
        Reservation updatedReservation = reservationRepository.save(reservation);
        reservationSender.sendUpdatedBook(reservation.getBook());
        System.out.println("updatedReservation" + updatedReservation);
        return ResponseEntity.ok(Book.serializeBook(book));
    }

    @PostMapping("/reserveBook")
    public ResponseEntity<String> reserveBook(@RequestBody JsonNode requestBody) {
        ObjectMapper o = new ObjectMapper();
        Book reservationBook;
        User reservationUser;
        Date reservationDate;
        try {
            reservationBook = o.readValue(requestBody.get("book").toString(), Book.class);
            reservationUser = o.readValue(requestBody.get("ResevationUser").toString(), User.class);
            reservationDate = o.readValue(requestBody.get("date").toString(), Date.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Reservation newReservation = new Reservation(reservationDate, reservationBook, reservationUser);
        System.out.println("\n  newReservation: " + newReservation);
        Book book = bookRepository.findById(newReservation.getBook().getId());
        book.setAvailable(false);
//        bookRepository.save(book);

        newReservation.setDate(dateTimeNow());
        System.out.println("dateTimeNow(): " + newReservation.getDate());
        newReservation.setBook(book);
        System.out.println("\n  newReservation with new Date: " + newReservation);
        Reservation savedReservation = reservationRepository.save(newReservation);
        System.out.println("\n  savedReservation with new Date: " + savedReservation);

        reservationSender.sendUpdatedBook(savedReservation.getBook());
        reservationSender.sendNewReservation(savedReservation);
        return ResponseEntity.ok(Reservation.serializeReservation(savedReservation));
    }

    private Date dateTimeNow(){
        return java.sql.Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Europe/Rome")));
    }

    @GetMapping("/getAllReservation")
    public ResponseEntity<List<String>> getAllReservation(){
        List<Reservation> reservations = reservationRepository.findAll();
        List<String> reservationsJson = new ArrayList<>();
        for (Reservation r : reservations){
            reservationsJson.add(Reservation.serializeReservation(r));
        }
        return !reservations.isEmpty() ? ResponseEntity.ok(reservationsJson) : ResponseEntity.notFound().build();
    }

    @GetMapping("/getReservation/{id}")
    public ResponseEntity<String> getReservationById(@PathVariable int id){
        Reservation reservation = reservationRepository.findById(id);
        System.out.println(reservation);
        return reservation != null ? ResponseEntity.ok(Reservation.serializeReservation(reservation)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/getUser/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username){
        User user = userRepository.findUserByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/getBook")
    public ResponseEntity<String> getBook(@RequestBody JsonNode requestBody){
        ObjectMapper objectMapper = new ObjectMapper();
        Book book;
        User user;
        System.out.println("requestBody" + requestBody);
        System.out.println("owner form json: " + requestBody.get("owner").toString());

        try {
            user = objectMapper.readValue(requestBody.get("owner").toString(), User.class);
            System.out.println("owner form json after convertion: " + user);

//            book = bookRepository.findAllByAuthorAndPublishingDateAndTitle(requestBody.get("author").textValue() , objectMapper.readValue(requestBody.get("publishingDate").toString(), Date.class), requestBody.get("title").textValue());
            book = bookRepository.findAllByAuthorAndPublishingDateAndTitleAndOwner(requestBody.get("author").textValue() , objectMapper.readValue(requestBody.get("publishingDate").toString(), Date.class), requestBody.get("title").textValue(), objectMapper.readValue(requestBody.get("owner").toString(), User.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return book != null ? ResponseEntity.ok(Book.serializeBook(book)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/getReservationsBorrowed/{username}")
    public ResponseEntity<List<String>> getReservationsBorrowed(@PathVariable String username) {
        User currentUser = userRepository.findUserByUsername(username);
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        List<Reservation> listReservation = reservationRepository.findAllByReservationUser(currentUser);

        List<String> serializableList = new ArrayList<>();
        for (Reservation reservation : listReservation) {
            serializableList.add(Reservation.serializeReservation(reservation));
        }
        return ResponseEntity.ok(serializableList);
    }

    @GetMapping("/getReservationsLend/{username}")
    public ResponseEntity<List<String>> getReservationsLend(@PathVariable String username) {
        User currentUser = userRepository.findUserByUsername(username);
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        List<Reservation> listReservation = reservationRepository.findAllByBook_Owner(currentUser);

        List<String> serializableList = new ArrayList<>();
        for (Reservation reservation : listReservation) {
            serializableList.add(Reservation.serializeReservation(reservation));
        }
        return ResponseEntity.ok(serializableList);
    }

    /*
    * metodi solo per il testing
    * */

    @PostMapping("/insertUser")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userRepository.findAll();
        return !users.isEmpty() ? ResponseEntity.ok(users) : ResponseEntity.notFound().build();
    }

    @PostMapping("/insert")
    public ResponseEntity<String> saveBook(@RequestBody Book book){
        book.setOwner(userRepository.findUserByUsername(book.getOwner().getUsername()));
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(Book.serializeBook(savedBook));
    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<String>> getAllBooks(){
        List<Book> books = (List<Book>) bookRepository.findAll();
        List<String> booksJson = new ArrayList<>();
        for (Book b : books){
            booksJson.add(Book.serializeBook(b));
        }
        return !books.isEmpty() ? ResponseEntity.ok(booksJson) : ResponseEntity.notFound().build();
    }
}
