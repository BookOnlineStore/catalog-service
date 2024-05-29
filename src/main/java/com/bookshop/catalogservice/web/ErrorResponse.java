package com.bookshop.catalogservice.web;

public record ErrorResponse(
        String fieldInValid, String msg) {
}
