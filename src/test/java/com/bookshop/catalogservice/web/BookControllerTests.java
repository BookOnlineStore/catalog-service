package com.bookshop.catalogservice.web;

import com.bookshop.catalogservice.domain.Book;
import com.bookshop.catalogservice.domain.BookAlreadyExistsException;
import com.bookshop.catalogservice.domain.BookNotFoundException;
import com.bookshop.catalogservice.domain.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(BookController.class)
public class BookControllerTests {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    BookService bookService;

    @Test
    void whenGetAllBookThenReturn200() {
        BDDMockito.when(bookService.getAllBook())
                .thenReturn(buildBookObject(5));

        webTestClient
                .get().uri("/books")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Book.class)
                .hasSize(5);
    }

    @Test
    void whenBookToGetByIsbnAvailableThenReturn200() {
        var book = Book.of("1234567890", "Title", "Author", 19.90);
        BDDMockito.when(bookService.getBookByIsbn(book.isbn()))
                .thenReturn(book);

        Book actualBook = webTestClient
                .get().uri("/books/{isbn}", book.isbn())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Book.class).returnResult().getResponseBody();
        Assertions.assertThat(actualBook).isEqualTo(actualBook);
    }

    @Test
    void whenBookToGetByIsbnUnAvailableThenReturn404() {
        String isbn = "0987654321";
        BDDMockito.when(bookService.getBookByIsbn(isbn))
                .thenThrow(new BookNotFoundException(isbn));

        webTestClient
                .get().uri("/books/{isbn}", isbn)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenBookToCreateValidThenCreated() {
        var book = Book.of("1029384756", "Title", "Author", 1.2);
        BDDMockito.when(bookService.addBookToCatalog(book))
                .thenReturn(book);

        webTestClient
                .post().uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void whenBookToCreateAlreadyExistsThenUnprocessableEntity() {
        var book = Book.of("1029384756", "Title", "Author", 1.2);
        BDDMockito.when(bookService.addBookToCatalog(book))
                .thenThrow(new BookAlreadyExistsException(book.isbn()));

        webTestClient
                .post().uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void whenBookToCreateInValidThenBadRequest() {
        var book = Book.of("1029384756", "Title", "Author", 0);  // price must greater than zero

        webTestClient
                .post().uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenBookToUpdateDoesNotExistsThenNotFound() {
        var book = Book.of("1029384756", "Title", "Author", 1.2);
        BDDMockito.when(bookService.editBookDetail(book.isbn(), book))
                .thenThrow(new BookNotFoundException(book.isbn()));

        webTestClient
                .put().uri("/books/{isbn}", book.isbn())
                .bodyValue(book)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenBookToUpdateOkThenUpdate() {
        var book = Book.of("1029384756", "Title", "Author", 1.2);
        BDDMockito.when(bookService.editBookDetail(book.isbn(), book))
                .thenReturn(book);

        Book actualBook = webTestClient
                .put().uri("/books/{isbn}", book.isbn())
                .bodyValue(book)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class).returnResult().getResponseBody();
        Assertions.assertThat(actualBook).isEqualTo(book);
    }

    @Test
    void whenBookToUpdateInvalidThenBadRequest() {
        var book = Book.of("123456789", "Title", "Author", 1.2);
        webTestClient
                .put().uri("/books/{isbn}", book.isbn())
                .bodyValue(book)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenBookToDeleteOkThenNoContent() {
        var book = Book.of("123456789", "Title", "Author", 1.2);
        webTestClient
                .delete().uri("/books/{isbn}", book.isbn())
                .exchange()
                .expectStatus().isNoContent();
    }

    private static List<Book> buildBookObject(int count) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String isbn = String.format("%d", 1000000000 + i);
            var book = new Book((long) i, isbn,
                    "Title " + i,
                    "Author " + i,
                    0.1, Instant.now(), "unknown",
                    Instant.now(), "unknown", 0);
            books.add(book);
        }
        return books;
    }

}
