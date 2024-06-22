package com.bookshop.catalogservice.book;

import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.Instant;
import java.util.List;

@Table("books")
public class Book {

    @Id
    private Long id;

    @NotBlank(message = "The isbn of book must not be null or blank.")
    @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN must be valid")
    private String isbn;

    @NotBlank(message = "The title of book must not be null or blank.")
    @Size(max = 255, message = "The title too long")
    private String title;

    @NotBlank(message = "The author of book must not be null or blank.")
    @Size(max = 255, message = "The author name is too long")
    private String author;

    @NotBlank(message = "The publisher of book must not be null or blank.")
    @Size(max = 255, message = "The publisher name is too long")
    private String publisher;

    @NotBlank(message = "The supplier of book must not be null or blank.")
    @Size(max = 255, message = "The supplier name is too long")
    private String supplier;

    private String description;

    @NotNull(message = "The price of book must not be null.")
    @Positive(message = "Value of price must greater than zero")
    private Double price;

    @NotNull(message = "The inventory of book must not be null.")
    @Min(value = 0, message = "The inventory must greater than 0")
    private Integer inventory;

    @NotNull(message = "The language of book must not be null.")
    private Language language;

    @NotNull(message = "The book cover type must not be null.")
    private CoverType coverType;

    @NotNull(message = "The number of pages of book must not be null.")
    @Min(value = 3, message = "The number of pages must greater than 3")
    @Max(value = 3000, message = "The number of pages must less than 3000")
    private Integer numberOfPages;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    @NotNull(message = "The measure of book must not be null.")
    @Valid
    private Measure measure;

    private List<String> photos;

    @CreatedDate
    private Instant createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Instant lastModifiedAt;

    @LastModifiedBy
    private String lastModifiedBy;

    @Version
    private int version;

    public Book() {
    }

    public Book(Long id, String isbn, String title, String author, String publisher, String supplier, String description, Double price, Integer inventory, Language language, CoverType coverType, Integer numberOfPages, Measure measure, List<String> photos, Instant createdAt, String createdBy, Instant lastModifiedAt, String lastModifiedBy, int version) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.supplier = supplier;
        this.description = description;
        this.price = price;
        this.inventory = inventory;
        this.language = language;
        this.coverType = coverType;
        this.numberOfPages = numberOfPages;
        this.measure = measure;
        this.photos = photos;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedBy = lastModifiedBy;
        this.version = version;
    }

    public static Book of(String isbn, String title, String author, String publisher, String supplier, double price, int inventory, Language language, CoverType coverType, Integer numberOfPages, Measure measure) {
        return new Book(null, isbn, title, author, publisher, supplier, null, price, inventory, language, coverType, numberOfPages, measure, null, null, null, null, null, 0);
    }

    public static Book of(String isbn, String title, String author, String publisher, String supplier, String description, double price, int  inventory, Language language, CoverType coverType, int numberOfPages, Measure measure) {
        return new Book(null, isbn, title, author, publisher, supplier, description, price, inventory, language, coverType, numberOfPages, measure, null, null, null, null, null, 0);
    }

    public String addPhoto(String photo) {
        photos.add(photo);
        return photo;
    }

    public String addAllPhotos(List<String> photos) {
        this.photos.addAll(photos);
        return photos.get(0);
    }

    // Getters and setters for all fields
    // ...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "The isbn of book must not be null or blank.") @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN must be valid") String getIsbn() {
        return isbn;
    }

    public void setIsbn(@NotBlank(message = "The isbn of book must not be null or blank.") @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN must be valid") String isbn) {
        this.isbn = isbn;
    }

    public @NotBlank(message = "The title of book must not be null or blank.") @Size(max = 255, message = "The title too long") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "The title of book must not be null or blank.") @Size(max = 255, message = "The title too long") String title) {
        this.title = title;
    }

    public @NotBlank(message = "The author of book must not be null or blank.") @Size(max = 255, message = "The author name is too long") String getAuthor() {
        return author;
    }

    public void setAuthor(@NotBlank(message = "The author of book must not be null or blank.") @Size(max = 255, message = "The author name is too long") String author) {
        this.author = author;
    }

    public @NotBlank(message = "The publisher of book must not be null or blank.") @Size(max = 255, message = "The publisher name is too long") String getPublisher() {
        return publisher;
    }

    public void setPublisher(@NotBlank(message = "The publisher of book must not be null or blank.") @Size(max = 255, message = "The publisher name is too long") String publisher) {
        this.publisher = publisher;
    }

    public @NotBlank(message = "The supplier of book must not be null or blank.") @Size(max = 255, message = "The supplier name is too long") String getSupplier() {
        return supplier;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setSupplier(@NotBlank(message = "The supplier of book must not be null or blank.") @Size(max = 255, message = "The supplier name is too long") String supplier) {
        this.supplier = supplier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotNull(message = "The price of book must not be null.") @Positive(message = "Value of price must greater than zero") Double getPrice() {
        return price;
    }

    public void setPrice(@NotNull(message = "The price of book must not be null.") @Positive(message = "Value of price must greater than zero") Double price) {
        this.price = price;
    }

    public @NotNull(message = "The inventory of book must not be null.") @Min(value = 0, message = "The inventory must greater than 0") Integer getInventory() {
        return inventory;
    }

    public void setInventory(@NotNull(message = "The inventory of book must not be null.") @Min(value = 0, message = "The inventory must greater than 0") Integer inventory) {
        this.inventory = inventory;
    }

    public @NotNull(message = "The language of book must not be null.") Language getLanguage() {
        return language;
    }

    public void setLanguage(@NotNull(message = "The language of book must not be null.") Language language) {
        this.language = language;
    }

    public @NotNull(message = "The book cover type must not be null.") CoverType getCoverType() {
        return coverType;
    }

    public void setCoverType(@NotNull(message = "The book cover type must not be null.") CoverType coverType) {
        this.coverType = coverType;
    }

    public @NotNull(message = "The number of pages of book must not be null.") @Min(value = 3, message = "The number of pages must greater than 3") @Max(value = 3000, message = "The number of pages must less than 3000") Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(@NotNull(message = "The number of pages of book must not be null.") @Min(value = 3, message = "The number of pages must greater than 3") @Max(value = 3000, message = "The number of pages must less than 3000") Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public @NotNull(message = "The measure of book must not be null.") Measure getMeasure() {
        return measure;
    }

    public void setMeasure(@NotNull(message = "The measure of book must not be null.") Measure measure) {
        this.measure = measure;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", supplier='" + supplier + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", language=" + language +
                ", coverType=" + coverType +
                ", numberOfPages=" + numberOfPages +
                ", measure=" + measure +
                ", photos=" + photos +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastModifiedAt=" + lastModifiedAt +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", version=" + version +
                '}';
    }
}
