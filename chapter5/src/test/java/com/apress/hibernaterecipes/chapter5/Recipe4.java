package com.apress.hibernaterecipes.chapter5;


import com.apress.hibernaterecipes.chapter5.recipe4.Address4;
import com.apress.hibernaterecipes.chapter5.recipe4.Customer4;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class Recipe4 {
    @BeforeMethod
    public void clearAll() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Customer4 c");
        List<Customer4> customers = query.list();
        for (Customer4 c : customers) {
            session.delete(c);
        }
        tx.commit();
        session.close();
    }

    @Test
    public void testOneToOne() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Customer4 customer4 = new Customer4();
        customer4.setName("Absalom");
        session.persist(customer4);

        Address4 address4 = new Address4();
        address4.setAddress("100 Hebron Way");
        address4.setCity("Tel Aviv");

        address4.setCustomer(customer4);
        customer4.setAddress4(address4);

        tx.commit();
        session.close();

        session = SessionManager.openSession();
        tx = session.beginTransaction();
        Customer4 customer = (Customer4) session
                .byId(Customer4.class)
                .load(customer4.getId());
        Hibernate.initialize(customer.getAddress4());
        tx.commit();
        session.close();

        assertEquals(customer.getName(), customer4.getName());
        assertEquals(customer.getAddress4().getAddress(),
                customer4.getAddress4().getAddress());
    }

    @Test(dependsOnMethods = "testOneToOne")
    public void other() {
    }
}
