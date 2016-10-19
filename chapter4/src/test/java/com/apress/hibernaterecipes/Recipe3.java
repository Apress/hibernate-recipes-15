package com.apress.hibernaterecipes;

import com.apress.hibernaterecipes.chapter4.model.recipe3.AudioDisc3;
import com.apress.hibernaterecipes.chapter4.model.recipe3.Disc3;
import com.apress.hibernaterecipes.chapter4.model.recipe3.VideoDisc3;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class Recipe3 {
@BeforeMethod
public void cleanAll() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();
    session.createQuery("delete from Disc3").executeUpdate();
    tx.commit();
    session.close();
}

@Test
public void testHierarchy() {
    Session session = SessionManager.openSession();
    Transaction tx = session.beginTransaction();

    Disc3 disc = new Disc3();
    disc.setName("Blank CDR");
    disc.setPrice(199);
    session.save(disc);

    VideoDisc3 videoDisc = new VideoDisc3();
    videoDisc.setName("Blazing Saddles");
    videoDisc.setPrice(1499);
    videoDisc.setDirector("Mel Brooks");
    videoDisc.setLanguage("english");
    session.save(videoDisc);

    AudioDisc3 audioDisc = new AudioDisc3();
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

    VideoDisc3 videoDisc2 = (VideoDisc3) session.load(VideoDisc3.class, videoDisc.getId());
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
    Query query=session.createQuery("from Disc3 d where d.price>:price");
    query.setParameter("price", 1299);
    List<Disc3> results=query.list();
    assertEquals(results.size(), 1);
    assertEquals(results.get(0).getName(), "Blazing Saddles");
    tx.commit();
    session.close();
}

@Test
public void showGeneralQuery() {
    testHierarchy();
    Session session = SessionManager.openSession();
    session.setDefaultReadOnly(true);
    Transaction tx = session.beginTransaction();
    Query query = session.createQuery("from Disc3 d");
    List<Disc3> results = query.list();
    assertEquals(results.size(), 3);
    for (Disc3 d : results) {
        switch (d.getClass().getName()) {
            case "Disc":
                assertEquals(d.getName(), "Blank CDR");
                break;
            case "AudioDisc":
                AudioDisc3 audioDisc = (AudioDisc3) d;
                assertEquals(audioDisc.getArtist(), "Rush");
                break;
            case "VideoDisc":
                VideoDisc3 videoDisc = (VideoDisc3) d;
                assertEquals(videoDisc.getDirector(), "Mel Brooks");
                break;
        }
    }
    tx.commit();
    session.close();
}
}
