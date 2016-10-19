package com.apress.hibernaterecipes.chapter3;

import com.apress.hibernaterecipes.chapter3.recipe2.Contact;
import com.apress.hibernaterecipes.chapter3.recipe2.Order;
import com.apress.hibernaterecipes.chapter3.recipe2.Phone;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

public class Recipe2Test {
    @Test
    public void testNestedComponents() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Order order = new Order();
        order.setWeekdayContact(new Contact(
                "Srinivas Guruzu",
                "100 Main Street",
                new Phone(454, 555, 1212)));
        order.setHolidayContact(new Contact(
                "Joseph Ottinger",
                "P. O. Box 0",
                new Phone(978, 555, 1212)));
        session.persist(order);
        tx.commit();
        session.close();
    }
}
