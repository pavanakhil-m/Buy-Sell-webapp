package com.labweek.menumate.config;

import com.labweek.menumate.dto.ErrorDto;
import com.labweek.menumate.exceptions.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {AppException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(AppException ex){

        return ResponseEntity.status(ex.getStatusCode())
                .body(ErrorDto.builder().message(ex.getMessage()).build());

    }
}
