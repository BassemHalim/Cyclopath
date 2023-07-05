package com.bassemHalim.Expections;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = HttpMessageConversionException.class)
    public ResponseEntity<APIError> handleException(HttpMessageConversionException e,
                                                    HttpServletRequest request) {

        APIError error = new APIError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIError> handleException(ResourceNotFoundException e,
                                                    HttpServletRequest request) {

        APIError error = new APIError(
                request.getRequestURI(),
                e.getErrorMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<APIError> handleException(SecurityException e,
                                                    HttpServletRequest request) {

        APIError error = new APIError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<APIError> handleException(Exception e,
                                                    HttpServletRequest request) {
        APIError error = new APIError(
                request.getRequestURI(),
                "internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
