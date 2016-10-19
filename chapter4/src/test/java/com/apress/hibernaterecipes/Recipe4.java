package com.apress.hibernaterecipes;

import com.apress.hibernaterecipes.chapter4.model.recipe4.*;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class Recipe4 {
@BeforeMethod
public void cleanAll() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    session.createQuery("delete from VideoDisc4").executeUpdate();
    session.createQuery("delete from AudioDisc4").executeUpdate();
    tx.commit();
    session.close();
}

@Test
public void testHierarchy() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();

    VideoDisc4 videoDisc = new VideoDisc4();
    videoDisc.setName("Blazing Saddles");
    videoDisc.setPrice(1499);
    videoDisc.setDirector("Mel Brooks");
    videoDisc.setLanguage("english");
    session.save(videoDisc);

    AudioDisc4 audioDisc = new AudioDisc4();
    audioDisc.setName("Grace Under Pressure");
    audioDisc.setPrice(999);
    audioDisc.setArtist("Rush");
    audioDisc.setTrackCount(8);
    session.save(audioDisc);

    tx.commit();
    session.close();

    session = SessionManager.openSession();
    session.setDefaultReadOnly(true);
    tx = session.beginTransaction();

    VideoDisc4 videoDisc2 = (VideoDisc4) session.load(VideoDisc4.class, videoDisc.getId());
    assertEquals(videoDisc2.getName(), videoDisc.getName());

    tx.commit();
    session.close();
}

@Test
public void showGeneralQueryByType() {
    // populate data
    testHierarchy();
    Session session=SessionManager.openSession();
    session.setDefaultReadOnly(true);
    Transaction tx=session.beginTransaction();
    Query query=session.createQuery("from VideoDisc4 d where d.price>:price");
    query.setParameter("price", 1299);
    List<Disc4> results=query.list();
    assertEquals(results.size(), 1);
    assertEquals(results.get(0).getName(), "Blazing Saddles");
    tx.commit();
    session.close();
}
}
