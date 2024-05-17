package com.bookshop.catalogservice.domain;

import com.bookshop.catalogservice.config.DataConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.stream.StreamSupport;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class BookRepositoryTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findAllBook() {
        var book1 = Book.of("1234567890", "Title 1", "Author 1", 9.90);
        var book2 = Book.of("1234567891", "Title 2", "Author 2", 9.90);
        jdbcAggregateTemplate.save(book1);
        jdbcAggregateTemplate.save(book2);

        Iterable<Book> actualBooks = bookRepository.findAll();
        Assertions.assertThat(StreamSupport
                .stream(actualBooks.spliterator(), false)
                .count()
        ).isEqualTo(2);
    }

    @Test
    void findBookByIsbn() {
        var book1 = Book.of("1234567890", "Title 1", "Author 1", 9.90);
        var book2 = Book.of("1234567891", "Title 2", "Author 2", 9.90);
        jdbcAggregateTemplate.save(book1);
        jdbcAggregateTemplate.save(book2);

        Optional<Book> actualBook = bookRepository.findByIsbn(book1.isbn());
        Assertions.assertThat(actualBook.isPresent()).isEqualTo(true);
        Assertions.assertThat(actualBook.get().author()).isEqualTo("Author 1");
        Assertions.assertThat(actualBook.get().createdAt()).isNotNull();
    }

    @Test
    void deleteBookByIsbn() {
        var book1 = Book.of("1234567890", "Title 1", "Author 1", 9.90);
        var book2 = Book.of("1234567891", "Title 2", "Author 2", 9.90);
        jdbcAggregateTemplate.save(book1);
        jdbcAggregateTemplate.save(book2);

        bookRepository.deleteByIsbn(book1.isbn());
        Iterable<Book> actualBooks = bookRepository.findAll();
        Assertions.assertThat(StreamSupport
                .stream(actualBooks.spliterator(), false)
                .count()
        ).isEqualTo(1);
    }

}
