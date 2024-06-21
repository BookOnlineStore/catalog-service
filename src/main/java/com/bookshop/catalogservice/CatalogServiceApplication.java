package com.bookshop.catalogservice;

import com.bookshop.catalogservice.book.BookRepository;
import com.bookshop.catalogservice.config.BookValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CatalogServiceApplication implements RepositoryRestConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);
    }

//    /**
//     * @param v
//     */
//    @Override
//    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener v, BookRepository bookRepository) {
//        v.addValidator("beforeCreate", new BookValidator(bookRepository));
//    }

    @Bean
    BookValidator beforeSaveBookValidator(BookRepository bookRepository) {
        return new BookValidator(bookRepository);
    }

    @Bean
    BookValidator beforeCreateBookValidator(BookRepository bookRepository) {
        return new BookValidator(bookRepository);
    }
}