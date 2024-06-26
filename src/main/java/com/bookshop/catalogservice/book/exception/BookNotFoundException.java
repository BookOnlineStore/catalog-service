package com.bookshop.catalogservice.book.exception;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String isbn) {
        super("Book not found!");
    }
}
