package com.dabs.book_ao.services;

import com.dabs.book_ao.domain.entities.Author;
import org.springframework.stereotype.Service;

public interface AuthorService {

    Author createAuthor(Author author);

}
