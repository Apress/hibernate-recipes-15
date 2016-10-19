package com.apress.hibernaterecipes.chapter6;

import com.apress.hibernaterecipes.chapter6.recipe5.Book5;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Recipe5 {
    @BeforeMethod
    public void clearAll() {
        SessionManager.deleteAll("Book5");
    }

    @Test
    public void testMappedElements() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Book5 book = new Book5();
        book.setTitle("My World of Stuff");
        book.getTopicMap().put("language", "chapter1");
        book.getTopicMap().put("math", "chapter2");
        book.getTopicMap().put("spellling", "chapter3");
        session.persist(book);

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        Book5 book2 = (Book5) session
                .byId(Book5.class)
                .load(book.getId());

        assertTrue(book2.getTopicMap().containsKey("spellling"));
        assertEquals(book2.getTopicMap().get("spellling"), "chapter3");
        book2.getTopicMap().put("reading", "chapter orange");

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        book2 = (Book5) session
                .byId(Book5.class)
                .load(book.getId());

        assertTrue(book2.getTopicMap().containsKey("reading"));
        assertEquals(book2.getTopicMap().get("reading"), "chapter orange");

        tx.commit();
        session.close();
    }
}
