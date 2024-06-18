package com.bookshop.catalogservice.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CoverType {
    PAPERBACK("paperback"), HARDCOVER("hardcover");
    private String value;
}
