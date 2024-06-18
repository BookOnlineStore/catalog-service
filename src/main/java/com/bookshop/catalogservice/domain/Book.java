package com.bookshop.catalogservice.domain;

import lombok.Builder;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.List;

@Table("books")
@Builder
public record Book(
        @Id
        Long id,
        @NotBlank(groups = OnCreate.class, message = "The isbn of book must not be null or blank.")
        @Pattern(groups = {OnCreate.class, OnUpdate.class}, regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN must be valid")
        String isbn,
        @NotBlank(groups = OnCreate.class, message = "The title of book must not be null or blank.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 255, message = "The title too long")
        String title,
        @NotBlank(groups = OnCreate.class, message = "The author of book must not be null or blank.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 255, message = "The author name is too long")
        String author,
        @NotBlank(groups = OnCreate.class, message = "The publisher of book must not be null or blank.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 255, message = "The publisher name is too long")
        String publisher,
        @NotBlank(groups = OnCreate.class, message = "The supplier of book must not be null or blank.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, max = 255, message = "The supplier name is too long")
        String supplier,
        String description,
        @NotNull(groups = OnCreate.class, message = "The price of book must not be null.")
        @Positive(groups = {OnCreate.class, OnUpdate.class}, message = "Value of price must greater than zero")
        Double price,
        @NotNull(groups = OnCreate.class, message = "The language of book must not be null.")
        Language language,
        @NotNull(groups = OnCreate.class, message = "The book cover type must not be null.")
        CoverType coverType,
        @NotNull(groups = OnCreate.class, message = "The number of pages of book must not be null.")
        @Min(groups = {OnCreate.class, OnUpdate.class}, value = 3, message = "The number of pages must greater than 3")
        @Max(groups = {OnCreate.class, OnUpdate.class}, value = 3000, message = "The number of pages must less than 3000")
        Integer numberOfPages,
        @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
        @NotNull(groups = OnCreate.class, message = "The measure of book must not be null.")
        Measure measure,
        List<String> photos,
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

    public interface OnCreate {}
    public interface OnUpdate {}

    public static Book of(String isbn, String title,
                          String author, String publisher,
                          String supplier, double price,
                          Language language, CoverType coverType, Integer numberOfPages,
                          Measure measure) {
        return new Book(null, isbn, title, author, publisher, supplier, null, price, language,
                coverType, numberOfPages, measure, null,
                null, null, null,
                null, 0);
    }

    public static Book of(String isbn, String title,
                          String author, String publisher,
                          String supplier, String description,
                          double price, Language language, CoverType coverType,
                          int numberOfPages, Measure measure) {
        return new Book(null, isbn, title, author, publisher, supplier, description, price, language,
                coverType, numberOfPages, measure, null,
                null, null, null,
                null, 0);
    }

    public String addPhoto(String photo) {
        photos.add(photo);
        return photo;
    }

    public String addAllPhotos(List<String> photos) {
        this.photos.addAll(photos);
        return photos.get(0);
    }

}
