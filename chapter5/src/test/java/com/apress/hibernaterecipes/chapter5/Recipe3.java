package com.apress.hibernaterecipes.chapter5;

import com.apress.hibernaterecipes.chapter5.recipe3.Book3;
import com.apress.hibernaterecipes.chapter5.recipe3.ReaderErrata3Eager;
import com.apress.hibernaterecipes.chapter5.recipe3.ReaderErrata3Lazy;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class Recipe3 {
    long eagerKey;
    long lazyKey;

    @BeforeMethod
    public void initialize() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        session.createQuery("delete from ReaderErrata3Eager").executeUpdate();
        session.createQuery("delete from ReaderErrata3Lazy").executeUpdate();
        session.createQuery("delete from Book1").executeUpdate();

        Book3 book3 = new Book3();
        book3.setTitle("The Dog in the Fog");
        session.persist(book3);

        ReaderErrata3Eager eager = new ReaderErrata3Eager();
        eager.setContent("Test eager data");
        eager.setBook(book3);
        session.save(eager);
        eagerKey = eager.getId();

        ReaderErrata3Lazy lazy = new ReaderErrata3Lazy();
        lazy.setContent("Test lazy data");
        lazy.setBook(book3);
        session.save(lazy);
        lazyKey = lazy.getId();

        tx.commit();
        session.close();
    }

    @Test
    public void testEager() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        ReaderErrata3Eager eager = (ReaderErrata3Eager) session
                .byId(ReaderErrata3Eager.class)
                .load(eagerKey);
        tx.commit();
        session.close();
        assertEquals(eager.getBook().getTitle(), "The Dog in the Fog");
    }

    @Test(expectedExceptions = LazyInitializationException.class)
    public void testLazy() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        ReaderErrata3Lazy lazy = (ReaderErrata3Lazy) session
                .byId(ReaderErrata3Lazy.class)
                .load(lazyKey);
        tx.commit();
        session.close();
        assertEquals(lazy.getBook().getTitle(), "The Dog in the Fog");
    }

    @Test
    public void testLazyWorking() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        ReaderErrata3Lazy lazy = (ReaderErrata3Lazy) session
                .byId(ReaderErrata3Lazy.class)
                .load(lazyKey);
        Hibernate.initialize(lazy.getBook());
        tx.commit();
        session.close();
        assertEquals(lazy.getBook().getTitle(), "The Dog in the Fog");
    }

    @Test
    public void testLazyChangeFetchMode() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        session.enableFetchProfile("errata-with-book");
        ReaderErrata3Lazy lazy = (ReaderErrata3Lazy) session
                .byId(ReaderErrata3Lazy.class)
                .load(lazyKey);
        tx.commit();
        session.close();
        assertEquals(lazy.getBook().getTitle(), "The Dog in the Fog");
    }
}
