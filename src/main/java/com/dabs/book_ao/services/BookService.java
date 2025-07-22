package com.dabs.book_ao.services;

import com.dabs.book_ao.domain.entities.Book;

public interface BookService {

    Book createBook(String isbn, Book book);

}
