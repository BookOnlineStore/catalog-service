package com.bookshop.catalogservice.demo;

import com.bookshop.catalogservice.domain.Book;
import com.bookshop.catalogservice.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("datatest")
@RequiredArgsConstructor
public class DataTest {

    private final BookRepository bookRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void setupDataTest() {
        bookRepository.deleteAll();

        var book1 = Book.of("1234567891", "Title 1", "Author 1", 19.90);
        var book2 = Book.of("1234567892", "Title 2", "Author 2", 19.90);
        var book3 = Book.of("1234567893", "Title 3", "Author 3", 19.90);
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }

}
