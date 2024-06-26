package com.bookshop.catalogservice.book.web;

import com.bookshop.catalogservice.book.Book;
import com.bookshop.catalogservice.book.BookRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private final BookRepository bookRepository;
    private final EntityLinks entityLinks;

    public BookController(BookRepository bookRepository, EntityLinks entityLinks) {
        this.bookRepository = bookRepository;
        this.entityLinks = entityLinks;
    }

    @GetMapping("/books/best-seller")
    public CollectionModel<Book> getBestSellerBooks(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        var allBookBestSeller = bookRepository.findAllBookBestSeller(pageable.getPageSize(), pageable.getPageNumber());
        return CollectionModel.of(allBookBestSeller, entityLinks.linkToCollectionResource(Book.class));
    }

}
