package com.bookshop.catalogservice.book.web;

import com.bookshop.catalogservice.book.BookRepository;
import com.bookshop.catalogservice.book.exception.BookNotEnoughInventoryException;
import com.bookshop.catalogservice.book.exception.BookNotFoundException;
import com.bookshop.catalogservice.book.CartItem;
import com.bookshop.catalogservice.book.CartItemRepository;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartItemController {

    private static final Logger log = LoggerFactory.getLogger(CartItemController.class);
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    public CartItemController(CartItemRepository cartItemRepository, BookRepository bookRepository) {
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/cart-items/my-cart")
    public CollectionModel<CartItem> getCartItems(Pageable pageable, @AuthenticationPrincipal Jwt jwt) {
        var username = jwt.getClaim(StandardClaimNames.PREFERRED_USERNAME).toString();
        log.info("User {} is fetching cart items", username);
        return CollectionModel.of(cartItemRepository.findByCreatedBy(username, pageable));
    }

    @PostMapping("/cart-items/add-to-cart")
    @ResponseStatus(HttpStatus.CREATED)
    public CartItem createCartItem(@Valid @RequestBody CartItem cartItem) {
        log.info("Adding cart item {} to cart", cartItem);
        var bookExisting = bookRepository.findByIsbn(cartItem.getIsbn()).map(book -> {
            if (book.getInventory() < cartItem.getQuantity())
                throw new BookNotEnoughInventoryException(cartItem.getIsbn());
            return book;
        }).orElseThrow(() -> new BookNotFoundException(cartItem.getIsbn()));
        return cartItemRepository.findByIsbn(cartItem.getIsbn())
                .map(existingCartItem -> {
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItem.getQuantity());
                    existingCartItem.setTotalPrice(existingCartItem.getQuantity() * bookExisting.getPrice());
                    return cartItemRepository.save(existingCartItem);
                }).orElseGet(() -> {
                    cartItem.setTotalPrice(cartItem.getQuantity() * bookExisting.getPrice());
                    CartItem save = cartItemRepository.save(cartItem);
                    log.info("Cart item {} is added to cart", save);
                    return save;
                });
    }
}
