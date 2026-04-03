package com.pokeapp.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.pokeapp.application.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFound(
                        ResourceNotFoundException ex,
                        HttpServletRequest request) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ErrorResponse.of(
                                                HttpStatus.NOT_FOUND.value(),
                                                "Not Found",
                                                ex.getMessage(),
                                                request.getRequestURI()));
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorResponse> handleTypeMismatch(
                        MethodArgumentTypeMismatchException ex,
                        HttpServletRequest request) {

                String message = String.format(
                                "Invalid value '%s' for parameter '%s'",
                                ex.getValue(), ex.getName());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ErrorResponse.of(
                                                HttpStatus.BAD_REQUEST.value(),
                                                "Bad Request",
                                                message,
                                                request.getRequestURI()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(
                        Exception ex,
                        HttpServletRequest request) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ErrorResponse.of(
                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                "Internal Server Error",
                                                "An unexpected error occurred",
                                                request.getRequestURI()));
        }

        @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentials(
                        org.springframework.security.authentication.BadCredentialsException ex,
                        HttpServletRequest request) {

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(ErrorResponse.of(
                                                HttpStatus.UNAUTHORIZED.value(),
                                                "Unauthorized",
                                                "Invalid username or password",
                                                request.getRequestURI()));
        }

        @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(
                        org.springframework.web.bind.MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                String message = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                                .collect(java.util.stream.Collectors.joining("; "));

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ErrorResponse.of(
                                                HttpStatus.BAD_REQUEST.value(),
                                                "Validation Failed",
                                                message,
                                                request.getRequestURI()));
        }
}