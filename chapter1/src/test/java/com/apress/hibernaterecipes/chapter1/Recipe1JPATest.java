package com.apress.hibernaterecipes.chapter1;

import com.apress.hibernaterecipes.chapter1.model.Book;
import com.apress.hibernaterecipes.chapter1.model.Publisher;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Date;

import static org.testng.Assert.*;

/**
 * Created by jottinge on 6/25/14.
 */
public class Recipe1JPATest {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("chapter1");

    /**
     * This method simply iterates through the entity types, calling
     * a utility method that removes all available entries.
     * <p/>
     * It is meant to be called before each test method, so that the
     * tests have a blank slate with which to work.
     */
    @BeforeMethod
    public void clearData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        clearAll(em, "from Book b");
        clearAll(em, "from Publisher p");
        em.getTransaction().commit();
        em.close();
    }

    /**
     * This method executes an JPAQL delete with a constraint.
     *
     * @param em         the active manager in which the delete occurs
     * @param constraint the constraint for the delete statement
     */
    private void clearAll(EntityManager em, String constraint) {
        Query query = em.createQuery("delete " + constraint);
        query.executeUpdate();
    }

    @Test
    public void testCreate() {
    	
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Publisher publisher = new Publisher();
        publisher.setCode("apress");
        publisher.setName("Apress");
        publisher.setAddress("233 Spring Street, New York, NY 10013");
        em.persist(publisher);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        Publisher publisher1 = em.find(Publisher.class, "apress");
        assertEquals(publisher.getName(), publisher1.getName());
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testCreateObjectGraph() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
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

        em.persist(book);

        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        Book book1 = em.find(Book.class, "9781484201282");
        assertEquals(book.getName(), book1.getName());
        assertNotNull(book.getPublisher());
        assertEquals(book.getPublisher().getName(), publisher.getName());
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();

        // this changes the publisher back to managed state by
        // returning the managed version of publisher
        publisher = em.merge(publisher);

        book = new Book();
        book.setIsbn("9781430265177");
        book.setName("Beginning Hibernate");
        book.setPrice(new BigDecimal("44.00"));
        book.setPublishdate(Date.valueOf("2014-04-04"));
        book.setPublisher(publisher);

        em.persist(book);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        book1 = em.find(Book.class, "9781430265177");
        assertEquals(book.getName(), book1.getName());
        assertNotNull(book.getPublisher());
        assertEquals(book.getPublisher().getName(), publisher.getName());
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testUpdate() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Publisher publisher = new Publisher();
        publisher.setCode("apress");
        publisher.setName("Apress");
        publisher.setAddress("233 Spring Street, New York, NY 10013");
        em.persist(publisher);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        Publisher publisher1 = em.find(Publisher.class, "apress");
        assertEquals(publisher.getName(), publisher1.getName());
        publisher1.setName("Apress Publishing");
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        Publisher publisher2 = em.find(Publisher.class, "apress");
        assertEquals(publisher1.getName(), publisher2.getName());
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testUpdateDetachedObject() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Publisher publisher = new Publisher();
        publisher.setCode("apress");
        publisher.setName("Apress");
        publisher.setAddress("233 Spring Street, New York, NY 10013");
        em.persist(publisher);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        assertFalse(em.contains(publisher));

        // the order here is not relevant.
        publisher.setName("Apress Publishing");
        em.merge(publisher);

        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        Publisher publisher1 = em.find(Publisher.class, "apress");
        assertEquals(publisher1.getName(), publisher.getName());
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testDelete() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Publisher publisher = new Publisher();
        publisher.setCode("apress");
        publisher.setName("Apress");
        publisher.setAddress("233 Spring Street, New York, NY 10013");
        em.persist(publisher);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        Publisher publisher1 = em.find(Publisher.class, "apress");
        assertEquals(publisher.getName(), publisher1.getName());
        em.remove(publisher1);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        Publisher publisher2 = em.find(Publisher.class, "apress");
        assertNull(publisher2);
        em.getTransaction().commit();
        em.close();
    }

}
