package com.dabs.book_ao.services.impl;

import com.dabs.book_ao.domain.entities.Author;
import com.dabs.book_ao.repositories.AuthorRepository;
import com.dabs.book_ao.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public List<Author> findAll() {
        return StreamSupport.stream(authorRepository
                .findAll()
                .spliterator(),
                false)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Author> findOne(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public Author partialUpdate(Long id, Author author) {
        author.setId(id);
        return authorRepository.findById(id).map(foundAuthor -> {
            Optional.ofNullable(author.getName()).ifPresent(foundAuthor::setName);
            Optional.ofNullable(author.getAge()).ifPresent(foundAuthor::setAge);
            return authorRepository.save(foundAuthor);
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
