package edu.dsiedlarz.ParkingMeterAssistant.beans;

import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by private on 11.06.2017.
 */
@Singleton(name = "LocationEJB")
public class LocationBean {
    public LocationBean() {
    }

    public ArrayList<Location> getLocations() {

        ArrayList<Location> locations = new ArrayList<>();
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();


        //noinspection JpaQlInspection
        List locationsList = session.createQuery("from Location").list();
        for (Iterator iter = locationsList.iterator(); iter.hasNext(); ) {
            locations.add((Location) iter.next());
        }

        tx.commit();
        session.close();

        return locations;
    }
}
