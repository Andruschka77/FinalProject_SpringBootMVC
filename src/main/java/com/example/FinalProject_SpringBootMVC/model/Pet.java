package com.example.FinalProject_SpringBootMVC.model;

public class Pet {
    private Long id;
    private final String name;
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
