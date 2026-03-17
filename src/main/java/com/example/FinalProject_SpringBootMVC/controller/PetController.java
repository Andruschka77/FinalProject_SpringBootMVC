package com.example.FinalProject_SpringBootMVC.controller;

import com.example.FinalProject_SpringBootMVC.model.Pet;
import com.example.FinalProject_SpringBootMVC.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(
            @RequestBody Pet pet
    ) {
        var createdPet = petService.createPet(pet);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> findPetById(
            @PathVariable("id") Long petId
    ) {
        var pet = petService.findPetById(petId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(
            @PathVariable("id") Long petId,
            @RequestBody Pet pet
    ) {
        var updatePet = petService.updatePet(petId, pet);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatePet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(
            @PathVariable("id") Long petId
    ) {
        petService.deletePet(petId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
