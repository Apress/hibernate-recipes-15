package com.apress.hibernaterecipes.chapter8;

import com.apress.hibernaterecipes.chapter8.recipe2.Book2;
import com.apress.hibernaterecipes.chapter8.recipe2.Publisher2;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Recipe2 {
    String[][] data = new String[][]{
            {"Book1", "Apress"},
            {"Book2", "Apress"},
            {"Book3", "Springer"},
            {"Book4", "Apress"},
            {"Book5", "Springer"},
    };

    @BeforeMethod
    public void setup() {
        SessionManager.deleteAll("Book2");
        SessionManager.deleteAll("Publisher2");

        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Query publisherQuery = session.getNamedQuery("Publisher2.byName");
        Query bookQuery = session.getNamedQuery("Book2.byName");
        for (String[] datum : data) {
            publisherQuery.setParameter("name", datum[1]);
            Publisher2 publisher2 = (Publisher2) publisherQuery.uniqueResult();
            if (null == publisher2) {
                publisher2 = new Publisher2(datum[1]);
                session.persist(publisher2);
            }
            bookQuery.setParameter("name", datum[0]);
            Book2 book2 = (Book2) bookQuery.uniqueResult();
            if (null == book2) {
                book2 = new Book2(datum[0]);
                book2.setPublisher2(publisher2);
                session.persist(book2);
            }
        }
        tx.commit();
        session.close();
    }

    @Test
    public void distinctQueries() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("select b.publisher2.name from Book2 b");
        List results = query.list();
        assertEquals(results.size(), 5);

        // now let's apply distinctness
        query = session.createQuery("select distinct b.publisher2.name from Book2 b");
        results = query.list();
        assertEquals(results.size(), 2);

        tx.commit();
        session.close();
    }

    @Test
    public void testObjectProjection() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("select " +
                "b.name, b.publisher2.name from Book2 b");
        List<Object[]> results = query.list();
        assertEquals(results.size(), 5);
        for (Object[] o : results) {
            assertEquals(o.length, 2);
        }

        tx.commit();
        session.close();
    }

    @Test
    public void testSimpleTypeProjection() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("select new " +
                "com.apress.hibernaterecipes.chapter8.recipe2.BookSummary2" +
                "(b.name, b.publisher2.name) from Book2 b");
        List results = query.list();
        assertEquals(results.size(), 5);

        tx.commit();
        session.close();
    }

    @Test
    public void testListProjection() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("select new " +
                "list(b.name, b.publisher2.name) from Book2 b");
        List<List> results = query.list();
        assertEquals(results.size(), 5);
        for (List l : results) {
            assertEquals(l.size(), 2);
        }
        tx.commit();
        session.close();
    }

    @Test
    public void testMapProjection() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("select new " +
                "map(b.name as name, b.publisher2.name as publisher)" +
                " from Book2 b");
        List<Map> results = query.list();
        assertEquals(results.size(), 5);
        for (Map m : results) {
            assertTrue(m.containsKey("name"));
            assertTrue(m.containsKey("publisher"));
        }
        tx.commit();
        session.close();
    }
}
