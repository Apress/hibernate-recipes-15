package com.apress.hibernaterecipes.chapter14.services;

import com.apress.hibernaterecipes.chapter14.model.Author;
import com.apress.hibernaterecipes.chapter14.model.Book;
import com.apress.hibernaterecipes.web.SessionManager;

import org.hibernate.*;

import javax.ws.rs.*;
import java.util.List;

@Path("library")
public class ServiceImpl implements AuthorController, BookController {
    @GET
    @Path("/hello")
    @Produces("application/json")
    public String heartbeat() {
        return "Hello, world";
    }
    
@GET
@Path("/authors/")
@Consumes("application/json")
@Produces("application/json")
@Override
public Author[] getAuthors() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    try {
        Query query = session.getNamedQuery("author.findAll");
        query.setReadOnly(true);
        @SuppressWarnings("unchecked")
        List<Author> authors = (List<Author>) query.list();
        for (Author author : authors) {
            Hibernate.initialize(author);
        }
        tx.commit();
        return authors.toArray(new Author[0]);
    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
        session.close();
    }

}

@Override
@GET
@Path("/authors/{id}")
@Consumes("application/json")
@Produces("application/json")
public Author getAuthor(@PathParam("id") Integer id) {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    try {
        Author author = (Author) session.byId(Author.class)
                .with(LockOptions.NONE)
                .getReference(id);
        Hibernate.initialize(author);
        tx.commit();
        return author;
    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
        session.close();
    }
}

@Override
@DELETE
@Path("/authors/{id}")
@Consumes("application/json")
@Produces("application/json")
public Author removeAuthor(@PathParam("id") Integer id) {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    try {
        Author author = (Author) session.byId(Author.class)
                .with(LockOptions.NONE)
                .getReference(id);
        session.delete(author);
        tx.commit();
        return author;
    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
        session.close();
    }
}


@Override
@PUT
@Path("/authors/{id}")
@Consumes("application/json")
@Produces("application/json")
public Author updateAuthor(@PathParam("id") Integer id, Author updatedAuthor) {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    try {
        Author author = (Author) session.byId(Author.class)
                .with(LockOptions.NONE)
                .getReference(id);
        author.setName(updatedAuthor.getName());
        tx.commit();
        return updatedAuthor;
    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
        session.close();
    }
}

@Override
@POST
@Path("/authors")
@Consumes("application/json")
@Produces("application/json")
public Author createAuthor(Author model) {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    try {
        session.persist(model);
        tx.commit();
        return model;
    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
        session.close();
    }
}

@Override
@GET
@Path("/authors/{authorId}/books/{id}")
public Book getBook(@PathParam("authorId") Integer authorId, @PathParam("id") Integer id) {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    try {
        Query query = session.getNamedQuery("book.findByIdAndAuthor");
        query.setParameter("id", id);
        query.setParameter("authorId", authorId);
        Book book = (Book) query.uniqueResult();
        if (book == null) {
            throw new ObjectNotFoundException(id, "Book");
        }
        Hibernate.initialize(book);
        tx.commit();
        return book;
    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
        session.close();
    }
}

@GET
@Path("/authors/{authorId}/books")
@Override
@Consumes("application/json")
@Produces("application/json")
public Book[] getBooks(@PathParam("authorId") Integer authorId) {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    try {
        Query query = session.getNamedQuery("book.findByAuthor");
        query.setReadOnly(true);
        query.setParameter("authorId", authorId);
        @SuppressWarnings("unchecked")
        List<Book> books = (List<Book>) query.list();
        for (Book book : books) {
            Hibernate.initialize(book);
        }
        tx.commit();
        return books.toArray(new Book[0]);
    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
        session.close();
    }
}

@Override
@DELETE
@Path("/authors/{authorId}/books/{id}")
@Consumes("application/json")
@Produces("application/json")
public Book removeBook(@PathParam("authorId") Integer authorId, @PathParam("id") Integer id) {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    try {
        Query query = session.getNamedQuery("book.findByIdAndAuthor");
        query.setParameter("id", id);
        query.setParameter("authorId", authorId);
        Book book = (Book) query.uniqueResult();
        if (book == null) {
            throw new ObjectNotFoundException(id, "Book");
        }
        session.delete(book);
        tx.commit();
        return book;
    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
        session.close();
    }
}

@Override
@PUT
@Path("/authors/{authorId}/books/{id}")
@Consumes("application/json")
@Produces("application/json")
public Book updateBook(@PathParam("authorId") Integer authorId, @PathParam("id") Integer id, Book updatedBook) {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    try {
        Query query = session.getNamedQuery("book.findByIdAndAuthor");
        query.setParameter("id", id);
        query.setParameter("authorId", authorId);
        Book book = (Book) query.uniqueResult();
        if (book == null) {
            throw new ObjectNotFoundException(id, "Book");
        }
        book.setCopiesSold(updatedBook.getCopiesSold());
        book.setEdition(updatedBook.getEdition());
        book.setTitle(updatedBook.getTitle());
        tx.commit();
        return book;
    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
        session.close();
    }
}

@Override
@POST
@Path("/authors/{authorId}/books")
@Consumes("application/json")
@Produces("application/json")
public Book createBook(@PathParam("authorId") Integer authorId, Book model) {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    try {
        Author author = (Author) session.byId(Author.class)
                .getReference(authorId);
        model.setAuthor(author);
        session.persist(model);
        tx.commit();
        return model;
    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
        session.close();
    }
}
}
