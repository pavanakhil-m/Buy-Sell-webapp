package com.labweek.menumate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException{
    private final HttpStatus statusCode;
    public AppException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
