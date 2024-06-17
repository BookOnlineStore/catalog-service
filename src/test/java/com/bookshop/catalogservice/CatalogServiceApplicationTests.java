package com.bookshop.catalogservice;

import com.bookshop.catalogservice.domain.Book;
import com.bookshop.catalogservice.domain.BookRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Testcontainers
class CatalogServiceApplicationTests {

    private static KeycloakToken employeeToken;
    private static KeycloakToken customerToken;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    WebTestClient webTestClient;

    @Container
    private static final KeycloakContainer keycloakContainer =
            new KeycloakContainer("quay.io/keycloak/keycloak:23.0")
                    .withRealmImportFile("bookstore-realm.json");

    @DynamicPropertySource
    static void keycloakProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realms/bookstore");
    }

    @BeforeAll
    static void generateAccessToken() {
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakContainer.getAuthServerUrl() +
                        "/realms/bookstore/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
        employeeToken = authenticateWith("employee", "1", webClient);
        customerToken = authenticateWith("user", "1", webClient);
    }

    @AfterEach
    public void clear() {
        bookRepository.deleteAll();
    }

    @Test
    void whenGetRequestWithIdThenBookReturned() {
        var book = Book.of("1234567890", "Title", "Author", 19.0);
        webTestClient
                .post().uri("/books")
                .bodyValue(book)
                .headers(headers -> headers.setBearerAuth(employeeToken.accessToken()))
                .exchange()
                .expectStatus().isCreated();

        Book actualBook = webTestClient
                .get().uri("/books/{isbn}", book.isbn())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .returnResult().getResponseBody();

        assertThat(actualBook.isbn()).isEqualTo(book.isbn());
        assertThat(actualBook.id()).isNotNull();
    }

    @Test
    void whenPostRequestThenBookCreated() {
        var book = Book.of("1234567891", "Title", "Author", 19.0);
        webTestClient
                .post().uri("/books")
                .headers(httpHeaders ->
                        httpHeaders.setBearerAuth(employeeToken.accessToken()))
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .value(actualBook -> Objects.requireNonNull(actualBook.id()));
    }

    @Test
    void whenPostRequestUnauthenticatedThen401() {
        var expectedBook = Book.of("1234567890", "Title", "Author", 9.90);

        webTestClient
                .post()
                .uri("/books/")
                .bodyValue(expectedBook)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenPostRequestUnauthorizedThen403() {
        var expectedBook = Book.of("1234567890", "Title", "Author", 9.90);

        webTestClient
                .post()
                .uri("/books/")
                .headers(headers -> headers.setBearerAuth(customerToken.accessToken()))
                .bodyValue(expectedBook)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenPutRequestThenBookUpdated() {
        String isbn = "1234567892";
        var book = Book.of(isbn, "Title", "Author", 19.0);
        webTestClient
                .post().uri("/books")
                .headers(headers -> headers.setBearerAuth(employeeToken.accessToken()))
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
                .headers(headers -> headers.setBearerAuth(employeeToken.accessToken()))
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
                .headers(headers -> headers.setBearerAuth(employeeToken.accessToken()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .value(actualBook -> Objects.requireNonNull(actualBook.id()));

        webTestClient
                .delete().uri("/books/{isbn}", isbn)
                .headers(headers -> headers.setBearerAuth(employeeToken.accessToken()))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get().uri("/books/{isbn}", isbn)
                .headers(headers -> headers.setBearerAuth(employeeToken.accessToken()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(errorMessage ->
                        assertThat(errorMessage).isEqualTo("Book with isbn " + isbn + " not found."));
    }

    private record KeycloakToken(String accessToken) {
        @JsonCreator
        private KeycloakToken(@JsonProperty("access_token") final String accessToken) {
            this.accessToken = accessToken;
        }
    }

    private static KeycloakToken authenticateWith(
            String username, String password, WebClient webClient) {
        return webClient
                .post()
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "edge-service")
                        .with("client_secret", "cT5pq7W3XStcuFVQMhjPbRj57Iqxcu4n")
                        .with("username", username)
                        .with("password", password)
                )
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block();
    }

}