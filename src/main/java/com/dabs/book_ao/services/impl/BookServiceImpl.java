package com.dabs.book_ao.services.impl;

import com.dabs.book_ao.domain.entities.Book;
import com.dabs.book_ao.repositories.BookRepository;
import com.dabs.book_ao.services.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(String isbn, Book book) {
        book.setIsbn(isbn);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
       return StreamSupport
               .stream(
                       bookRepository.findAll().spliterator(),
                       false)
               .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findOne(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public boolean isExists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public Book partialUpdate(String isbn, Book book) {
        book.setIsbn(isbn);
        return bookRepository.findById(isbn).map(foundBook -> {
            Optional.ofNullable(book.getTitle()).ifPresent(foundBook::setTitle);
            return bookRepository.save(foundBook);
        }).orElseThrow(() -> new RuntimeException("Book does not exist"));
    }

    @Override
    public void delete(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
