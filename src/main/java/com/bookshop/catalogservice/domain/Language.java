package com.bookshop.catalogservice.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Language {
    VIETNAMESE(
            "Vietnamese",
            "Tiếng Việt"
    ), ENGLISH(
            "English",
            "English"
    );

    private final String englishName;
    private final String nativeName;
}
