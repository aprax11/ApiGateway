package com.example.ApiGateway.exceptions;

public class ErrorResponseException extends RuntimeException {

    public ErrorResponseException(String message) {
        super(message);
    }
}
