package com.example.FinalProject_SpringBootMVC.model;

import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

public class User {

    private Long id;

    @NotBlank
    @Size(min=3, max=20)
    private final String name;

    @NotBlank
    @Email
    private final String email;

    @NotNull
    @Max(100)
    private final Integer age;

    private final List<Pet> pets = new ArrayList<>();

    public User(
            Long id,
            String name,
            String email,
            Integer age
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(Pet pet) {
        this.pets.add(pet);
    }

}