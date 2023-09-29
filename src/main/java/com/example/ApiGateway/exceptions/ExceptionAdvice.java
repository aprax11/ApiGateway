package com.example.ApiGateway.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(ErrorResponseException.class)
    @ResponseStatus(value = BAD_REQUEST, reason = "couldn't process request")
    public ResponseEntity<String> handleErrorResponseException(final ErrorResponseException exception) {

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(exception.getMessage());
    }
}
