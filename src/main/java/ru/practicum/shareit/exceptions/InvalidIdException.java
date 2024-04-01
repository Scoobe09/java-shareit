package ru.practicum.shareit.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidIdException extends RuntimeException{
    private final HttpStatus httpStatus;

    public InvalidIdException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
