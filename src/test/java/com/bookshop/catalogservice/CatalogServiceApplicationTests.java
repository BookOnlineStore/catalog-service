package com.bookshop.catalogservice;

import com.bookshop.catalogservice.book.*;
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
    void whenGetBookThenOk() {
        Book actualBook = webTestClient
                .get().uri("/books")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .returnResult().getResponseBody();
    }

    @Test
    void whenOtherMethodToBookUrlThenUnauthenticated() {
        var book = Book.of("1234567891", "Title 1", "Author 1",
                "Publisher 1", "Supplier 1", 19.02, 3, Language.ENGLISH,
                CoverType.PAPERBACK, 25, new Measure(120, 180, 10, 200));
        webTestClient
                .post().uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenAuthenticatedPostBookThenReturn() {
        var book = Book.of("1234567891", "Title 1", "Author 1",
                "Publisher 1", "Supplier 1", 19.02, 3, Language.ENGLISH,
                CoverType.PAPERBACK, 25, new Measure(120, 180, 10, 200));
        webTestClient
                .post().uri("/books")
                .headers(headers -> headers.setBearerAuth(employeeToken.accessToken))
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated();
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