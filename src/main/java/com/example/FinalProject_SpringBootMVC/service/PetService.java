package com.example.FinalProject_SpringBootMVC.service;

import com.example.FinalProject_SpringBootMVC.exception.ResourceNotFoundException;
import com.example.FinalProject_SpringBootMVC.exception.ValidationException;
import com.example.FinalProject_SpringBootMVC.model.Pet;
import com.example.FinalProject_SpringBootMVC.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PetService {

    private Long idCounter = 0L;
    private final Map<Long, Pet> pets = new HashMap<>();
    private final UserService userService;

    public PetService(UserService userService) {
        this.userService = userService;
    }

    public Pet createPet(Pet pet) {
        if (pet.getUserId() < 0) {
            throw new ValidationException("Id питомца не может быть отрицательным!");
        }
        User user = userService.findUserById(pet.getUserId());
        var newPet = new Pet(
                ++idCounter,
                pet.getName(),
                pet.getUserId()
        );
        pets.put(newPet.getId(), newPet);
        user.setPets(newPet);
        return newPet;
    }

    public Pet findPetById(Long petId) {
        return Optional.ofNullable(pets.get(petId))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pet", petId)
                );
    }

    public Pet updatePet(Long petId, Pet pet) {
        if (pet.getUserId() < 0) {
            throw new ValidationException("Id пользователя не может быть отрицательным!");
        }
        var newPet = new Pet(
                petId,
                pet.getName(),
                pet.getUserId()
        );
        pets.put(petId, newPet);

        User user = userService.findUserById(pet.getUserId());
        user.getPets().removeIf(p -> p.getId().equals(petId));
        user.setPets(newPet);
        return newPet;
    }

    public void deletePet(Long petId) {
        Pet pet = pets.remove(petId);
        if (pet == null) {
            throw new ResourceNotFoundException("Pet", petId);
        }

        User user = userService.findUserById(pet.getUserId());
        user.getPets().removeIf(p -> p.getId().equals(petId));
    }

    public void deleteAllPetsUsers(User deletedUser) {
        pets.values().stream()
                .filter(pet -> pet.getUserId() == deletedUser.getId())
                .map(Pet::getId)
                .toList()
                .forEach(pets::remove);
    }
}