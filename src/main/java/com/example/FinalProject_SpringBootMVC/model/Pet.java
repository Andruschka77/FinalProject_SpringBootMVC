package com.example.FinalProject_SpringBootMVC.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Pet {

    private Long id;

    @NotBlank
    @Size(min=3, max=20)
    private final String name;

    @NotNull
    private final Long userId;

    public Pet(
            Long id,
            String name,
            Long userId
    ) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }

}