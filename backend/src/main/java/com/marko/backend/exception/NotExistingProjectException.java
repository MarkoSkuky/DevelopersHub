package com.marko.backend.exception;

public class NotExistingProjectException extends RuntimeException {
    public NotExistingProjectException(Long id) {
        super("Project with id: " + id + " does not exist");
    }
}
