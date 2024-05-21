package com.bookshop.catalogservice.web;

import com.bookshop.catalogservice.config.SecurityConfig;
import com.bookshop.catalogservice.domain.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
public class BookControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService bookService;

    @MockBean
    JwtDecoder jwtDecoder;

    @Test
    void whenDeleteBookWithEmployeeRoleThenReturn204() throws Exception {
        var isbn = "1234567890";
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + isbn)
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_employee"))
                )
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void whenDeleteBookWithCustomerThenReturn403() throws Exception {
        var isbn = "1234567890";
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + isbn)
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_customer"))
                )
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void whenDeleteBookNotAuthenticatedThenReturn401() throws Exception {
        var isbn = "1234567890";
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}
