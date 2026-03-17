package com.example.FinalProject_SpringBootMVC.service;

import com.example.FinalProject_SpringBootMVC.model.Pet;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class PetService {

    private Long idCounter = 0L;
    private final Map<Long, Pet> pets = new HashMap<>();
    private final UserService userService;

    public PetService(UserService userService) {
        this.userService = userService;
    }

    public Pet createPet(Pet pet) {
        var newPet = new Pet(
                ++idCounter,
                pet.getName(),
                pet.getUserId()
        );
        pets.put(newPet.getId(), newPet);
        var user = userService.findUserById(pet.getUserId());
        user.setPets(newPet);
        return newPet;
    }

    public Pet findPetById(Long petId) {
        return pets.get(petId);
    }

    public Pet updatePet(Long petId, Pet pet) {
        var newPet = new Pet(
                petId,
                pet.getName(),
                pet.getUserId()
        );
        pets.put(petId, newPet);

        var user = userService.findUserById(pet.getUserId());
        user.getPets().removeIf(p -> p.getId().equals(petId));
        user.setPets(newPet);
        return newPet;
    }

    public void deletePet(Long petId) {
        var pet = findPetById(petId);
        var user = userService.findUserById(pet.getUserId());
        user.getPets().removeIf(p -> p.getId().equals(petId));
        pets.remove(petId);
    }
}
