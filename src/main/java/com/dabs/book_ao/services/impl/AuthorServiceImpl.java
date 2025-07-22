package com.dabs.book_ao.services.impl;

import com.dabs.book_ao.domain.entities.Author;
import com.dabs.book_ao.repositories.AuthorRepository;
import com.dabs.book_ao.services.AuthorService;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

}
