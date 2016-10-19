package com.apress.hibernaterecipes.chapter6;

import com.apress.hibernaterecipes.chapter6.recipe7.Book7;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class Recipe7 {
    @BeforeMethod
    public void clearAll() {
        SessionManager.deleteAll("Book7");
    }

    @Test(expectedExceptions = LazyInitializationException.class)
    public void testLazyInitialization() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Book7 book7 = new Book7();
        book7.setTitle("Book Title");
        book7.getReviews().add("first");
        book7.getReviews().add("second");

        session.persist(book7);

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        Book7 book = (Book7) session
                .byId(Book7.class)
                .load(book7.getId());

        tx.commit();
        session.close();

        // this should throw an exception.
        assertEquals(book.getReviews().size(), 2);
    }

    @Test
    public void testExplicitInitialization() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Book7 book7 = new Book7();
        book7.setTitle("Book Title");
        book7.getReviews().add("first");
        book7.getReviews().add("second");

        session.persist(book7);

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        Book7 book = (Book7) session
                .byId(Book7.class)
                .load(book7.getId());

        Hibernate.initialize(book.getReviews());

        tx.commit();
        session.close();

        assertEquals(book.getReviews().size(), 2);
    }

    @Test
    public void testExplicitUsage() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Book7 book7 = new Book7();
        book7.setTitle("Book Title");
        book7.getReviews().add("first");
        book7.getReviews().add("second");

        session.persist(book7);

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        Book7 book = (Book7) session
                .byId(Book7.class)
                .load(book7.getId());

        assertEquals(book.getReviews().size(), 2);

        tx.commit();
        session.close();
    }

    ThreadLocal<Session> session = new ThreadLocal<>();
    ThreadLocal<Transaction> tx = new ThreadLocal<>();

    @Test
    public void testSessionInThreadLocal() throws Exception {
        session.set(SessionManager.openSession());
        tx.set(session.get().beginTransaction());

        Book7 book7 = addBook("Book Title");
        Book7 book = getBook(book7.getId());

        assertEquals(book.getTitle(), book7.getTitle());

        tx.get().commit();
        tx.set(null);
        session.get().close();
        session.set(null);
    }

    private Book7 getBook(int id) throws Exception {
        if (session.get() == null) {
            throw new Exception("No Session ThreadLocal initialized");
        }
        Book7 book = (Book7) session.get()
                .byId(Book7.class)
                .load(id);
        return book;
    }

    private Book7 addBook(String title) throws Exception {
        if (session.get() == null) {
            throw new Exception("No Session ThreadLocal initialized");
        }
        Book7 book = new Book7();
        book.setTitle(title);
        session.get().persist(book);
        return book;
    }
}
