package com.apress.hibernaterecipes.chapter2.test;

import com.apress.hibernaterecipes.chapter2.recipe2.Book;
import com.apress.hibernaterecipes.chapter2.recipe2.Employee;
import com.apress.hibernaterecipes.chapter2.recipe2.EmployeeId;
import com.apress.hibernaterecipes.chapter2.recipe2.ISBN;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Recipe2Test {
    @DataProvider
    Object[][] getEmployeeData() {
        return new Object[][]{{1, 1, "Joseph Ottinger", false},
                {1, 2, "Srinivas Guruzu", false},
                {2, 1, "J. C. Smell", false},
                {1, 1, "Drew Lombardo", true}};
    }

    @DataProvider
    Object[][] getBookData() {
        return new Object[][]{
                {978, 14, 3026, 517, 7, "Beginning Hibernate"},
                {978, 14, 8420, 128, 2, "Hibernate Recipes"},
        };
    }

    @Test(dataProvider = "getEmployeeData")
    public void testEmbeddedId(long dept, long card, String name, boolean duplicate) {
        Session session = SessionManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Employee employee = new Employee(dept, card, name);
        try {
            session.persist(employee);
            tx.commit();
        } catch (ConstraintViolationException cve) {
            if (!duplicate) {
                fail("Didn't catch duplicate employee id");
            }
        } finally {
            session.close();
        }
    }

    @Test(dataProvider = "getEmployeeData", dependsOnMethods = "testEmbeddedId")
    public void testLoadWithEmbeddedId(long dept, long card, String name, boolean duplicate) {
        if (!duplicate) {
            Session session = SessionManager.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Employee employee = (Employee) session.get(Employee.class, new EmployeeId(dept, card));
            assertNotNull(employee);
            assertEquals(employee.getDepartment(), Long.valueOf(dept));
            assertEquals(employee.getIdCard(), Long.valueOf(card));
            assertEquals(employee.getName(), name);
            tx.commit();
            session.close();
        }
    }

    @Test(dataProvider = "getBookData")
    public void createBookData(int ean, int group, int publisher, int titleRef, int checkDigit, String title) {
        Session session = SessionManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // ean is ignored for 10-digit ISBNs; it's always 978 anyway.
        Book book = new Book(ean, group, publisher, titleRef, checkDigit, title);
        session.persist(book);
        tx.commit();
        session.close();
    }

    @Test(dataProvider = "getBookData", dependsOnMethods = "createBookData")
    public void loadBookData(int ean, int group, int publisher, int titleRef, int checkDigit, String title) {
        Session session = SessionManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Book book = (Book) session.load(Book.class, new ISBN(ean, group, publisher, titleRef, checkDigit));
        assertEquals(book.getTitle(), title);
        tx.commit();
        session.close();
    }
}
