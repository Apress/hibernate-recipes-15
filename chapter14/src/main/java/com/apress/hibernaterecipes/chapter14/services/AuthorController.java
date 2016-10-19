package com.apress.hibernaterecipes.chapter14.services;

import com.apress.hibernaterecipes.chapter14.model.Author;

import java.util.List;

public interface AuthorController {
    Author getAuthor(Integer id);
    Author removeAuthor(Integer id);
    Author updateAuthor(Integer id, Author updatedAuthor);
    Author createAuthor(Author model);
    Author[] getAuthors();
}
