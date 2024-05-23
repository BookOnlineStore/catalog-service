package com.bookshop.catalogservice.domain;

import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.*;
import java.time.Instant;

@Table("books")
public record Book(
        @Id
        Long id,
        @NotBlank(message = "The isbn of book must not be null or blank.")
                @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN must be valid")
        String isbn,
        @NotBlank(message = "The title of book must not be null or blank.")
        @Size(max = 255, message = "The title too long")
        String title,
        @NotBlank(message = "The author of book must not be null or blank.")
        String author,
        @NotNull(message = "The price of book must not be null.")
        @Positive(message = "Value of price must greater than zero")
        Double price,
        @CreatedDate
        Instant createdAt,
        @CreatedBy
        String createdBy,
        @LastModifiedDate
        Instant lastModifiedAt,
        @LastModifiedBy
        String lastModifiedBy,
        @Version
        int version
) {

    public static Book of(String isbn, String title, String author, double price) {
        return new Book(null, isbn,
                title, author, price,
                null, null,
                null, null, 0);
    }

}
