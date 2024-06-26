package com.bookshop.catalogservice.book.event;

import java.util.Map;
import java.util.UUID;

public record BooksPlacedMessage(
        UUID orderId,
        Map<String, Integer> lineItems
){

}
