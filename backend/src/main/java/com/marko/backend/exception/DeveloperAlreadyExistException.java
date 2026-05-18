package com.marko.backend.exception;

public class DeveloperAlreadyExistException extends RuntimeException {
    public DeveloperAlreadyExistException(String email) {
        super("Developer with email adress: " + email + " already exist");
    }
}
