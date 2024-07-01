package com.bookshop.catalogservice.config;

import com.bookshop.catalogservice.book.Book;
import com.bookshop.catalogservice.book.BookRepository;
import com.bookshop.catalogservice.book.CartItem;
import com.bookshop.catalogservice.book.CartItemRepository;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy.RepositoryDetectionStrategies;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Component
public class SpringDataRestConfiguration implements RepositoryRestConfigurer {

    /**
     * @param config
     * @param cors
     */
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors) {

        ExposureConfiguration exposureConfiguration = config.getExposureConfiguration();
        exposureConfiguration.disablePutOnItemResources();

        config
                .setRepositoryDetectionStrategy(RepositoryDetectionStrategies.ANNOTATED)
                .withEntityLookup()
                .forRepository(BookRepository.class)
                .withIdMapping(Book::getIsbn)
                .withLookup(BookRepository::findByIsbn)

                .forRepository(CartItemRepository.class)
                .withIdMapping(CartItem::getId)
                .withLookup(CartItemRepository::findById);
    }

}
