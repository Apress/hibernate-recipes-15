package com.apress.hibernaterecipes.chapter11;

import com.apress.hibernaterecipes.chapter11.recipe2.Book2;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class Recipe2 {
  @BeforeMethod
  public void clearAll() {
    SessionManager.deleteAll("Book2");
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    for (int i = 0; i < 1000; i++) {
      Book2 book2 = new Book2();
      book2.setTitle("Hibernate " + i);
      book2.setPrice(39.99);
      session.persist(book2);
    }
    tx.commit();
    session.close();
  }

  @Test
  public void testNoBatchUpdate() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    Query query = session.getNamedQuery("Book2.findLikeTitle");
    query.setParameter("title", "%Hibernate%");
    List<Book2> books = query.list();
    for (Book2 book : books) {
      book.setPrice(book.getPrice() - 10.0);
    }
    tx.commit();
    session.close();

    session = SessionManager.openSession();
    tx = session.beginTransaction();
    query = session.getNamedQuery("Book2.findLikeTitle");
    query.setParameter("title", "%Hibernate%");
    books = query.list();
    for (Book2 book : books) {
      assertEquals(book.getPrice(), 29.99, 0.001);
    }
    tx.commit();
    session.close();
  }

  @Test
  public void testBatchUpdate() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    Query query = session.getNamedQuery("Book2.findLikeTitle")
        .setCacheMode(CacheMode.IGNORE);

    query.setParameter("title", "%Hibernate%");
    ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
    int counter = 0;
    while (results.next()) {
      Book2 book = (Book2) results.get(0);
      book.setPrice(book.getPrice() - 10.0);
      counter++;
      if (counter == 25) {
        session.flush();
        session.clear();
        counter = 0;
      }
    }
    tx.commit();
    session.close();

    session = SessionManager.openSession();
    tx = session.beginTransaction();
    query = session.getNamedQuery("Book2.findLikeTitle");
    query.setParameter("title", "%Hibernate%");
    List<Book2> books = query.list();
    for (Book2 book : books) {
      assertEquals(book.getPrice(), 29.99, 0.001, book.toString());
    }
    tx.commit();
    session.close();
  }

  @Test
  public void testSQLUpdate() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    Query query = session.createQuery(
        "update Book2 b set b.price=b.price-:discount where b.title like :title");
    query.setParameter("discount", 10.0);
    query.setParameter("title", "%Hibernate%");
    int count = query.executeUpdate();
    assertEquals(count, 1000);
    tx.commit();
    session.close();

    session = SessionManager.openSession();
    tx = session.beginTransaction();
    query = session.getNamedQuery("Book2.findLikeTitle");
    query.setParameter("title", "%Hibernate%");
    List<Book2> books = query.list();
    for (Book2 book : books) {
      assertEquals(book.getPrice(), 29.99, 0.001, book.toString());
    }
    tx.commit();
    session.close();
  }
}
