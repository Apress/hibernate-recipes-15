package com.apress.hibernaterecipes.chapter12;

import com.apress.hibernaterecipes.chapter12.recipe5.Book5;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class Test5 {
    @BeforeMethod
    public void clear() {
        SessionManager.deleteAll("Book5");
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Book5 book5 = new Book5();
        book5.setTitle("My Book");
        session.persist(book5);
        tx.commit();
        session.close();
    }

    public List<Book5> runQuery(boolean cacheStatus) {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Book5 b where b.title like :title");
        query.setString("title", "My%");
        query.setCacheable(cacheStatus);
        List<Book5> books = (List<Book5>) query.list();
        tx.commit();
        session.close();
        return books;
    }

    @Test
    public void testNoQueryCache() {
        SessionFactory factory = SessionManager.getSessionFactory();
        Statistics stats = factory.getStatistics();
        stats.clear();
        assertEquals(runQuery(false).size(), 1);
        assertEquals(runQuery(false).size(), 1);
        assertEquals(stats.getQueryCacheHitCount(), 0);
        assertEquals(stats.getSecondLevelCacheHitCount(), 0);
    }

    @Test
    public void testQueryCache() {
        SessionFactory factory = SessionManager.getSessionFactory();
        Statistics stats = factory.getStatistics();
        stats.clear();
        assertEquals(runQuery(true).size(), 1);
        assertEquals(runQuery(true).size(), 1);
        assertEquals(stats.getQueryCacheHitCount(), 1);
        assertEquals(stats.getSecondLevelCacheHitCount(), 1);
    }
}
