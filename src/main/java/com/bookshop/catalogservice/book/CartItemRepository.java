package com.bookshop.catalogservice.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(path = "cart-items")
public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    Optional<CartItem> findByIsbn(String isbn);

    Page<CartItem> findAll(Pageable pageable);

    Page<CartItem> findByCreatedBy(String createdBy, Pageable pageable);

    /**
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    @RestResource(exported = false)
    <S extends CartItem> S save(S entity);
}
