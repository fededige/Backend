package com.progettoTAASS.user.controller;

import com.progettoTAASS.user.model.*;
import com.progettoTAASS.user.repository.ReservationRepository;
import com.progettoTAASS.user.repository.ReviewRepository;
import com.progettoTAASS.user.repository.UserRepository;
import com.progettoTAASS.user.repository.WalletRepository;
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
    private WalletRepository walletRepository;

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = (List<User>)userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserInfos(@PathVariable int id) {
        User currentUser = userRepository.findDistinctFirstById(id);

        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/insert")
    public ResponseEntity<User> insertNewUser(@RequestBody User newUser) {
        Wallet w = newUser.getWallet();
        walletRepository.save(w);
        User u = userRepository.save(newUser);

        return ResponseEntity.ok(u);
    }

    @GetMapping("/{id}/coins")
    public ResponseEntity<Integer> getUserCoins(@PathVariable int id) {
        User currentUser = userRepository.findDistinctFirstById(id);
        if(currentUser == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(currentUser.getWallet().getCoins());
    }

    @PostMapping("/{id}/coins/add")
    public ResponseEntity<Wallet> addUserCoins(@PathVariable int id, @RequestBody int coins) {
        User currentUser = userRepository.findDistinctFirstById(id);
        if(currentUser == null)
            return ResponseEntity.notFound().build();

        Wallet w = walletRepository.findWalletById(currentUser.getWallet().getId());

        w.setCoins(w.getCoins() + coins);

        Wallet savedWallet = walletRepository.save(w);
        currentUser.setWallet(savedWallet);

        return ResponseEntity.ok(savedWallet);
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
        List<Review> reviewWrote = reviewRepository.findReviewByReservationId(id);

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
