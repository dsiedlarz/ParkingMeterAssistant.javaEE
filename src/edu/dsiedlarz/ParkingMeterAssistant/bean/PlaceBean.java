package edu.dsiedlarz.ParkingMeterAssistant.bean;

import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.Location;
import edu.dsiedlarz.ParkingMeterAssistant.model.ParkingPlace;
import edu.dsiedlarz.ParkingMeterAssistant.model.ParkingPlaceState;
import edu.dsiedlarz.ParkingMeterAssistant.model.PlaceStats;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by private on 11.06.2017.
 */
@Stateless(name = "PlaceEJB")
public class PlaceBean {
    public PlaceBean() {
    }

    public PlaceStats getPlacesStatus(Location location) {
        PlaceStats placeStats = new PlaceStats();

        if (location == null) {
            return placeStats;
        }
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();
        //noinspection JpaQlInspection
        String hql = "SELECT count(*), p.state FROM ParkingPlace p WHERE p.location = :location GROUP BY p.state ";
        List result = session.createQuery(hql)
                .setParameter("location", location)
                .list();

        List<Object[]> recs = result;
        for (Object[] line : recs) {
            if (line[1] == ParkingPlaceState.FREE) {
                placeStats.setFree(Integer.valueOf(line[0].toString()));
            }
            if (line[1] == ParkingPlaceState.TAKEN) {
                placeStats.setTaken(Integer.valueOf(line[0].toString()));
            }
            if (line[1] == ParkingPlaceState.PAID) {
                placeStats.setPaid(Integer.valueOf(line[0].toString()));
            }

            if (line[1] == ParkingPlaceState.SUSPICIOUS) {
                placeStats.setSuspicious(Integer.valueOf(line[0].toString()));
            }
        }

        System.out.println(placeStats);
        tx.commit();
        session.close();

        return placeStats;
    }


    public ArrayList<ParkingPlace> getPlaces(Location location) {
        ArrayList<ParkingPlace> parkingPlaces = new ArrayList<>();

        if (location == null) {
            return parkingPlaces;
        }
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();
        //noinspection JpaQlInspection
        String hql = "FROM ParkingPlace p WHERE p.location = :location ORDER BY id asc ";
        List result = session.createQuery(hql)
                .setParameter("location", location)
                .list();

        for (Object object : result) {
           parkingPlaces.add((ParkingPlace) object);
        }

        tx.commit();
        session.close();

        return parkingPlaces;
    }

    public ParkingPlace getParkingPlace(int id) {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        String hql = "from ParkingPlace WHERE id = :id";
        Object result = session.createQuery(hql)
                .setParameter("id", id)
                .uniqueResult();

        tx.commit();
        session.close();

        return  (ParkingPlace) result;
    }

    public void saveParkingPlace(ParkingPlace parkingPlace) {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        session.saveOrUpdate(parkingPlace);
        tx.commit();
        session.close();



        System.out.println("Send notification");
        EventBus eventBus = EventBusFactory.getDefault().eventBus();

        eventBus.publish("/primepush/notify", "notify");
        eventBus.publish("/notify", "notify");
    }
}
