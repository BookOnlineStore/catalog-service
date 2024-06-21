package com.bookshop.catalogservice.book.web;

import com.bookshop.catalogservice.book.Book;
import com.bookshop.catalogservice.book.BookRepository;
import com.bookshop.catalogservice.book.exception.BookNotFoundException;
import com.bookshop.catalogservice.cloudinary.CloudinaryUtils;
import com.cloudinary.Cloudinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("books")
public class PhotoController {

    private static final Logger log = LoggerFactory.getLogger(PhotoController.class);
    private final Cloudinary cloudinary;
    private final BookRepository bookRepository;
    private final EntityLinks entityLinks;


    public PhotoController(Cloudinary cloudinary, BookRepository bookRepository, EntityLinks entityLinks) {
        this.cloudinary = cloudinary;
        this.bookRepository = bookRepository;
        this.entityLinks = entityLinks;
    }

    @PutMapping(value = "/{isbn}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EntityModel<?> uploadPhoto(@PathVariable String isbn, @ModelAttribute List<MultipartFile> photos) {
        Optional<Book> bookO = bookRepository.findByIsbn(isbn);
        Book bookUpdate = bookO.map(book -> {
            var oldPhotos = book.getPhotos();
            // clear old photos
            if (oldPhotos!= null && !oldPhotos.isEmpty()) {
                book.getPhotos().forEach(photoUrl -> {
                    try {
                        CloudinaryUtils.deleteFile(CloudinaryUtils.convertUrlToPublicId(photoUrl), cloudinary);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            List<String> urls = CloudinaryUtils.convertListMultipartFileToListUrl(photos, cloudinary);
            book.setPhotos(urls);
            return bookRepository.save(book);
        }).orElseThrow(BookNotFoundException::new);
        return EntityModel.of(bookUpdate,
                entityLinks.linkToItemResource(Book.class, bookUpdate.getIsbn()).withSelfRel());
    }

}
