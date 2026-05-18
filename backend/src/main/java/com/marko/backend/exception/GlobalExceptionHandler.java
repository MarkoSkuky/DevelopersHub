package com.marko.backend.exception;

import com.marko.backend.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotExistingDeveloper.class)
    public ResponseEntity<ErrorResponse> handleNotExistingDeveloper(NotExistingDeveloper e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());

        return ResponseEntity
            .status(404)
            .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(error -> error.getField() + " " + error.getDefaultMessage())
            .orElse("Validation failed");

        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse(message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleDeveloperAlreadyExistException(HttpMessageNotReadableException e) {
        ErrorResponse response = new ErrorResponse("Invalid seniority value");

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DeveloperAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleDeveloperAlreadyExistException(DeveloperAlreadyExistException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

}