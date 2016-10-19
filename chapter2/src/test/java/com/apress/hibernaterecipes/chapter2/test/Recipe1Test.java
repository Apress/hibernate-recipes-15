package com.apress.hibernaterecipes.chapter2.test;

import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class Recipe1Test {
    @DataProvider(name = "idClassNames")
    Object[][] getClassNames() {
        return new Object[][]{
                {"com.apress.hibernaterecipes.chapter2.recipe1.AutoIdEntity",},
                {"com.apress.hibernaterecipes.chapter2.recipe1.HiloIdEntity",},
                {"com.apress.hibernaterecipes.chapter2.recipe1.IdentityIdEntity",},
                {"com.apress.hibernaterecipes.chapter2.recipe1.IncrementIdEntity",},
                {"com.apress.hibernaterecipes.chapter2.recipe1.SequenceIdEntity",},
                {"com.apress.hibernaterecipes.chapter2.recipe1.TableIdEntity",},
                {"com.apress.hibernaterecipes.chapter2.recipe1.UUIDIdEntity",},
                {"com.apress.hibernaterecipes.chapter2.recipe1.SequenceStyleIdEntity",},
                {"com.apress.hibernaterecipes.chapter2.recipe1.EnhancedTableIdEntity",},
        };
    }

    /**
     * This method generates forty entities, in ten batches of four. As it
     * generates them, it stores the generated identities in a Set, for
     * validation of uniqueness (which shouldn't be a problem) and for simple
     * visual verification purposes.
     *
     * @param className the entity type to generate
     */
    @Test(dataProvider = "idClassNames")
    public void testGeneration(String className) throws Exception {
        Set<Object> generatedKeys = new HashSet<>();
        System.out.println("Testing: " + className);
        Class<?> entityType = Class.forName(className);
        Field idField = entityType.getDeclaredField("id");
        Field fieldField = entityType.getDeclaredField("field");

        for (int i = 0; i < 10; i++) {
            Session session = SessionManager.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            for (int j = 0; j < 4; j++) {
                Object o = entityType.newInstance();
                fieldField.set(o, "" + (char) ('a' + i) + (char) ('b' + j));
                session.persist(o);
                generatedKeys.add(idField.get(o));
            }
            tx.commit();
            session.close();
        }
        for (Object key : generatedKeys) {
            System.out.println(key);
        }
        assertEquals(generatedKeys.size(), 40);
    }
}
