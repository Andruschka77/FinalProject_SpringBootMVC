package com.example.FinalProject_SpringBootMVC.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String errorCode;

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s с ID %d не найден", resourceName, id));
        this.errorCode = "RESOURCE_NOT_FOUND";
    }

    public String getErrorCode() {
        return errorCode;
    }
}