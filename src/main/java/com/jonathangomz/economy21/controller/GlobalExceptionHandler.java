package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.exceptions.ResourceNotFound;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Data
    static public class ErrorResponse {
        private String message;
        private LocalDateTime timestamp;
        private int status;

        public ErrorResponse(String message, int status) {
            this.message = message;
            this.timestamp = LocalDateTime.now();
            this.status = status;
        }
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFound e) {
        var errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
