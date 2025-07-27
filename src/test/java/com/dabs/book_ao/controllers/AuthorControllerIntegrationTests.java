package com.dabs.book_ao.controllers;

import com.dabs.book_ao.TestDataUtil;
import com.dabs.book_ao.domain.entities.Author;
import com.dabs.book_ao.services.AuthorService;
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
public class AuthorControllerIntegrationTests {

    private AuthorService authorService;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.mapper = new ObjectMapper();
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        Author author = TestDataUtil.createTestAuthorA();
        String authorJson = mapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        Author author = TestDataUtil.createTestAuthorA();
        String authorJson = mapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        );
    }

    @Test
    public void testThatListAuthorsSuccessfullyReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListAuthorsSuccessfullyReturnsListOfAuthors() throws Exception {
        Author author = TestDataUtil.createTestAuthorA();
        authorService.save(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(80)
        );
    }

    @Test
    public void testThatGetAuthorSuccessfullyReturnsHttpStatus200OkWhenAuthorExists() throws Exception {
        Author author = TestDataUtil.createTestAuthorA();
        authorService.save(author);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAuthorSuccessfullyReturnsHttpStatus404NotFoundWhenAuthorDoestNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetAuthorSuccessfullyReturnsAuthor() throws Exception {
        Author author = TestDataUtil.createTestAuthorA();
        authorService.save(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        );
    }

    @Test
    public void testThatFullUpdateAuthorSuccessfullyReturnsHttpStatus200OkWhenAuthorExists() throws Exception {
        Author author = TestDataUtil.createTestAuthorA();
        authorService.save(author);
        Author updatedAuthor = TestDataUtil.createTestAuthorB();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/{id}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedAuthor))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateAuthorSuccessfullyReturnsHttpStatus400NotFoundWhenAuthorDoesNotExists() throws Exception {
        Author updatedAuthor = TestDataUtil.createTestAuthorB();
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/{id}", updatedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedAuthor))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateAuthorSuccessfullyReturnsUpdatedAuthor() throws Exception {
        Author author = TestDataUtil.createTestAuthorA();
        authorService.save(author);
        Author updatedAuthor = TestDataUtil.createTestAuthorB();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/{id}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedAuthor))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(author.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(updatedAuthor.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(updatedAuthor.getAge())
        );
    }
}
