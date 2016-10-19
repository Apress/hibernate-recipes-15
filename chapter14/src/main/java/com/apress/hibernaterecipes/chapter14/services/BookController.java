package com.apress.hibernaterecipes.chapter14.services;

import com.apress.hibernaterecipes.chapter14.model.Book;

import java.util.List;

public interface BookController {
    Book getBook(Integer authorId, Integer id);
    Book removeBook(Integer authorId, Integer id);
    Book updateBook(Integer authorId, Integer id, Book updatedBook);
    Book createBook(Integer authorId, Book model);
    public Book[] getBooks(Integer authorId);
}
