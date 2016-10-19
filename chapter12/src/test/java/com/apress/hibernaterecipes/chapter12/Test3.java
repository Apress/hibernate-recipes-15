package com.apress.hibernaterecipes.chapter12;

import com.apress.hibernaterecipes.chapter12.recipe3.Book3;
import com.apress.hibernaterecipes.chapter12.recipe3.Publisher3;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class Test3 {
    @BeforeMethod
    public void clearData() {
        SessionManager.deleteAll("Book3");
        SessionManager.deleteAll("Publisher3");
    }

    @Test
    public void testAssociationCache() {
        SessionFactory sessionFactory = SessionManager.getSessionFactory();
        Statistics stats = sessionFactory.getStatistics();
        stats.clear();

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Publisher3 publisher3 = new Publisher3();
        publisher3.setName("My Publisher");
        session.persist(publisher3);

        Book3 book3 = new Book3();
        book3.setTitle("My Title");
        book3.setPublisher(publisher3);

        session.persist(book3);

        tx.commit();
        session.close();

        sessionFactory.getCache().evictAllRegions();

        session = sessionFactory.openSession();
        tx = session.beginTransaction();

        Book3 b = (Book3) session.byId(Book3.class).load(book3.getId());
        Book3 b2 = (Book3) session.byId(Book3.class).load(book3.getId());
        Publisher3 p = b.getPublisher();

        assertEquals(b, b2);
        assertEquals(b, book3);
        assertEquals(p, publisher3);

        tx.commit();
        session.close();

        assertEquals(stats.getSecondLevelCacheHitCount(), 0);
        assertEquals(stats.getSecondLevelCacheMissCount(), 1);
        assertEquals(stats.getSecondLevelCachePutCount(), 2);

        session = sessionFactory.openSession();
        tx = session.beginTransaction();

        b = (Book3) session.byId(Book3.class).load(book3.getId());
        b2 = (Book3) session.byId(Book3.class).load(book3.getId());
        p = b.getPublisher();

        assertEquals(b, b2);
        assertEquals(b, book3);
        assertEquals(p, publisher3);

        tx.commit();
        session.close();

        // our values accumulate, because it's the same session factory
        assertEquals(stats.getSecondLevelCacheHitCount(), 2);
        assertEquals(stats.getSecondLevelCacheMissCount(), 1);
        assertEquals(stats.getSecondLevelCachePutCount(), 2);
    }
}
