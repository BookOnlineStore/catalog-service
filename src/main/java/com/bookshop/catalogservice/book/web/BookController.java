package com.bookshop.catalogservice.book.web;

import com.bookshop.catalogservice.book.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping("/reduce-inventory")
    @ResponseBody
    public void reduceInventory(@RequestBody Map<String, Object> data) {
        bookRepository.updateInventoryByIsbn((String) data.get("isbn"), (Integer) data.get("quantity"));
    }

}
