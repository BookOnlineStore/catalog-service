package com.bookshop.catalogservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bookstore")
public record BookstoreProperties (
        CloudinaryProperties cloudinary
) {
    public record CloudinaryProperties(
            String cloudName,
            String apiKey,
            String apiSecret
    ) {
    }

}
