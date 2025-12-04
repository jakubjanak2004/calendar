package com.example.demo.exception;

import com.example.demo.dto.response.ErrorMessageDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorMessageDTO> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessageDTO> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorMessageDTO> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessageDTO> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
