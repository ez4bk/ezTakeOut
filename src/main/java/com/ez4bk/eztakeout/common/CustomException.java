package com.ez4bk.eztakeout.common;

/**
 * Custom exception for business logic
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
