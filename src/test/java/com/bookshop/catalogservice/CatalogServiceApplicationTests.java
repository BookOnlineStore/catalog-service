package com.bookshop.catalogservice;

import com.bookshop.catalogservice.domain.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Testcontainers
class CatalogServiceApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void whenGetRequestWithIdThenBookReturned() {
        var book = Book.of("1234567890", "Title", "Author", 19.0);
        webTestClient
                .post().uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated();

        Book actualBook = webTestClient
                .get().uri("/books/{isbn}", book.isbn())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(actualBook.isbn()).isEqualTo(book.isbn());
        Assertions.assertThat(actualBook.id()).isNotNull();
    }

    @Test
    void whenPostRequestThenBookCreated() {
        var book = Book.of("1234567891", "Title", "Author", 19.0);
        webTestClient
                .post().uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .value(actualBook -> Objects.requireNonNull(actualBook.id()));
    }

    @Test
    void whenPutRequestThenBookUpdated() {
        String isbn = "1234567892";
        var book = Book.of(isbn, "Title", "Author", 19.0);
        webTestClient
                .post().uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .value(actualBook -> Objects.requireNonNull(actualBook.id()));

        var bookUpdate = Book.of(isbn, "Another title",
                "Another author", 19.0);
        webTestClient
                .put().uri("/books/{isbn}", isbn)
                .bodyValue(bookUpdate)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Book.class)
                .value(updatedBook -> Objects.equals(updatedBook.author(), "Another author"));
    }

    @Test
    void whenDeleteRequestThenDeleted() {
        String isbn = "1234567893";
        var book = Book.of(isbn, "Title", "Author", 19.0);
        webTestClient
                .post().uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .value(actualBook -> Objects.requireNonNull(actualBook.id()));

        webTestClient
                .delete().uri("/books/{isbn}", isbn)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get().uri("/books/{isbn}", isbn)
                .exchange()
                .expectStatus().isNotFound();
    }

}
