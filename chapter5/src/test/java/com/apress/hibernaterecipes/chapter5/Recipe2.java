package com.apress.hibernaterecipes.chapter5;

import com.apress.hibernaterecipes.chapter5.recipe2.Book2;
import com.apress.hibernaterecipes.chapter5.recipe2.Publisher2;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Recipe2 {
    @BeforeMethod
    public void cleanAll() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        //session.createQuery("delete from Book2").executeUpdate();
        session.createQuery("delete from Publisher2").executeUpdate();
        tx.commit();
        session.close();
    }

    @Test
    public void createPublisher() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        Publisher2 publisher = new Publisher2();
        publisher.setName("Sirius Topics");
        publisher.setAddress("100 Canine Way");
        publisher.setBooks(new ArrayList<Book2>());
        Book2 book2 = new Book2();
        book2.setTitle("Now isn't the Time");
        book2.setPrice(BigDecimal.valueOf(29.99));
        publisher.getBooks().add(book2);
        session.persist(book2);
        session.persist(publisher);
        tx.commit();
        session.close();

    }
}
