package com.bookshop.catalogservice.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RepositoryRestResource(path = "books")
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);

    @RestResource(path = "isbn")
    Optional<Book> findByIsbn(String query);

    @RestResource(path = "authors")
    Page<Book> findAllByAuthorContaining(String query, Pageable pageable);

    @RestResource(path = "titles")
    Page<Book> findAllByTitleContaining(String query, Pageable pageable);

    @RestResource(path = "publishers")
    Page<Book> findAllByPublisherContaining(String query, Pageable pageable);

    @RestResource(path = "suppliers")
    Page<Book> findAllBySupplierContainingAndLanguage(String query, Language language, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from books where isbn = :isbn")
    void deleteByIsbn(@Param("isbn") String isbn);
}
