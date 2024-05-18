package com.bookshop.catalogservice.domain;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public Iterable<Book> getAllBook () {
        return bookRepository.findAll();
    }

    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(bookExists -> {
                    log.info("Book with isbn {} found.", isbn);
                    return bookExists;
                })
                .orElseThrow(() -> {
                    log.info("Book with isbn {} not found.", isbn);
                    return new BookNotFoundException(isbn);
                });
    }

    public Book addBookToCatalog(Book book) {
        if (bookRepository.existsByIsbn(book.isbn())) {
            log.info("Book with isbn {} already exists, can not create!.", book.isbn());
            throw new BookAlreadyExistsException(book.isbn());
        }
        log.info("Book with isbn {} created successfully!. ID generated: {}", book.isbn(), book.id());
        return bookRepository.save(book);
    }

    public Book editBookDetail(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                .map(bookPersist -> {
                    log.info("Book with isbn {} found.", isbn);
                    var bookUpdate = new Book(
                            bookPersist.id(),
                            bookPersist.isbn(),
                            book.title(),
                            book.author(),
                            book.price(),
                            bookPersist.createdAt(),
                            bookPersist.createdBy(),
                            bookPersist.lastModifiedAt(),
                            bookPersist.lastModifiedBy(),
                            bookPersist.version()
                    );
                    return bookRepository.save(bookUpdate);
                })
                .orElseThrow(() -> {
                    log.info("Book with isbn {} not found.", isbn);
                    return new BookNotFoundException(isbn);
                });
    }

    public void removeBookFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

}
