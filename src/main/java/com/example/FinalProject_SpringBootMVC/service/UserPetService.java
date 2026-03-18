package com.example.FinalProject_SpringBootMVC.service;

import com.example.FinalProject_SpringBootMVC.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPetService {

    private final UserService userService;
    private final PetService petService;

    public UserPetService(
            UserService userService, PetService petService
    ) {
        this.userService = userService;
        this.petService = petService;
    }

    public void deleteUserWithPets(Long userId) {
        var deletedUser = userService.findDeletedUser(userId);
        if (deletedUser == null) {
            throw new ResourceNotFoundException("User", userId);
        }
        petService.deleteAllPetsUsers(deletedUser);
    }

}