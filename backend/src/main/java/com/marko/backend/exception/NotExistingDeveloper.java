package com.marko.backend.exception;

public class NotExistingDeveloper extends RuntimeException {
    public NotExistingDeveloper(Long id) {
        super("Developer with id: " + id + " doesnt exist");
    }
}
