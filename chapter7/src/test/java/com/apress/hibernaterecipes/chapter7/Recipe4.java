package com.apress.hibernaterecipes.chapter7;

import com.apress.hibernaterecipes.chapter7.recipe4.Book4;
import com.apress.hibernaterecipes.chapter7.recipe4.Chapter4;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class Recipe4 {
    @BeforeMethod
    public void clearAll() {
        SessionManager.deleteAll("Chapter4");
        SessionManager.deleteAll("Book4");
    }

    @Test
    public void testManyToMany() {
        // book, chapter(s)
        int[][] chapterMatrix = new int[][]{{1, 1, 2, 3},
                {2, 4, 5},
                {3, 1, 3, 5},
                {4, 2, 4},
                {5, 1, 2, 4, 5}
        };
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        for (int i = 1; i < 6; i++) {
            Chapter4 c = new Chapter4();
            c.setTitle("title " + i);
            c.setContent("content " + i);

            Book4 b = new Book4();
            b.setTitle("book " + i);

            session.persist(b);
            session.persist(c);
        }
        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();
        Query bookQuery = session.getNamedQuery("Book4.findByTitle");
        Query chapterQuery = session.getNamedQuery("Chapter4.findByTitle");
        for (int[] matrix : chapterMatrix) {
            int bookRef = matrix[0];
            bookQuery.setParameter("title", "book " + bookRef);
            Book4 book = (Book4) bookQuery.uniqueResult();

            assertNotNull(book);

            for (int chapterIndex = 1; chapterIndex < matrix.length; chapterIndex++) {
                chapterQuery.setParameter("title", "title " + matrix[chapterIndex]);
                Chapter4 chapter = (Chapter4) chapterQuery.uniqueResult();

                assertNotNull(chapter);

                book.getChapters().add(chapter);
                chapter.getBooks().add(book);
            }
        }
        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();
        bookQuery = session.getNamedQuery("Book4.findByTitle");
        bookQuery.setParameter("title", "book 1");

        Book4 book = (Book4) bookQuery.uniqueResult();

        assertNotNull(book);
        assertNotNull(book.getChapters());
        assertEquals(book.getChapters().size(), 3);

        tx.commit();
        session.close();
    }
}
