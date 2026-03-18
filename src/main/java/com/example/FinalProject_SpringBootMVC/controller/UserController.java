package com.example.FinalProject_SpringBootMVC.controller;

import com.example.FinalProject_SpringBootMVC.model.User;
import com.example.FinalProject_SpringBootMVC.service.UserPetService;
import com.example.FinalProject_SpringBootMVC.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserPetService userPetService;

    public UserController(
            UserService userService, UserPetService userPetService
    ) {
        this.userService = userService;
        this.userPetService = userPetService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @Valid @RequestBody User user
    ) {
        var createdUser = userService.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(
            @PathVariable("id") Long userId
    ) {
        var user = userService.findUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id") Long userId,
            @Valid @RequestBody User user
    ) {
        var updatedUser = userService.updateUser(userId, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") Long userId
    ) {
        userPetService.deleteUserWithPets(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}