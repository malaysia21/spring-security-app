package com.aga.security.app.controller;

import com.aga.security.app.model.UserEntity;
import com.aga.security.app.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("api")
@CrossOrigin
public class ApiRestController {

    private final UserRepository userRepository;


    public ApiRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //available for all authenticated users
    @GetMapping("/advertisement")
    public ResponseEntity<String> getAdvertisement() {
        return ResponseEntity.status(HttpStatus.OK).body("Advertisement 1");
    }

    //available for manager and admin
    @GetMapping("/advertisements")
    public ResponseEntity<List<String>> getAdvertisements() {
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList("Advertisement 1", "Advertisement 2"));
    }

    //available for admin
    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }
}
