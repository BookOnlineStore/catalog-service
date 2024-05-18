package com.bookshop.catalogservice.web;

import com.bookshop.catalogservice.domain.Book;
import com.bookshop.catalogservice.domain.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @GetMapping
    public Iterable<Book> getAll() {
        log.info("BookController attempt to retrieve books from catalog");
        return bookService.getAllBook();
    }

    @GetMapping("{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        log.info("BookController attempt to retrieve book by isbn {} from catalog", isbn);
        return bookService.getBookByIsbn(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@Valid @RequestBody Book book) {
        log.info("BookController attempt to create book");
        return bookService.addBookToCatalog(book);
    }

    @PutMapping("{isbn}")
    public Book updateBookByIsbn(@PathVariable String isbn, @Valid @RequestBody Book book) {
        log.info("BookController attempt to update book with isbn {}", isbn);
        return bookService.editBookDetail(isbn, book);
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookByIsbn(@PathVariable String isbn) {
        log.info("BookController attempt to delete book with isbn {}", isbn);
        bookService.removeBookFromCatalog(isbn);
    }

}
