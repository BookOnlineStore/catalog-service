package com.bookshop.catalogservice.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void whenBookToReadAllAreadyExistsThenReturn() {
        String isbn = "1234567890";
        var book = Book.of(isbn, "Title", "Author", 9.90);
        Mockito.when(bookRepository.findAll())
                .thenReturn(List.of(book, book, book));

        Assertions.assertThat(bookService.getAllBook())
                .isEqualTo(List.of(book, book, book));
    }

    @Test
    void whenBookToReadByIdDoesNotExistsThenThrow() {
        String isbn = "1234567890";
        Mockito.when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> bookService.getBookByIsbn(isbn))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book with isbn " + isbn + " not found.");
    }

    @Test
    void whenBookToReadByIdFoundThenReturn() {
        String isbn = "1234567890";
        var book = Book.of(isbn, "Title", "Author", 9.90);
        Mockito.when(bookRepository.findByIsbn(isbn))
                .thenReturn(Optional.of(book));

        Assertions.assertThat(bookService.getBookByIsbn(isbn).isbn())
                .isEqualTo(isbn);
    }

    @Test
    void whenBookToCreateAlreadyExistsThenThrows() {
        String isbn = "1234567890";
        var book = Book.of(isbn, "Title", "Author", 9.90);
        Mockito.when(bookRepository.existsByIsbn(isbn)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> bookService.addBookToCatalog(book))
                .isInstanceOf(BookAlreadyExistsException.class)
                .hasMessage("Book with isbn " + isbn + " already exists.");
    }

    @Test
    void whenBookToCreateDoesNotExistsThenCreated() {
        String isbn = "1234567890";
        var book = Book.of(isbn, "Title", "Author", 9.90);
        Mockito.when(bookRepository.existsByIsbn(isbn)).thenReturn(false);
        Mockito.when(bookRepository.save(book)).thenReturn(book);

        Assertions.assertThat(bookService.addBookToCatalog(book).isbn())
                .isEqualTo(isbn);
    }

    @Test
    void whenBookToUpdateAlreadyExistsThenUpdate() {
        String isbn = "1234567890";
        Instant now = Instant.now();
        var bookPersisted = new Book(100l, isbn,
                "Title",
                "Author",
                9.90, now,
                "unknown", now,
                "unknown",
                0);
        var bookNeedToUpdate = new Book(bookPersisted.id(), bookPersisted.isbn(),
                "Title updated",
                "Author updated",
                19.90,
                bookPersisted.createdAt(),
                bookPersisted.createdBy(),
                bookPersisted.lastModifiedAt(),
                bookPersisted.lastModifiedBy(),
                bookPersisted.version());
        // mock value when find by isbn
        Mockito.when(bookRepository.findByIsbn(isbn))
                .thenReturn(Optional.of(bookPersisted));
        // mock value when update
        Mockito.when(bookRepository.save(bookNeedToUpdate))
                .thenReturn(bookNeedToUpdate);

        Assertions.assertThat(bookService.editBookDetail(isbn, bookNeedToUpdate).isbn())
                .isEqualTo(isbn);
    }

    @Test
    void whenBookToUpdateDoesNotExistsThenThrow() {
        String isbn = "1234567890";
        Mockito.when(bookRepository.findByIsbn(isbn))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> bookService.editBookDetail(isbn, null))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book with isbn " + isbn + " not found.");
    }

}
