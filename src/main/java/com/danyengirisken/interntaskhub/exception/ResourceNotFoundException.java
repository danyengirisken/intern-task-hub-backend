package com.danyengirisken.interntaskhub.exception;

/**
 * Aranan kaydın bulunamadığı durumlarda fırlatılır (HTTP 404).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
