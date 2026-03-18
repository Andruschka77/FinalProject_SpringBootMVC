package com.example.FinalProject_SpringBootMVC.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        var error = new ErrorMessageResponse(ex.getMessage(), ex.getErrorCode());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorMessageResponse> handleConflict(ConflictException ex) {
        var error = new ErrorMessageResponse(ex.getMessage(), ex.getErrorCode());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidation(ValidationException ex) {
        var error = new ErrorMessageResponse(ex.getMessage(), ex.getErrorCode());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ?
                                fieldError.getDefaultMessage() : "Invalid value"
                ));

        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "Ошибка валидации данных",
                "VALIDATION_FAILED"
        );
        errorResponse.setDetails(fieldErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        var error = new ErrorMessageResponse(
                "Некорректный JSON в теле запроса",
                "INVALID_JSON"
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorMessageResponse> handleAllException(Exception ex) {
//        var error = new ErrorMessageResponse(
//                "Внутренняя ошибка сервера",
//                "INTERNAL_ERROR"
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(error);
//    }

}