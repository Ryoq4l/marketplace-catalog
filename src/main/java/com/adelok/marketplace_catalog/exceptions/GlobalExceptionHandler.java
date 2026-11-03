package com.adelok.marketplace_catalog.exceptions;


import com.adelok.marketplace_catalog.dto.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import com.adelok.marketplace_catalog.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice

public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handle(ResponseStatusException exception, HttpServletRequest request) {
        log.warn("Http error: {}", exception.getReason());
        ErrorCode code = switch (exception.getStatusCode().value()) {
            case 404 -> ErrorCode.NOT_FOUND;
            case 409 -> ErrorCode.BAD_REQUEST;
            case 403 -> ErrorCode.FORBIDDEN;
            default -> ErrorCode.INTERNAL_ERROR;
        };
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                code,
                HttpStatus.valueOf(exception.getStatusCode().value()),
                exception.getReason(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(exception.getStatusCode()).body(error);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception, HttpServletRequest request){
        log.warn("Argument not valid");
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.BAD_REQUEST,
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception exception, HttpServletRequest request) {
        log.error("UNEXPECTED ERROR AT {}", request.getRequestURI(), exception);
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.INTERNAL_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "UNEXPECTED ERROR",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
