package com.basyir.security.exceptions;

public class MissingJWTException extends RuntimeException {
    public MissingJWTException(String message) {
        super(message);
    }
}
