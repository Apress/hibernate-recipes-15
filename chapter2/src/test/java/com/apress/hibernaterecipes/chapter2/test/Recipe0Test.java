package com.apress.hibernaterecipes.chapter2.test;

import com.apress.hibernaterecipes.chapter2.recipe0.Product;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import static org.testng.Assert.assertEquals;

public class Recipe0Test {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("chapter2");

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
        clearAll(em, "from Product p");

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
        //Session session = SessionManager.getSessionFactory().openSession();
        //Transaction tx = session.beginTransaction();
        Product product = new Product();
        product.setSku(1234l);
        product.setTitle("XBox");
        product.setDescription("Gaming");
        //session.persist(product);
        em.persist(product);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        Product product1 = em.find(Product.class, 1234l);
        //Product product1 = (Product)session.load(Product.class, 1234l);
        assertEquals(product.getTitle(), product1.getTitle());
        em.getTransaction().commit();
        em.close();
    }
}
