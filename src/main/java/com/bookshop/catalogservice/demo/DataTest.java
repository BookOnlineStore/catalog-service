package com.bookshop.catalogservice.demo;

import com.bookshop.catalogservice.book.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("datatest")
public class DataTest {

    private final BookRepository bookRepository;

    public DataTest(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setupDataTest() {
        bookRepository.deleteAll();
        // fake data
        var book1 = Book.of("1234567891", "Title 1", "Author 1",
                "Publisher 1", "Supplier 1", 19.02, Language.ENGLISH,
                CoverType.PAPERBACK, 25, new Measure(120, 180, 10, 200));
        var book2 = Book.of("1234567892", "Title 2", "Author 2",
                "Publisher 2", "Supplier 2", 19.02, Language.ENGLISH,
                CoverType.PAPERBACK, 25, new Measure(120, 180, 10, 200));
        var book3 = Book.of("1234567893", "Title 3", "Author 3",
                "Publisher 3", "Supplier 3", 19.02, Language.ENGLISH,
                CoverType.PAPERBACK, 25, new Measure(120, 180, 10, 200));
        List<Book> list = List.of(book1, book2, book3);
        bookRepository.saveAll(list);
    }

}
