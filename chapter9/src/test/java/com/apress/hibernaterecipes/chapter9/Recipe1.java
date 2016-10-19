package com.apress.hibernaterecipes.chapter9;

import com.apress.hibernaterecipes.chapter9.recipe1.Book1;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class Recipe1 {
  @BeforeMethod
  public void clear() {
    SessionManager.deleteAll("Book1");

    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    for (int i = 1; i < 6; i++) {
      Book1 book = new Book1();
      book.setName("Book " + i);
      book.setPublisher("Publisher " + (i % 2 + 1));
      session.persist(book);
    }
    tx.commit();
    session.close();
  }

  @Test
  public void testSimpleCriteria() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    List<Book1> books = session.createQuery("from Book1 b").list();
    assertEquals(books.size(), 5);

    Criteria criteria = session.createCriteria(Book1.class);
    List<Book1> booksFromCriteria = criteria.list();
    assertEquals(booksFromCriteria.size(), 5);

    tx.commit();
    session.close();
  }

  @Test
  public void testSimpleExpression() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();

    Query query = session.createQuery("from Book1 b where b.name=:name");
    query.setParameter("name", "Book 1");
    List<Book1> books = query.list();
    assertEquals(books.size(), 1);

    Criteria criteria = session.createCriteria(Book1.class)
        .add(Restrictions.eq("name", "Book 1"));
    List<Book1> booksFromCriteria = criteria.list();
    assertEquals(booksFromCriteria.size(), 1);

    tx.commit();
    session.close();
  }

  @Test
  public void testDetachedCriteria() {
    DetachedCriteria criteria = DetachedCriteria
        .forClass(Book1.class)
        .add(Restrictions.eq("name", "Book 1"));

    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();

    List<Book1> booksFromCriteria = criteria
        .getExecutableCriteria(session)
        .list();
    assertEquals(booksFromCriteria.size(), 1);

    tx.commit();
    session.close();
  }

  @DataProvider
  Object[][] variedData() {
    return new Object[][]{
        {"Book 1", null, 1},
        {null, "Publisher 2", 3},
        {"Book 1", "Publisher 2", 1},
        {null, null, 5}
    };
  }

  @Test(dataProvider = "variedData")
  public void testRestrictionsCriteria(String name, String publisher, int count) {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();

    Criteria criteria = session.createCriteria(Book1.class);
    if (name != null) {
      criteria.add(Restrictions.eq("name", name));
    }
    if (publisher != null) {
      criteria.add(Restrictions.eq("publisher", publisher));
    }
    List<Book1> books = criteria.list();
    assertEquals(books.size(), count);

    tx.commit();
    session.close();
  }

  @Test(dataProvider = "variedData")
  public void testRestrictionsHQL(String name, String publisher, int count) {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    StringBuilder queryText = new StringBuilder("from Book1 b ");

    if (name != null) {
      queryText.append("where b.name=:name ");
    }
    if (publisher != null) {
      if (name == null) {
        queryText.append("where ");
      } else {
        queryText.append("and ");
      }
      queryText.append("b.publisher=:publisher ");
    }

    Query query = session.createQuery(queryText.toString());
    if (name != null) {
      query.setParameter("name", name);
    }
    if (publisher != null) {
      query.setParameter("publisher", publisher);
    }

    List<Book1> books = query.list();
    assertEquals(books.size(), count);

    tx.commit();
    session.close();
  }
}
