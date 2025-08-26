package com.pjqe.app.exception;

public class InsufficientMinutesException extends RuntimeException {
    public InsufficientMinutesException(String message) {
        super(message);
    }
}

