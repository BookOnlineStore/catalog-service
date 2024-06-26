package com.bookshop.catalogservice.book;

import com.bookshop.catalogservice.book.projection.PhotoProjection;
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

import java.util.List;
import java.util.Optional;

/**
 * The interface Book repository.
 */
@RepositoryRestResource(path = "books")
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    /**
     * Exists by isbn boolean.
     *
     * @param isbn the isbn
     * @return the boolean
     */
    @RestResource(exported = false)
    boolean existsByIsbn(String isbn);

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<Book> findAll(Pageable pageable);

    /**
     * Find by isbn optional.
     *
     * @param query the query
     * @return the optional
     */
    @RestResource(path = "isbn")
    Optional<Book> findByIsbn(String query);

    /**
     * Find all by author containing page.
     *
     * @param query    the query
     * @param pageable the pageable
     * @return the page
     */
    @RestResource(path = "authors")
    Page<Book> findAllByAuthorContaining(String query, Pageable pageable);

    /**
     * Find all by title containing page.
     *
     * @param query    the query
     * @param pageable the pageable
     * @return the page
     */
    @RestResource(path = "titles")
    Page<Book> findAllByTitleContaining(String query, Pageable pageable);

    /**
     * Find all by publisher containing page.
     *
     * @param query    the query
     * @param pageable the pageable
     * @return the page
     */
    @RestResource(path = "publishers")
    Page<Book> findAllByPublisherContaining(String query, Pageable pageable);

    /**
     * Find all by supplier containing page.
     *
     * @param query    the query
     * @param pageable the pageable
     * @return the page
     */
    @RestResource(path = "suppliers")
    Page<Book> findAllBySupplierContaining(String query, Pageable pageable);

    /**
     * Delete by isbn.
     *
     * @param isbn the isbn
     */
    @Modifying
    @Transactional
    @Query("delete from books where isbn = :isbn")
    void deleteByIsbn(@Param("isbn") String isbn);

    /**
     * Update inventory by isbn.
     *
     * @param isbn     the isbn
     * @param quantity the quantity
     */
    @Query("update books set inventory = inventory - :quantity where isbn = :isbn")
    @Modifying
    @Transactional
    @RestResource(exported = false)
    void updateInventoryByIsbn(@Param("isbn") String isbn, @Param("quantity") Integer quantity);


    /**
     * Find all book best seller list.
     *
     * @param size the size
     * @param page the page
     * @return the list
     */
    @Query("select * from books b order by b.purchases desc limit :size offset :page * :size")
    @RestResource(exported = false)
    List<Book> findAllBookBestSeller(int size, int page);

}
