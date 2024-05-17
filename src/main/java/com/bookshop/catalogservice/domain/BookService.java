package com.bookshop.catalogservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Iterable<Book> getAllBook () {
        return bookRepository.findAll();
    }

    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public Book addBookToCatalog(Book book) {
        if (bookRepository.existsByIsbn(book.isbn())) {
            throw new BookAlreadyExistsException(book.isbn());
        }
        return bookRepository.save(book);
    }

    public Book editBookDetail(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                .map(bookPersist -> {
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
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public void removeBookFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

}
