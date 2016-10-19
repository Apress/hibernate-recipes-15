package com.apress.hibernaterecipes;

import com.apress.hibernaterecipes.chapter4.model.recipe1.AudioDisc;
import com.apress.hibernaterecipes.chapter4.model.recipe1.Disc;
import com.apress.hibernaterecipes.chapter4.model.recipe1.VideoDisc;
import com.apress.hibernaterecipes.util.SessionManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class Recipe1 {
    @BeforeMethod
    public void cleanAll() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("delete from Disc").executeUpdate();
        tx.commit();
        session.close();
    }

    @Test
    public void testHierarchy() {
        Session session = SessionManager.openSession();
        Transaction tx = session.beginTransaction();

        Disc disc = new Disc();
        disc.setName("Blank CDR");
        disc.setPrice(199);
        session.save(disc);

        VideoDisc videoDisc = new VideoDisc();
        videoDisc.setName("Blazing Saddles");
        videoDisc.setPrice(1499);
        videoDisc.setDirector("Mel Brooks");
        videoDisc.setLanguage("english");
        session.save(videoDisc);

        AudioDisc audioDisc = new AudioDisc();
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
        Disc disc1 = (Disc) session.load(Disc.class, disc.getId());
        assertEquals(disc1.getName(), disc.getName());

        VideoDisc videoDisc2 = (VideoDisc) session.load(VideoDisc.class, videoDisc.getId());
        assertEquals(videoDisc2.getName(), videoDisc.getName());

        tx.commit();
        session.close();
    }

    @Test
    public void showGeneralQueryByPrice() {
        // populate data
        testHierarchy();
        Session session = SessionManager.openSession();
        session.setDefaultReadOnly(true);
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Disc d where d.price>:price");
        query.setParameter("price", 1299);
        List<Disc> results = query.list();
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
        Query query = session.createQuery("from Disc d");
        List<Disc> results = query.list();
        assertEquals(results.size(), 3);
        for (Disc d : results) {
            switch (d.getClass().getName()) {
                case "Disc":
                    assertEquals(d.getName(), "Blank CDR");
                    break;
                case "AudioDisc":
                    AudioDisc audioDisc = (AudioDisc) d;
                    assertEquals(audioDisc.getArtist(), "Rush");
                    break;
                case "VideoDisc":
                    VideoDisc videoDisc = (VideoDisc) d;
                    assertEquals(videoDisc.getDirector(), "Mel Brooks");
                    break;
            }
        }
        tx.commit();
        session.close();
    }
}
