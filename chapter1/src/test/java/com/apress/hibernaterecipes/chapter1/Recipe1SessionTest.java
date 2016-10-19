package com.apress.hibernaterecipes.chapter1;

import com.apress.hibernaterecipes.chapter1.model.Book;
import com.apress.hibernaterecipes.chapter1.model.Publisher;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Date;

import static org.testng.Assert.*;

public class Recipe1SessionTest {
  final static Logger logger = LoggerFactory.getLogger(Recipe1SessionTest.class);

  /**
   * This method simply iterates through the entity types, calling
   * a utility method that removes all available entries.
   * <p/>
   * It is meant to be called before each test method, so that the
   * tests have a blank slate with which to work.
   */
  @BeforeMethod
  public void clearData() {
    Session session = SessionManager.getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    clearAll(session, "from Book b");
    clearAll(session, "from Publisher p");
    tx.commit();
    session.close();
  }

  /**
   * This method executes an HQL delete with a constraint.
   *
   * @param session    the active session in which the delete occurs
   * @param constraint the constraint for the delete statement
   */
  private void clearAll(Session session, String constraint) {
    Query query = session.createQuery("delete " + constraint);
    query.executeUpdate();
  }

  /**
   * This test verifies creation of a single Publisher.
   * <p/>
   * It does so in two phases: it first persists a single Publisher, then
   * reads a Publisher with the same identifier, comparing a significant
   * field to make sure we have read an object similar to the one we
   * created.
   * <p/>
   * To read the object, it uses
   * {@link org.hibernate.Session#load(Class, java.io.Serializable)},
   * which will throw an {@link org.hibernate.ObjectNotFoundException}
   * if the entity is not found. In this test, this exception is a
   * failure condition, so we aren't worried about trapping it.
   */
  @Test
  public void testCreate() {
    Session session = SessionManager.getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    Publisher publisher = new Publisher();
    publisher.setCode("apress");
    publisher.setName("Apress");
    publisher.setAddress("233 Spring Street, New York, NY 10013");
    session.persist(publisher);
    tx.commit();
    session.close();

    session = SessionManager.getSessionFactory().openSession();
    tx = session.beginTransaction();
    Publisher publisher1 = (Publisher) session.load(Publisher.class, "apress");
    assertEquals(publisher.getName(), publisher1.getName());
    tx.commit();
    session.close();
  }

  /**
   * This method demonstrates the persistence of object graphs.
   * <p/>
   * It does so by creating a Publisher, and then a Book, setting
   * the Book's publisher to the Publisher we just created. Since
   * Book's mapping file says that the publisher reference cascades
   * all operations, we only need to persist Book; Publisher will
   * automatically be created in the database if necessary.
   * <p/>
   * It tests the persistence by reading the Book from the database
   * and checking significant field references.
   * <p/>
   * Lastly, it repeats the process with the same Publisher, but a
   * different book, demonstrating the cascaded operation as working
   * properly when used with prior existing entities.
   * <p/>
   * Note that {@link org.hibernate.Session#refresh(Object)} is used
   * to tell Hibernate to use the publisher with this information
   * from the database.
   */
  @Test
  public void testCreateObjectGraph() {
    Session session = SessionManager.getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    Publisher publisher = new Publisher();
    publisher.setCode("apress");
    publisher.setName("Apress");
    publisher.setAddress("233 Spring Street, New York, NY 10013");

    Book book = new Book();
    book.setIsbn("9781484201282");
    book.setName("Hibernate Recipes");
    book.setPrice(new BigDecimal("44.00"));
    book.setPublishdate(Date.valueOf("2014-10-10"));
    book.setPublisher(publisher);

    session.persist(book);

    tx.commit();
    session.close();

    session = SessionManager.getSessionFactory().openSession();
    tx = session.beginTransaction();
    Book book1 = (Book) session.get(Book.class, "9781484201282");
    assertEquals(book.getName(), book1.getName());
    assertNotNull(book.getPublisher());
    assertEquals(book.getPublisher().getName(), publisher.getName());
    tx.commit();
    session.close();

    session = SessionManager.getSessionFactory().openSession();
    tx = session.beginTransaction();

    // this changes the publisher back to managed state, so
    // Hibernate won't try to create a matching reference
    session.refresh(publisher);

    book = new Book();
    book.setIsbn("9781430265177");
    book.setName("Beginning Hibernate");
    book.setPrice(new BigDecimal("44.00"));
    book.setPublishdate(Date.valueOf("2014-04-04"));
    book.setPublisher(publisher);

    session.persist(book);
    tx.commit();
    session.close();

    session = SessionManager.getSessionFactory().openSession();
    tx = session.beginTransaction();
    book1 = (Book) session.get(Book.class, "9781430265177");
    assertEquals(book.getName(), book1.getName());
    assertNotNull(book.getPublisher());
    assertEquals(book.getPublisher().getName(), publisher.getName());
    tx.commit();
    session.close();
  }

  /**
   * This method demonstrates a Hibernate update, which
   * is accomplished by merely updating a managed object
   * while it is being managed by an active
   * {@link org.hibernate.Session}.
   */
  @Test
  public void testUpdate() {
    Session session = SessionManager.getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    Publisher publisher = new Publisher();
    publisher.setCode("apress");
    publisher.setName("Apress");
    publisher.setAddress("233 Spring Street, New York, NY 10013");
    session.persist(publisher);
    tx.commit();
    session.close();

    session = SessionManager.getSessionFactory().openSession();
    tx = session.beginTransaction();
    Publisher publisher1 = (Publisher) session.load(Publisher.class, "apress");
    assertEquals(publisher.getName(), publisher1.getName());
    publisher1.setName("Apress Publishing");
    tx.commit();
    session.close();

    session = SessionManager.getSessionFactory().openSession();
    tx = session.beginTransaction();
    Publisher publisher2 = (Publisher) session.load(Publisher.class, "apress");
    assertEquals(publisher1.getName(), publisher2.getName());
    tx.commit();
    session.close();
  }

  /**
   * This method demonstrates an update of an object that is
   * <strong>not</strong> managed by an active
   * {@link org.hibernate.Session}. It does so by using
   * {@link org.hibernate.Session#merge(Object)}, which tells
   * Hibernate that the object in memory has precedence over the
   * object in the database.
   * <p/>
   * If the object in the database has precedence over the
   * object in memory, {@link org.hibernate.Session#refresh(Object)}
   * is used instead.
   */
  @Test
  public void testUpdateDetachedObject() {
    Session session = SessionManager.getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    Publisher publisher = new Publisher();
    publisher.setCode("apress");
    publisher.setName("Apress");
    publisher.setAddress("233 Spring Street, New York, NY 10013");
    session.persist(publisher);
    tx.commit();
    session.close();

    session = SessionManager.getSessionFactory().openSession();
    tx = session.beginTransaction();
    assertFalse(session.contains(publisher));

    // the order here is not relevant.
    publisher.setName("Apress Publishing");
    session.merge(publisher);

    tx.commit();
    session.close();

    session = SessionManager.getSessionFactory().openSession();
    tx = session.beginTransaction();
    Publisher publisher1 = (Publisher) session.load(Publisher.class, "apress");
    assertEquals(publisher1.getName(), publisher.getName());
    tx.commit();
    session.close();
  }

  /**
   * This method shows how to delete a specific object by using
   * the primary key value.
   * <p/>
   * First, it creates an object, then it reads it (to make sure it
   * was created properly), then it deletes it (via
   * {@link org.hibernate.Session#delete(Object)}. Lastly, it
   * attempts to read the object throuh
   * {@link org.hibernate.Session#get(Class, java.io.Serializable)},
   * which will return <code>null</code> if the object is not found.
   */
  @Test
  public void testDelete() {
    Session session = SessionManager.getSessionFactory().openSession();
    Transaction tx = session.beginTransaction();
    Publisher publisher = new Publisher();
    publisher.setCode("apress");
    publisher.setName("Apress");
    publisher.setAddress("233 Spring Street, New York, NY 10013");
    session.persist(publisher);
    tx.commit();
    session.close();

    session = SessionManager.getSessionFactory().openSession();
    tx = session.beginTransaction();
    Publisher publisher1 = (Publisher) session.load(Publisher.class, "apress");
    assertEquals(publisher.getName(), publisher1.getName());
    session.delete(publisher1);
    assertFalse(session.contains(publisher1));
    tx.commit();
    session.close();


    session = SessionManager.getSessionFactory().openSession();
    tx = session.beginTransaction();
    Publisher publisher2 = (Publisher) session.get(Publisher.class, "apress");
    assertNull(publisher2);
    tx.commit();
    session.close();
  }

  /**
   * This method creates a Publisher, copied from the
   * {@link Recipe1SessionTest#testCreate}
   * method. This method includes proper exception handling, which
   * isn't present in the other recipes for brevity's sake.
   */
  @Test
  public void showSafeExecution() {
    Session session = null;
    Transaction tx = null;
    try {
      SessionFactory sessionFactory = SessionManager.getSessionFactory();
      session = sessionFactory.openSession();
      SessionStatistics sessionStats = session.getStatistics();
      Statistics stats = sessionFactory.getStatistics();
      tx = session.beginTransaction();
      Publisher publisher = new Publisher();
      publisher.setCode("apress");
      publisher.setName("Apress");
      publisher.setAddress("233 Spring Street, New York, NY 10013");
      session.persist(publisher);
      tx.commit();
      logger.info("getEntityCount- " + sessionStats.getEntityCount());
      logger.info("openCount- " + stats.getSessionOpenCount());
      logger.info("getEntityInsertCount- " + stats.getEntityInsertCount());
      stats.logSummary();
      session.close();
    } catch (Throwable t) {
      if (tx != null) {
        tx.rollback();
      }
    } finally {
      if (tx != null && !tx.wasRolledBack() && !tx.wasCommitted()) {
        tx.commit();
      }
      if (session != null && session.isOpen()) {
        session.close();
      }
    }
  }
}
