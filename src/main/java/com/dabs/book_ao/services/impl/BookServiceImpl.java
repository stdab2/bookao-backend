package com.dabs.book_ao.services.impl;

import com.dabs.book_ao.domain.entities.Book;
import com.dabs.book_ao.repositories.BookRepository;
import com.dabs.book_ao.services.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createBook(String isbn, Book book) {
        book.setIsbn(isbn);
        return bookRepository.save(book);
    }

}
