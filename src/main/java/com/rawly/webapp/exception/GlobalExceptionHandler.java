package com.rawly.webapp.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> resourceNotFoundException(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                ErrorResponse error = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                "Resource not found",
                                ex.getMessage(),
                                request.getRequestURI(),
                                UUID.randomUUID().toString());
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors().forEach(error -> {
                        errors.put(error.getField(), error.getDefaultMessage());
                });

                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation failed",
                                errors,
                                request.getRequestURI(),
                                UUID.randomUUID().toString());

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                        HttpServletRequest request) {
                Throwable cause = ex.getCause();

                ErrorResponse errorResponse;

                if (cause instanceof InvalidFormatException) {
                        InvalidFormatException ife = (InvalidFormatException) cause;
                        String targetType = ife.getTargetType().getSimpleName();
                        String invalidValue = String.valueOf(ife.getValue());
                        String message = "Invalid value '" + invalidValue + "' for type " +
                                        targetType + ". Please provide a valid value.";

                        errorResponse = new ErrorResponse(
                                        LocalDateTime.now(),
                                        HttpStatus.BAD_REQUEST.value(),
                                        "Invalid value",
                                        message,
                                        request.getRequestURI(),
                                        UUID.randomUUID().toString());
                } else {
                        errorResponse = new ErrorResponse(
                                        LocalDateTime.now(),
                                        HttpStatus.BAD_REQUEST.value(),
                                        "Malformed JSON request",
                                        ex.getMessage(),
                                        request.getRequestURI(),
                                        UUID.randomUUID().toString());
                }

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(DuplicateFieldException.class)
        public ResponseEntity<ErrorResponse> handleDuplicateFieldsException(DuplicateFieldException ex,
                        HttpServletRequest request) {
                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.CONFLICT.value(),
                                "Duplicate Fields Error",
                                "The following fields are already in use: " + String.join(", ", ex.getFields())
                                                + ". Please provide unique values.",
                                request.getRequestURI(),
                                UUID.randomUUID().toString());

                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);

        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
                String errorId = UUID.randomUUID().toString();
                MDC.put("errorId", errorId);
                log.error("ErrorId {}: {}", errorId, ex.getMessage(), ex);
                MDC.clear();
                ErrorResponse error = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                ex.getMessage(),
                                request.getRequestURI(),
                                errorId);
                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
