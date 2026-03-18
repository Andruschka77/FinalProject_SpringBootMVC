package com.example.FinalProject_SpringBootMVC.exception;

public class ValidationException extends RuntimeException {
    private final String errorCode;

    public ValidationException(String message) {
        super(message);
        this.errorCode = "VALIDATION_ERROR";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
