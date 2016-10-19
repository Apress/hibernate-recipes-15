package com.apress.hibernaterecipes.chapter13;

import com.apress.hibernaterecipes.chapter13.recipe3.Book3Version;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.LockOptions;
import org.hibernate.PessimisticLockException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Recipe4 {
  Book3Version b3v;

  @BeforeMethod
  public void clearData() {
    SessionManager.deleteAll("Book3Timestamp");
    SessionManager.deleteAll("Book3Version");
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    b3v = new Book3Version();
    b3v.setTitle("Book3V");
    b3v.setInventoryCount(100);
    session.persist(b3v);

    tx.commit();
    session.close();
  }

  @Test(expectedExceptions = PessimisticLockException.class)
  public void showPessimisticLocking() {
    Session session1 = SessionManager.openSession();
    Session session2 = SessionManager.openSession();
    Transaction tx1 = session1.beginTransaction();
    Transaction tx2 = session2.beginTransaction();
    try {
      Book3Version s1book3 = (Book3Version) session1.byId(Book3Version.class)
          .with(LockOptions.UPGRADE)
          .load(b3v.getId());

      Book3Version s2book3 = (Book3Version) session2.byId(Book3Version.class)
          .with(LockOptions.UPGRADE)
          .load(b3v.getId());

    } finally {
      session1.close();
      session2.close();
    }
  }
}
