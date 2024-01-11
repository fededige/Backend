package com.progettoTAASS.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.review.model.Reservation;
import com.progettoTAASS.review.model.Review;
import com.progettoTAASS.review.model.User;
import com.progettoTAASS.review.repository.ReservationRepository;
import com.progettoTAASS.review.repository.ReviewRepository;
import com.progettoTAASS.review.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;


    @PostMapping(value = "/writeReview", consumes = "application/json")
    public ResponseEntity<String> writeReview(@RequestBody JsonNode requestBody){
        System.out.println("requestBody: " + requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        String title = requestBody.get("title").textValue();
        int evaluation =  requestBody.get("evaluation").intValue();
        String text = requestBody.get("text").textValue();
        String writerUsername = requestBody.get("writerUsername").textValue();
        Date dateReservation;
        String usernameUserReservation = requestBody.get("usernameUserReservation").textValue();

        try {
            dateReservation = objectMapper.readValue(requestBody.get("dateReservation").toString(), Date.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        User writer = userRepository.findByUsername(writerUsername);
        User userReservation = userRepository.findByUsername(usernameUserReservation);

        if (writer == null || userReservation == null){
            System.out.println("writer or userReservation == null");
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = reservationRepository.findByDateAndUserReservation(dateReservation, userReservation);

        if (reservation == null){
            System.out.println("reservation == null");
            return ResponseEntity.notFound().build();
        }

        if (!writer.getUsername().equals(reservation.getUserReservation().getUsername())){
            System.out.println("writer user != reservation user");
            return ResponseEntity.notFound().build();
        }

        Review newReview = new Review();
        newReview.setTitle(title);
        newReview.setEvaluation(evaluation);
        newReview.setText(text);
        newReview.setDatePublished(dateTimeNow());
        newReview.setWriter(writer);
        newReview.setReservation(reservation);

        Review reviewSaved = reviewRepository.save(newReview);

        return ResponseEntity.ok(Review.serializeReview(reviewSaved));
    }

    private Date dateTimeNow(){
        return java.sql.Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Europe/Rome")));
    }


    @DeleteMapping(value = "/deleteReview", consumes = "application/json")
    public ResponseEntity<String> deleteReview(@RequestBody JsonNode requestBody){
        System.out.println("requestBody: " + requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        Date datePublished;
        String writerUsername = requestBody.get("writerUsername").textValue();
        try {
            datePublished = objectMapper.readValue(requestBody.get("datePublishedReview").toString(), Date.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        User writer = userRepository.findByUsername(writerUsername);

        if (writer == null){
            System.out.println("writer == null");
            return ResponseEntity.notFound().build();
        }

        Review existingReview = reviewRepository.findByDatePublishedAndWriter(datePublished, writer);
        if (existingReview != null){
            reviewRepository.delete(existingReview);
        }
        return existingReview != null ? ResponseEntity.ok(Review.serializeReview(existingReview)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/getAllReview")
    public ResponseEntity<List<Review>> getAllReview(){
        List<Review> review = (List<Review>) reviewRepository.findAll();
        return !review.isEmpty() ? ResponseEntity.ok(review) : ResponseEntity.notFound().build();
    }

    @GetMapping("/getReviewWrote/{username}")
    public ResponseEntity<List<String>> getReviewWrote(@PathVariable String username) {
        User currentUser = userRepository.findByUsername(username);
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        List<Review> listReview = reviewRepository.findAllByWriter(currentUser);

        List<String> serializableList = new ArrayList<>();
        for (Review review : listReview) {
            serializableList.add(Review.serializeReview(review));
        }
        return ResponseEntity.ok(serializableList);
    }

    @GetMapping("/getReviewReceived/{username}")
    public ResponseEntity<List<String>> getReviewReceived(@PathVariable String username) {
        User currentUser = userRepository.findByUsername(username);
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        List<Review> listReview = reviewRepository.findAllByReservation_Owner(currentUser);

        List<String> serializableList = new ArrayList<>();
        for (Review review : listReview) {
            serializableList.add(Review.serializeReview(review));
        }
        return ResponseEntity.ok(serializableList);
    }




//    methods for testing

    @PostMapping("/insertUser")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = (List<User>) userRepository.findAll();
        return !users.isEmpty() ? ResponseEntity.ok(users) : ResponseEntity.notFound().build();
    }

    @PostMapping("/insertReservation")
    public ResponseEntity<Reservation> saveReservation(@RequestBody Reservation reservation){
        Reservation savedReservation = reservationRepository.save(reservation);
        return ResponseEntity.ok(savedReservation);
    }

    @GetMapping("/getAllReservations")
    public ResponseEntity<List<Reservation>> getAllReservations(){
        List<Reservation> reservations = (List<Reservation>) reservationRepository.findAll();
        return !reservations.isEmpty() ? ResponseEntity.ok(reservations) : ResponseEntity.notFound().build();
    }

}
