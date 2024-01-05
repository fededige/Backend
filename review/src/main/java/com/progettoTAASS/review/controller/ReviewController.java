package com.progettoTAASS.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoTAASS.review.model.Reservation;
import com.progettoTAASS.review.model.Review;
import com.progettoTAASS.review.model.User;
import com.progettoTAASS.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping(value = "/writeReview", consumes = "application/json")
    public ResponseEntity<String> writeReview(@RequestBody JsonNode requestBody){
        System.out.println("requestBody: " + requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        String title = requestBody.get("title").textValue();
        int evaluation =  requestBody.get("evaluation").intValue();
        String text = requestBody.get("text").textValue();
        Date datePublished;
        User writer;
        Reservation reservation;

        try {
            datePublished = objectMapper.readValue(requestBody.get("datePublished").toString(), Date.class);
            writer = objectMapper.readValue(requestBody.get("writer").toString(), User.class);
            reservation = objectMapper.readValue(requestBody.get("reservation").toString(), Reservation.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Review newReview = new Review();
        newReview.setTitle(title);
        newReview.setEvaluation(evaluation);
        newReview.setText(text);
        newReview.setDatePublished(datePublished);
        newReview.setWriter(writer);
        newReview.setReservation(reservation);

        Review reviewSaved = reviewRepository.save(newReview);

        return ResponseEntity.ok(Review.serializeReview(reviewSaved));
    }


    @PostMapping(value = "/deleteReview", consumes = "application/json")
    public ResponseEntity<String> deleteReview(@RequestBody JsonNode requestBody){
        //TODO
        return ResponseEntity.ok("todo");
    }

}
