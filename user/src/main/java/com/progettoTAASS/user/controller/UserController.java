package com.progettoTAASS.user.controller;

import com.progettoTAASS.user.model.*;
import com.progettoTAASS.user.repository.ReservationRepository;
import com.progettoTAASS.user.repository.ReviewRepository;
import com.progettoTAASS.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserSender userSender;

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = (List<User>)userRepository.findAll();
        System.out.println(users);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserInfos(@PathVariable int id) {
        User currentUser = userRepository.findUserById(id);

        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/insert")
    public ResponseEntity<User> insertNewUser(@RequestBody User newUser) {
        User u = userRepository.save(newUser);
        userSender.sendNewUser(u);

        return ResponseEntity.ok(u);
    }

    // 405: METHOD NOT ALLOWED (?)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        User userToDelete = userRepository.findUserById(id);
        userRepository.delete(userToDelete);
        userSender.sendNewUser(userToDelete);

        return ResponseEntity.ok(userToDelete);
    }

    @GetMapping("/{id}/coins")
    public ResponseEntity<Integer> getUserCoins(@PathVariable int id) {
        User currentUser = userRepository.findUserById(id);
        if(currentUser == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(currentUser.getCoins());
    }

    // Potrebbe essere gestito da un microservizio a parte
    @PostMapping("/{id}/coins/add")
    public ResponseEntity<User> addUserCoins(@PathVariable int id, @RequestBody int coins) {
        User currentUser = userRepository.findUserById(id);
        if(currentUser == null)
            return ResponseEntity.notFound().build();

        currentUser.setCoins(coins);
        User savedUser = userRepository.save(currentUser);

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/getAllReservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> allReservation = (List<Reservation>) reservationRepository.findAll();

        return ResponseEntity.ok(allReservation);
    }

    @GetMapping("/{id}/read")
    public ResponseEntity<List<Reservation>> getBooksRead(@PathVariable int id) {
        List<Reservation> listBookRead = reservationRepository.findReservationByUserReservationId(id);

        return ResponseEntity.ok(listBookRead);
    }

    @GetMapping("/{id}/loan")
    public ResponseEntity<List<Reservation>> getBooksLoan(@PathVariable int id) {
        List<Reservation> listBookLoan = reservationRepository.findReservationByOwnerId(id);

        return ResponseEntity.ok(listBookLoan);
    }

    @GetMapping("/{id}/review/wrote")
    public ResponseEntity<List<Review>> getReviewWrote(@PathVariable int id) {
        List<Review> reviewWrote = reviewRepository.findReviewByWriterId(id);

        return ResponseEntity.ok(reviewWrote);
    }

    @GetMapping("/{id}/review/received")
    public ResponseEntity<List<Review>> getReviewReceived(@PathVariable int id) {
        List<Reservation> userBook = reservationRepository.findReservationByOwnerId(id);
        List<Review> reviewReceived = null;
        for (Reservation r: userBook) {
            List<Review> queryResult = reviewRepository.findReviewByReservationId(r.getId());
            if(queryResult != null)
                reviewReceived.addAll(queryResult);
        }

        return ResponseEntity.ok(reviewReceived);
    }


}
