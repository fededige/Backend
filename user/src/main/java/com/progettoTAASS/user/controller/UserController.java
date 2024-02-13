package com.progettoTAASS.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.progettoTAASS.user.model.*;
import com.progettoTAASS.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
//@CrossOrigin
@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSender userSender;

//    @Value("${review_url}")
//    private String review_url;

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
        User checkExistingUser = userRepository.findUserByUsername(newUser.getUsername());
        User u;
        if(checkExistingUser == null){
            System.out.println(newUser);
            newUser.setCoins(5);
            u = userRepository.save(newUser);
            userSender.sendNewUser(u);
        }
        else {
            u = checkExistingUser;
        }

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

    @GetMapping("/getCoins")
    public ResponseEntity<Integer> getUserCoins(@RequestParam String username) {
        User currentUser = userRepository.findUserByUsername(username);
        if(currentUser == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(currentUser.getCoins());
    }

    // Potrebbe essere gestito da un microservizio a parte
    @PostMapping("/addCoins")
    public ResponseEntity<User> addUserCoins(@RequestParam String username, @RequestBody int coins) {
        User currentUser = userRepository.findUserByUsername(username);
        if(currentUser == null)
            return ResponseEntity.notFound().build();

        currentUser.setCoins(currentUser.getCoins() + coins);
        User savedUser = userRepository.save(currentUser);
        userSender.sendNewUser(savedUser);

        return ResponseEntity.ok(savedUser);
    }


//TODO: http request between user and review
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode rootNode = objectMapper.createObjectNode();
//        rootNode.put("username", u.getUsername());
//        rootNode.put("email", u.getEmail());
//        String userJson;
//        try {
//            userJson = objectMapper.writeValueAsString(rootNode);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<User> request = null;
//        ResponseEntity<User> response = null;
//        try{
//            request = new HttpEntity<>(u, headers);
//            response = restTemplate.postForEntity("http://" + review_url + "/review/insertUser", request, User.class);
//            System.out.println(" [x] Sent '" + rootNode + "'");
//        } catch (Exception e){
//            System.out.println("http://" + review_url + "/review/insertUser");
//            System.out.println(request);
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }

}
