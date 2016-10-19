package com.apress.hibernaterecipes.chapter10;

import com.apress.hibernaterecipes.chapter10.recipe5.AuditableInterceptor;
import com.apress.hibernaterecipes.chapter10.recipe5.Book5;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class Recipe5 {

  @BeforeMethod
  public void setup() {
    SessionManager.deleteAll("Book5");
  }

  @Test
  public void testInterceptorOnSave() {
    Session session = SessionManager.getSessionFactory()
        .withOptions().interceptor(new AuditableInterceptor())
        .openSession();
    Transaction tx = session.beginTransaction();
    Book5 book5 = new Book5("Angsty Dead People", 1);
    assertNull(book5.getCreateDate());
    session.save(book5);
    tx.commit();
    session.close();

    assertNotNull(book5.getCreateDate());
  }
}
