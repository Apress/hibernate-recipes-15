package com.apress.hibernaterecipes.chapter14.services;

import com.apress.hibernaterecipes.chapter14.model.Author;
import com.apress.hibernaterecipes.chapter14.model.Book;
import com.apress.hibernaterecipes.web.SessionManager;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class ServiceTest {
    ServiceImpl controller = new ServiceImpl();

    @BeforeMethod
    public void setUp() {
        SessionManager.deleteAll("Book");
        SessionManager.deleteAll("Author");
    }

    @Test
    public void testGetAuthor() {
        Author data = createAuthor("Test Author");

        Author model = controller.getAuthor(data.getId());
        assertEquals(data, model);
    }

    @Test
    public void testGetAuthors() {
        Author[] authors = controller.getAuthors();
        assertEquals(authors.length, 0);
        createAuthor("First Author");
        createAuthor("Second Author");
        authors = controller.getAuthors();
        assertEquals(authors.length, 2);
    }

    @Test
    public void testUpdateAuthorExistingAuthor() {
        Author data = createAuthor("Test Author");
        data.setName("Other Author");
        Author model = controller.updateAuthor(data.getId(), data);
        assertEquals(model.getName(), "Other Author");
        assertEquals(model.getId(), data.getId());
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testUpdateAuthorNoExistingAuthor() {
        Author model = new Author();
        model.setName("My Test Author");

        model = controller.updateAuthor(15, model);
        assertEquals(model.getName(), "My Test Author");
        assertEquals(model.getId().intValue(), 15);

        Author data = controller.getAuthor(model.getId());
        assertEquals(model, data);
    }

    @Test
    public void testRemoveAuthor() {
        Author data = createAuthor("Test Author");
        Author removed = controller.removeAuthor(data.getId());
        assertEquals(data, removed);
        try {
            Author check = controller.getAuthor(removed.getId());
            fail("Should not have found author " + check);
        } catch (ObjectNotFoundException ignored) {
        }
    }

    @Test
    public void testCreateAuthor() {
        Author data = createAuthor("Test Author");
        assertEquals(data.getName(), "Test Author");
        assertNotNull(data.getId());
    }

    @Test(expectedExceptions = HibernateException.class)
    public void testDuplicateAuthor() {
        createAuthor("Foo Bar");
        createAuthor("Foo Bar");
        fail("Should not have been able to create authors with same name");
    }

    @Test
    public void createValidBook() {
        Author author = createAuthor("Arthur Author");
        Book book = createBook(author, "Otter Ought To", 1);
        assertNotNull(book);
        assertEquals(book.getTitle(), "Otter Ought To");
        assertNotNull(book.getId());
    }

    @Test(expectedExceptions=HibernateException.class)
    public void createDuplicateBook() {
        Author author = createAuthor("Arthur Author");
        createBook(author, "Otter Ought To", 1);
        createBook(author, "Otter Ought To", 1);
        fail("Should not have been able to create books with same name");
    }

    @Test(expectedExceptions = HibernateException.class)
    public void createBookWithInvalidAuthor() {
        Book model = new Book();
        model.setTitle("Arthur Author");
        model.setCopiesSold(0);
        model.setEdition(1);
        Book book = controller.createBook(15, model);
        assertNotNull(book);
        assertEquals(book.getTitle(), "Otter Ought To");
        assertNotNull(book.getId());
    }

    @Test
    public void testRemoveBook() {
        Author author = createAuthor("Arthur Author");
        Book book = createBook(author, "Otter Ought To", 1);
        Book persisted = controller.getBook(author.getId(), book.getId());
        assertEquals(book, persisted);
        controller.removeBook(author.getId(), book.getId());
        try {
            controller.getBook(author.getId(), book.getId());
            fail("Shouldn't have been able to get book " + book.getId());
        } catch (HibernateException ignored) {
        }
    }

    @Test(expectedExceptions = HibernateException.class)
    public void removeNonexistentBookWithValidAuthor() {
        Author author = createAuthor("Arthur Author");
        controller.removeBook(author.getId(), -1);
        fail("Should not have been able to remove book with invalid id");
    }

    @Test(expectedExceptions = HibernateException.class)
    public void removeNonexistentBookWithInvalidAuthor() {
        controller.removeBook(-4, -1);
        fail("Should not have been able to remove book with invalid author and id");
    }

    @Test(expectedExceptions = HibernateException.class)
    public void updateNonexistentAuthor() {
        Author author = createAuthor("Arthur Author");
        author.setName("Arthur Author II");
        controller.updateAuthor(-12, author);
        fail("Should not have been able to update nonexistent author");
    }

    @Test(expectedExceptions = HibernateException.class)
    public void removeNonExistentAuthor() {
        controller.removeAuthor(-12);
        fail("Should not have been able to remove nonexistent author");
    }

    @Test
    public void testGetBooks() {
        Author author = createAuthor("First Author");
        assertEquals(controller.getBooks(author.getId()).length, 0);
        createBook(author, "First Book", 1);
        assertEquals(controller.getBooks(author.getId()).length, 1);
        createBook(author, "Second Book", 1);
        assertEquals(controller.getBooks(author.getId()).length, 2);
    }

    @Test
    public void testUpdateBook() {
        Author author = createAuthor("Arthur Author");
        Book book = createBook(author, "Otter Ought To", 1);
        book.setCopiesSold(1000);
        controller.updateBook(author.getId(), book.getId(), book);
        Book updated = controller.getBook(author.getId(), book.getId());
        assertEquals(updated.getCopiesSold(), 1000);
    }

    @Test(expectedExceptions = HibernateException.class)
    public void testUpdateNonexistentBook() {
        Author author = createAuthor("Arthur Author");
        Book book = createBook(author, "Otter Ought To", 1);
        book.setCopiesSold(1000);
        controller.updateBook(author.getId(), -1, book);
        Book updated = controller.getBook(author.getId(), book.getId());
        assertEquals(updated.getCopiesSold(), 1000);
    }

    @Test
    public void testUpdateBookWithNewAuthor() {
        Author author = createAuthor("Arthur Author");
        Author author2 = createAuthor("Ronald Rhino");
        Book book = createBook(author, "Otter Ought To", 1);
        book.setAuthor(author2);
        controller.updateBook(author.getId(), book.getId(), book);
        Book updated = controller.getBook(author.getId(), book.getId());
        assertEquals(updated.getAuthor(), author);
    }

    private Book createBook(Author author, String title, int edition) {
        Book model = new Book();
        model.setTitle(title);
        model.setCopiesSold(0);
        model.setEdition(edition);
        return controller.createBook(author.getId(), model);
    }

    private Author createAuthor(String name) {
        Author model = new Author();
        model.setName(name);
        return controller.createAuthor(model);
    }
}