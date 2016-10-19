package com.apress.hibernaterecipes.chapter13;

import com.apress.hibernaterecipes.chapter13.recipe1.Book1;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Recipe1 {
  @BeforeMethod
  public void clearData() {
    SessionManager.deleteAll("Book1");
  }

  @Test
  public void testConnection() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    Book1 book1 = new Book1();
    book1.setTitle("The Dog Barker");
    session.persist(book1);
    tx.commit();
    session.close();
  }

  @Test
  public void testTransactionWithExceptionHandling() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    try {
      Book1 book1 = new Book1();
      book1.setTitle("The Dog Barker");
      session.persist(book1);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      session.close();
    }
  }
}
