package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<ErrorResponse> notFound(InvalidIdException exp) {
        log.error("Ваш параметр не найден. {}", exp.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(exp.getMessage())
                .build(), exp.getHttpStatus());
    }

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<ErrorResponse> onValidateException(ValidateException exp) {
        log.error("Неверные данные. {}", exp.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(exp.getMessage())
                .build(), exp.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> onConstraintViolationException(ConstraintViolationException exp) {
        log.error("Невалидные данные. {}", exp.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(exp.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
}

