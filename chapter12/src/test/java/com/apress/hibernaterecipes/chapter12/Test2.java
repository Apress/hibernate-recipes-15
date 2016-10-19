package com.apress.hibernaterecipes.chapter12;

import com.apress.hibernaterecipes.chapter12.recipe2.Book2;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class Test2 {

    @BeforeMethod
    public void clearData() {
        SessionManager.deleteAll("Book2");
    }

    @Test
    public void test2LCache() {
        SessionFactory sessionFactory = SessionManager.getSessionFactory();
        Statistics stats = sessionFactory.getStatistics();
        stats.clear();

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Book2 book2 = new Book2();
        book2.setTitle("My Title");

        session.persist(book2);

        tx.commit();
        session.close();

        sessionFactory.getCache().evictAllRegions();

        session = sessionFactory.openSession();
        tx = session.beginTransaction();

        Book2 b = (Book2) session.byId(Book2.class).load(book2.getId());
        Book2 b2 = (Book2) session.byId(Book2.class).load(book2.getId());

        assertEquals(book2, b);
        assertEquals(book2, b2);

        tx.commit();
        session.close();

        // this is the initial select
        assertEquals(stats.getSecondLevelCacheMissCount(), 1);
        // we put one element in the cache from the miss
        assertEquals(stats.getSecondLevelCachePutCount(), 1);
        // we still didn't hit the cache, because of 1L cache
        assertEquals(stats.getSecondLevelCacheHitCount(), 0);

        session = sessionFactory.openSession();
        tx = session.beginTransaction();

        b = (Book2) session.byId(Book2.class).load(book2.getId());
        assertEquals(book2, b);
        tx.commit();
        session.close();

        // same miss count (we should hit now)
        assertEquals(stats.getSecondLevelCacheMissCount(), 1);
        // same put count (we didn't put anything new)
        assertEquals(stats.getSecondLevelCachePutCount(), 1);
        // now we hit the 2L cache for load
        assertEquals(stats.getSecondLevelCacheHitCount(), 1);
    }

    @Test(expectedExceptions = {UnsupportedOperationException.class})
    public void updateReadOnly() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Book2 book2 = new Book2();
        book2.setTitle("My Title");

        session.persist(book2);

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        try {
            tx = session.beginTransaction();

            Book2 b = (Book2) session.byId(Book2.class).load(book2.getId());
            b.setTitle("The Revised Title");
            session.flush();
        } catch (UnsupportedOperationException e) {
            tx.rollback();
            session.close();
            throw e;
        }
        tx.commit();
        session.close();
        fail("Should have gotten an exception");
    }
}
