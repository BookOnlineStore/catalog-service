package com.bookshop.catalogservice.book.projection;

import com.bookshop.catalogservice.book.Book;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(name = "photoProjection", types = {Book.class})
public interface PhotoProjection {

    String getIsbn();

    List<String> getPhotos();

}
