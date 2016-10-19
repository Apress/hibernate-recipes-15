package com.apress.hibernaterecipes.chapter13;

import com.apress.hibernaterecipes.chapter13.recipe3.Book3Timestamp;
import com.apress.hibernaterecipes.chapter13.recipe3.Book3Version;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Recipe3 {
  Book3Version b3v;

  @BeforeMethod
  public void clearData() {
    SessionManager.deleteAll("Book3Timestamp");
    SessionManager.deleteAll("Book3Version");
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    Book3Timestamp b3t = new Book3Timestamp();
    b3t.setTitle("Book3T");
    session.persist(b3t);

    b3v = new Book3Version();
    b3v.setTitle("Book3V");
    b3v.setInventoryCount(100);
    session.persist(b3v);

    tx.commit();
    session.close();
  }

  @Test(expectedExceptions = {StaleObjectStateException.class})
  public void showOptimisticLocking() {
    Session session1 = SessionManager.openSession();
    Session session2 = SessionManager.openSession();
    Transaction tx1 = session1.beginTransaction();
    Transaction tx2 = session2.beginTransaction();
    try {
      Book3Version s1book3 = (Book3Version) session1.byId(Book3Version.class).load(b3v.getId());

      Book3Version s2book3 = (Book3Version) session2.byId(Book3Version.class).load(b3v.getId());

      s1book3.setInventoryCount(s1book3.getInventoryCount() - 1);

      tx1.commit();

      s2book3.setInventoryCount(s2book3.getInventoryCount() + 60);

      tx2.commit();
    } finally {
      session1.close();
      session2.close();
    }
  }
}
