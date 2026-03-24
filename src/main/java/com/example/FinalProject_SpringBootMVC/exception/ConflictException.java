package com.example.FinalProject_SpringBootMVC.exception;

public class ConflictException extends RuntimeException {
    private final String errorCode;

    public ConflictException(String message) {
        super(message);
        this.errorCode = "CONFLICT";
    }

    public String getErrorCode() {
        return errorCode;
    }
}