package com.example.practicespring.global.exception;

import com.example.practicespring.global.exception.customException.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getStatus().toString(),e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }

}
