package com.bookshop.catalogservice.config;

import com.bookshop.catalogservice.book.Book;
import com.bookshop.catalogservice.book.web.PhotoController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


/**
 * The type Book representation model processor.
 * @author thainguyencoffee
 * @implNote Additional links to Book EntityModel
 */
@Component
public class BookRepresentationModelProcessor
        implements RepresentationModelProcessor<EntityModel<Book>> {

    /**
     * @param model
     * @return
     */
    @Override
    public EntityModel<Book> process(EntityModel<Book> model) {
        Book content = model.getContent();
        model.add(linkTo(WebMvcLinkBuilder.methodOn(PhotoController.class)
                .uploadPhoto(content.getIsbn(), null)).withRel("upload-photos"));
        return model;
    }

}
