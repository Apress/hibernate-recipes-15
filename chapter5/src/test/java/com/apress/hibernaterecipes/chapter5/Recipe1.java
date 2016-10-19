package com.apress.hibernaterecipes.chapter5;

import com.apress.hibernaterecipes.chapter5.recipe1.Book1;
import com.apress.hibernaterecipes.chapter5.recipe1.ReaderErrata1;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class Recipe1 {
    @BeforeMethod
    public void cleanAll() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("delete from ReaderErrata1 ").executeUpdate();
        session.createQuery("delete from Book1").executeUpdate();
        tx.commit();
        session.close();
    }

    @Test
    public void persistExplicitGraph() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Book1 book1 = new Book1();
        book1.setTitle("Hibernate Recipes");
        session.persist(book1);
        ReaderErrata1 re = new ReaderErrata1();
        re.setBook(book1);
        re.setContent("First chapter is too short");
        session.persist(re);
        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();
        ReaderErrata1 re1 = (ReaderErrata1) session.byId(ReaderErrata1.class).load(re.getId());
        assertNotNull(re1);
        assertNotNull(re1.getBook());
        assertEquals(re1.getBook().getTitle(), book1.getTitle());
        tx.commit();
        session.close();
    }

    @Test
    public void persistImplicitGraph() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Book1 book1 = new Book1();
        book1.setTitle("Hibernate Recipes");
        ReaderErrata1 re = new ReaderErrata1();
        re.setBook(book1);
        re.setContent("First chapter is too short");
        session.persist(re);
        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();
        ReaderErrata1 re1 = (ReaderErrata1) session.byId(ReaderErrata1.class).load(re.getId());
        assertNotNull(re1);
        assertNotNull(re1.getBook());
        assertEquals(re1.getBook().getTitle(), book1.getTitle());
        tx.commit();
        session.close();
    }

    @Test
    public void deleteSingleErrata() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Book1 book1 = new Book1();
        book1.setTitle("Hibernate Recipes");
        ReaderErrata1 re = new ReaderErrata1();
        re.setBook(book1);
        re.setContent("First chapter is too short");
        session.persist(re);
        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();
        ReaderErrata1 re1 = (ReaderErrata1) session.byId(ReaderErrata1.class).load(re.getId());
        assertNotNull(re1);
        assertNotNull(re1.getBook());
        assertEquals(re1.getBook().getTitle(), book1.getTitle());
        session.delete(re1);
        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();
        List errata = session.createQuery("from ReaderErrata1 r").list();
        assertEquals(errata.size(), 0);
        List books = session.createQuery("from Book1 b").list();
        assertEquals(books.size(), 1);
        tx.commit();
        session.close();

    }


    @Test
    public void deleteErrata() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Book1 book1 = new Book1();
        book1.setTitle("Hibernate Recipes");
        ReaderErrata1 re = new ReaderErrata1();
        re.setBook(book1);
        re.setContent("First chapter is too short");
        session.persist(re);

        re = new ReaderErrata1();
        re.setBook(book1);
        re.setContent("Second chapter is too awesome");
        session.persist(re);

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();
        ReaderErrata1 re1 = (ReaderErrata1) session.byId(ReaderErrata1.class).load(re.getId());
        assertNotNull(re1);
        assertNotNull(re1.getBook());
        assertEquals(re1.getBook().getTitle(), book1.getTitle());
        session.delete(re1);
        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();
        List errata = session.createQuery("from ReaderErrata1 r").list();
        assertEquals(errata.size(), 1);
        List books = session.createQuery("from Book1 b").list();
        assertEquals(books.size(), 1);
        tx.commit();
        session.close();
    }
}
