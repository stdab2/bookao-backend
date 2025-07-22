package com.dabs.book_ao.controllers;

import com.dabs.book_ao.domain.dto.BookDto;
import com.dabs.book_ao.domain.entities.Book;
import com.dabs.book_ao.mappers.Mapper;
import com.dabs.book_ao.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {

    private BookService bookService;
    private Mapper<Book, BookDto> bookMapper;

    public BookController(BookService bookService, Mapper<Book, BookDto> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> createBook(@PathVariable("isbn") String isbn,
                                              @RequestBody BookDto bookDto) {
        Book bookEntity = bookMapper.mapFrom(bookDto);
        Book savedBookEntity = bookService.createBook(isbn, bookEntity);
        return new ResponseEntity<>(bookMapper.mapTo(savedBookEntity), HttpStatus.CREATED);
    }

    /*@PostMapping(path = "/books")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto book) {
        Book bookEntity = bookMapper.mapFrom(book);
        Book savedBookEntity = bookService.createBook(bookEntity);
        return new ResponseEntity<>(bookMapper.mapTo(savedBookEntity), HttpStatus.CREATED);
    }*/
}
