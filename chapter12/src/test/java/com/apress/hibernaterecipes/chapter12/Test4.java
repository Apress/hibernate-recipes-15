package com.apress.hibernaterecipes.chapter12;

import com.apress.hibernaterecipes.chapter12.recipe4.Book4;
import com.apress.hibernaterecipes.chapter12.recipe4.Chapter4;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class Test4 {
    Book4 book4;

    @BeforeMethod
    public void clear() {
        SessionManager.deleteAll("Chapter4");
        SessionManager.deleteAll("Book4");

        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        book4 = new Book4();
        book4.setTitle("sample book");
        book4.getChapters().add(new Chapter4("chapter one"));
        book4.getChapters().add(new Chapter4("chapter two"));
        session.persist(book4);
        tx.commit();
        session.close();
    }

    @Test
    public void testCollectionCache() {
        SessionFactory sessionFactory = SessionManager.getSessionFactory();
        Statistics stats = sessionFactory.getStatistics();
        stats.clear();

        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Book4 book = (Book4) session.byId(Book4.class).load(book4.getId());
        assertEquals(book.getTitle(), book4.getTitle());
        assertEquals(book.getChapters().size(), 2);
        tx.commit();
        session.close();
        assertEquals(stats.getSecondLevelCacheHitCount(), 0);
        assertEquals(stats.getSecondLevelCacheMissCount(), 2);
        // one book, two chapters, one collection
        assertEquals(stats.getSecondLevelCachePutCount(), 4);

        session = SessionManager.openSession();
        tx = session.beginTransaction();
        book = (Book4) session.byId(Book4.class).load(book4.getId());
        assertEquals(book.getTitle(), book4.getTitle());
        assertEquals(book.getChapters().size(), 2);
        tx.commit();
        session.close();

        // should hit the book, chapters, collection now
        assertEquals(stats.getSecondLevelCacheHitCount(), 4);
        assertEquals(stats.getSecondLevelCacheMissCount(), 2);
        // one book, two chapters, one collection
        assertEquals(stats.getSecondLevelCachePutCount(), 4);
    }
}
