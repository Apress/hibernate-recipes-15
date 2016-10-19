package com.apress.hibernaterecipes.chapter7;

import com.apress.hibernaterecipes.chapter7.recipe2.Book2;
import com.apress.hibernaterecipes.chapter7.recipe2.Chapter2;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class Recipe2 {
    @BeforeMethod
    public void clearAll() {
        SessionManager.deleteAll("Book2");
    }

    @Test
    public void bidirectionalOneToMany() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Book2 book2 = new Book2();
        book2.setTitle("First title");

        Chapter2 chapter2 = new Chapter2();
        chapter2.setTitle("first chapter");
        chapter2.setContent("here's some text");
        chapter2.setBook2(book2);
        book2.getChapters().add(chapter2);

        session.save(book2);

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        Book2 book = (Book2) session
                .byId(Book2.class)
                .load(book2.getId());

        assertEquals(book.getTitle(), book2.getTitle());
        assertEquals(book.getChapters().size(),
                book2.getChapters().size());

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        book = (Book2) session
                .byId(Book2.class)
                .load(book2.getId());

        session.delete(book);

        tx.commit();
        session.close();
    }

    @Test
    public void findBookGivenChapter() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Book2 book2 = new Book2();
        book2.setTitle("First title");

        Chapter2 chapter2 = new Chapter2();
        chapter2.setTitle("first chapter");
        chapter2.setContent("here's some text");
        chapter2.setBook2(book2);

        book2.getChapters().add(chapter2);
        session.save(book2);

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();

        Chapter2 chapter = (Chapter2) session.byId(Chapter2.class).load(chapter2.getId());
        assertNotNull(chapter);

        // if we need the book, we have it!
        Book2 book = chapter.getBook2();
        assertEquals(book.getTitle(), book2.getTitle());

        tx.commit();
        session.close();
    }


}
