package com.apress.hibernaterecipes.chapter3;

import com.apress.hibernaterecipes.chapter3.model.*;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

public class Recipe1Test {
    @Test
    public void testNonEmbeddedOrder() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        NonEmbeddedOrder nonEmbeddedOrder = new NonEmbeddedOrder();
        nonEmbeddedOrder.setWeekdayRecipientName("Srinivas Guruzu");
        nonEmbeddedOrder.setWeekdayAddress("100 Main Street");
        nonEmbeddedOrder.setHolidayPhone("454-555-1212");
        nonEmbeddedOrder.setHolidayRecipientName("Joseph Ottinger");
        nonEmbeddedOrder.setHolidayAddress("P.O. Box 0");
        nonEmbeddedOrder.setHolidayPhone("978-555-1212");
        session.save(nonEmbeddedOrder);
        tx.commit();
        session.close();
    }

    @Test
    public void testOrderWithRelatedContact() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        RelatedContact srinivas = new RelatedContact();
        srinivas.setName("Srinivas Guruzu");
        srinivas.setAddress("100 Main Street");
        srinivas.setPhone("454-555-1212");

        RelatedContact joseph = new RelatedContact();
        joseph.setName("Joseph B. Ottinger");
        joseph.setAddress("P.O. Box 0");
        joseph.setPhone("978-555-1212");

        OrderWithRelatedContact order = new OrderWithRelatedContact();
        order.setWeekdayContact(srinivas);
        order.setHolidayContact(joseph);

        session.save(order);
        tx.commit();
        session.close();
    }

    @Test
    public void testOrderWithEmbeddedContact() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        EmbeddedContact srinivas = new EmbeddedContact();
        srinivas.setName("Srinivas Guruzu");
        srinivas.setAddress("100 Main Street");
        srinivas.setPhone("454-555-1212");

        EmbeddedContact joseph = new EmbeddedContact();
        joseph.setName("Joseph B. Ottinger");
        joseph.setAddress("P.O. Box 0");
        joseph.setPhone("978-555-1212");

        OrderWithEmbeddedContact order = new OrderWithEmbeddedContact();
        order.setWeekdayContact(srinivas);
        order.setHolidayContact(joseph);

        session.save(order);
        tx.commit();
        session.close();
    }
}
