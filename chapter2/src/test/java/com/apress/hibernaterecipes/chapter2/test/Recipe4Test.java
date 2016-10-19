package com.apress.hibernaterecipes.chapter2.test;

import com.apress.hibernaterecipes.chapter2.recipe4.DynamicSQLEntity;
import com.apress.hibernaterecipes.chapter2.recipe4.StandardSQLEntity;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

public class Recipe4Test {
    @Test
    public void testStandardSQLInsert() {

        Session session;
        Transaction tx;

        System.out.println("Check logs: SQL for insert should " +
                "contain field1..field8");
        session = SessionManager.getSessionFactory().openSession();
        tx = session.beginTransaction();
        StandardSQLEntity standardSQLEntity = new StandardSQLEntity();
        standardSQLEntity.setField1("field 1");
        session.save(standardSQLEntity);
        tx.commit();
        session.close();
    }

    @Test
    public void testDynamicSQLInsert() {
        Session session;
        Transaction tx;

        System.out.println("Check logs: SQL for insert should " +
                "contain only field1");
        session = SessionManager.getSessionFactory().openSession();
        tx = session.beginTransaction();
        DynamicSQLEntity dynamicSQLEntity = new DynamicSQLEntity();
        dynamicSQLEntity.setField1("field 1");
        session.save(dynamicSQLEntity);
        tx.commit();
        session.close();
    }
}
