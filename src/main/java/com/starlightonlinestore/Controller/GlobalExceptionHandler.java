package com.starlightonlinestore.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception e){
        e.printStackTrace();
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
