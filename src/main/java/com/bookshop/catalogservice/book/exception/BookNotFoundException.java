package com.bookshop.catalogservice.book.exception;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException() {
        super("Book not found!");
    }
}
