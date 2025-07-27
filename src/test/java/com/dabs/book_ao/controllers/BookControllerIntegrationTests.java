package com.dabs.book_ao.controllers;

import com.dabs.book_ao.TestDataUtil;
import com.dabs.book_ao.domain.dto.BookDto;
import com.dabs.book_ao.domain.entities.Book;
import com.dabs.book_ao.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private BookService bookService;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.mapper = new ObjectMapper();
        this.bookService = bookService;
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsHttp201Created() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String bookDtoJson = mapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatUpdateBookSuccessfullyReturnsHttp200Created() throws Exception {
        Book book = TestDataUtil.createTestBookA(null);
        Book savedBook = bookService.save(book.getIsbn(), book);
        BookDto bookUpdated = TestDataUtil.createTestBookDtoA(null);
        bookUpdated.setIsbn(savedBook.getIsbn());
        String bookDtoJson = mapper.writeValueAsString(bookUpdated);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsSavedBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String bookDtoJson = mapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        );
    }

    @Test
    public void testThatUpdateBookSuccessfullyReturnsUpdatedBook() throws Exception {
        Book book = TestDataUtil.createTestBookA(null);
        Book savedBook = bookService.save(book.getIsbn(), book);

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        bookDto.setIsbn(savedBook.getIsbn());
        bookDto.setTitle("NEW TITLE");
        String bookDtoJson = mapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        );
    }

    @Test
    public void testThatListBooksSuccessfullyReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsListOfBooks() throws Exception {
        Book book = TestDataUtil.createTestBookA(null);
        bookService.save(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(book.getTitle())
        );
    }

    @Test
    public void testThatGetBookSuccessfullyReturnsHttpStatus200OkWhenBookExists() throws Exception {
        Book book = TestDataUtil.createTestBookA(null);
        bookService.save(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/{isbn}", book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetBookSuccessfullyReturnsHttpStatus404NotFoundWhenBookDoestNotExists() throws Exception {
        Book book = TestDataUtil.createTestBookA(null);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/{isbn}", book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetBookSuccessfullyReturnsBook() throws Exception {
        Book book = TestDataUtil.createTestBookA(null);
        bookService.save(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/{isbn}", book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("978-1-2345-6789-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("The Shadow in the Attic")
        );
    }

}
