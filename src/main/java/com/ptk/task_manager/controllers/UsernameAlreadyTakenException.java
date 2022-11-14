package com.ptk.task_manager.controllers;

// todo: create proper package for exceptions
public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException(String message) {
        super(message);
    }
}
