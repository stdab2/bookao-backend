package com.dabs.book_ao.services;

import com.dabs.book_ao.domain.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book save(String isbn, Book book);

    List<Book> findAll();

    Optional<Book> findOne(String isbn);

    boolean isExists(String isbn);

    Book partialUpdate(String isbn, Book book);

    void delete(String isbn);
}
