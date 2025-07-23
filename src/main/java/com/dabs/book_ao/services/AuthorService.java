package com.dabs.book_ao.services;

import com.dabs.book_ao.domain.entities.Author;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AuthorService {

    Author createAuthor(Author author);

    List<Author> findAll();
}
