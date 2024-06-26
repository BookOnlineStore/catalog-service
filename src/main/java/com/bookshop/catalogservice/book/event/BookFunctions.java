package com.bookshop.catalogservice.book.event;

import com.bookshop.catalogservice.book.BookRepository;
import com.bookshop.catalogservice.book.exception.BookNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class BookFunctions {

    private static final Logger log = LoggerFactory.getLogger(BookFunctions.class);

    @Bean
    public Consumer<BooksPlacedMessage> reduceInventory(BookRepository bookRepository) {
        return booksPlacedMessage -> {
            log.info("Received message: {}", booksPlacedMessage.toString());

            booksPlacedMessage.lineItems().forEach((isbn, quantity) -> {
                bookRepository.findByIsbn(isbn).map(book -> {
                    var bookUpdate = book;
                    bookUpdate.setInventory(book.getInventory() - quantity);
                    bookUpdate.setPurchases(book.getPurchases() + quantity);
                    return bookRepository.save(bookUpdate);
                }).orElseThrow(() -> new BookNotFoundException(isbn));
            });
        };
    }

}
