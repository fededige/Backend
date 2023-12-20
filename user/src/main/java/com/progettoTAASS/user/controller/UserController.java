package com.progettoTAASS.user.controller;

import com.progettoTAASS.user.model.Reservation;
import com.progettoTAASS.user.model.User;
import com.progettoTAASS.user.repository.ReservationRepository;
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

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserInfos(@PathVariable int id) {
        User currentUser = userRepository.findDistinctFirstById(id);

        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/insert")
    public ResponseEntity<User> insertNewUser(@RequestBody User newUser) {
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

    @GetMapping("/{id}/read")
    private ResponseEntity<List<Reservation>> getBooksRead(@PathVariable int id) {
        List<Reservation> listBookRead = reservationRepository.findReservationByUserReservationId(id);

        return ResponseEntity.ok(listBookRead);
    }

    @GetMapping("/{id}/loan")
    private ResponseEntity<List<Reservation>> getBooksLoan(@PathVariable int id) {
        List<Reservation> listBookLoan = reservationRepository.findReservationByOwnerId(id);

        return ResponseEntity.ok(listBookLoan);
    }
}
