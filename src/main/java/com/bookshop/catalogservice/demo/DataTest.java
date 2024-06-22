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
        var listPhotos = List.of(
                "http://res.cloudinary.com/dl0v8gbku/image/upload/v1718951746/b32c6f28-36d5-4c46-9f61-d488ef59027f",
                "http://res.cloudinary.com/dl0v8gbku/image/upload/v1718951748/22809e64-778d-4124-a0eb-dd45db254092",
                "http://res.cloudinary.com/dl0v8gbku/image/upload/v1718951750/d4a94418-f65a-40fc-9099-66e860c74866"
        );
        // fake data
        var book1 = Book.of("1234567891", "Title 1", "Author 1",
                "Publisher 1", "Supplier 1", 19.02, 3, Language.ENGLISH,
                CoverType.PAPERBACK, 25, new Measure(120, 180, 10, 200));
        book1.setPhotos(listPhotos);
        var book2 = Book.of("1234567892", "Title 2", "Author 2",
                "Publisher 2", "Supplier 2", 19.02, 30, Language.ENGLISH,
                CoverType.PAPERBACK, 25, new Measure(120, 180, 10, 200));
        book2.setPhotos(listPhotos);
        var book3 = Book.of("1234567893", "Title 3", "Author 3",
                "Publisher 3", "Supplier 3", 19.02, 100, Language.ENGLISH,
                CoverType.PAPERBACK, 25, new Measure(120, 180, 10, 200));
        book3.setPhotos(listPhotos);
        List<Book> list = List.of(book1, book2, book3);
        bookRepository.saveAll(list);
    }

}
