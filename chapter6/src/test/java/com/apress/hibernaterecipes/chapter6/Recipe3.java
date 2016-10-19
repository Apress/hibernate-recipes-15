package com.apress.hibernaterecipes.chapter6;

import com.apress.hibernaterecipes.chapter6.recipe3.Book3;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class Recipe3 {


    @BeforeMethod
    public void clearAll() {
        SessionManager.deleteAll("Book3");
    }

    @Test
    public void alterElements() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Book3 book1 = new Book3();
        book1.setTitle("First Book");
        book1.getReviews().add("first review");
        book1.getReviews().add("second review");
        session.persist(book1);

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        Book3 book = (Book3) session
                .byId(Book3.class)
                .load(book1.getId());

        // let's make sure the order is what we expected
        assertEquals(book.getReviews().size(), 2);
        assertEquals(book.getReviews().get(0), "first review");
        assertEquals(book.getReviews().get(1), "second review");

        // insert a review between the two existing reviews
        book.getReviews().add(1, "another review");

        tx.commit();
        session.close();

        // forcibly lose the reference...
        // not necessary but let's be explicit
        book = null;

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        book = (Book3) session
                .byId(Book3.class)
                .load(book1.getId());

        assertEquals(book.getReviews().get(0), "first review");
        assertEquals(book.getReviews().get(1), "another review");
        assertEquals(book.getReviews().get(2), "second review");

        // remove the first, then insert a new one
        book.getReviews().remove(0);
        book.getReviews().add(1, "new review");

        tx.commit();
        session.close();

        // forcibly lose the reference...
        book = null;

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        book = (Book3) session
                .byId(Book3.class)
                .load(book1.getId());
        assertEquals(book.getReviews().get(0), "another review");
        assertEquals(book.getReviews().get(1), "new review");
        assertEquals(book.getReviews().get(2), "second review");

        tx.commit();
        session.close();
    }
}
