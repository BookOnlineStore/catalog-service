package com.bookshop.catalogservice.domain;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String isbn) {
        super("Book with isbn " + isbn + " not found.");
    }
}
