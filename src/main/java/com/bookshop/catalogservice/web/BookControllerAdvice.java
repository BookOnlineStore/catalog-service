package com.bookshop.catalogservice.web;

import com.bookshop.catalogservice.domain.BookAlreadyExistsException;
import com.bookshop.catalogservice.domain.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class BookControllerAdvice {

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String bookNotFoundHandler(BookNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String bookAlreadyExistsHandler(BookAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponse> constraintsViolationHandler(MethodArgumentNotValidException ex) {
        var errors = new ArrayList<ErrorResponse>();
        ex.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldError = ((FieldError) objectError).getField();
            String msg = objectError.getDefaultMessage();
            errors.add(new ErrorResponse(fieldError, msg));
        });
        return errors;
    }

}
