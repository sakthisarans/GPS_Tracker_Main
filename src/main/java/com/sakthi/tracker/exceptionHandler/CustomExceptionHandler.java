package com.sakthi.tracker.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AdminNotFound.class)
    public ResponseEntity<?> adminNotFound(){
        return new ResponseEntity<>("",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
