package com.apress.hibernaterecipes.chapter6;

import com.apress.hibernaterecipes.chapter6.recipe4.Book4;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class Recipe4 {
    @BeforeMethod
    public void clearAll() {
        SessionManager.deleteAll("Book4");
    }
    @Test
    public void testArray() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Book4 book = new Book4();
        book.setTitle("First book");

        String[] reviews = new String[]{
                "This book is great",
                "This book is light on content"
        };

        book.setReviews(reviews);

        session.persist(book);
        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        Book4 book1 = (Book4) session
                .byId(Book4.class)
                .load(book.getId());
        assertEquals(book1.getReviews().length, 2);

        tx.commit();
        session.close();
    }
}
