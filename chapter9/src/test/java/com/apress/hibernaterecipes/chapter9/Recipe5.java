package com.apress.hibernaterecipes.chapter9;

import com.apress.hibernaterecipes.chapter9.recipe1.Book5;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class Recipe5 {
  @BeforeMethod
  public void clear() {
    SessionManager.deleteAll("Book5");

    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    for (int i = 1; i < 6; i++) {
      Book5 book = new Book5();
      book.setName("Book " + i);
      book.setPublisher("Publisher " + (i % 2 + 1));
      book.setPrice(10.0 + i * 2);
      session.persist(book);
    }
    tx.commit();
    session.close();
  }

  @Test
  public void testQueryByExample() {

    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();

    Book5 book5 = new Book5();
    book5.setName("Book 1");
    Example example = Example.create(book5);
    Criteria criteria = session.createCriteria(Book5.class)
        .add(example);
    List<Book5> books = criteria.list();
    assertEquals(books.size(), 1);
    assertEquals(books.get(0).getPublisher(), "Publisher 2");
    assertEquals(books.get(0).getPrice(), 12.0, 0.01);
    tx.commit();
    session.close();
  }

  @Test
  public void testQueryByExampleLike() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();

    Book5 book5 = new Book5();

    book5.setName("Book 1");
    book5.setPublisher("Publisher%");
    book5.setPrice(22.0);

    Example example = Example.create(book5)
        .enableLike()
        .excludeProperty("price");
    Criteria criteria = session.createCriteria(Book5.class)
        .add(example);
    List<Book5> books = criteria.list();
    assertEquals(books.size(), 1);
    assertEquals(books.get(0).getPublisher(), "Publisher 2");

    tx.commit();
    session.close();
  }

}
