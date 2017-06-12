package edu.dsiedlarz.ParkingMeterAssistant.bean;

import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.Location;
import edu.dsiedlarz.ParkingMeterAssistant.model.ParkingPlace;
import edu.dsiedlarz.ParkingMeterAssistant.model.TicketPlaceAssociation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ejb.Stateless;
import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by private on 11.06.2017.
 */
@Stateless(name = "TicketPlaceAssociationEJB")
public class TicketPlaceAssociationBean {
    public TicketPlaceAssociationBean() {
    }

    public void saveAssociation(TicketPlaceAssociation association) {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        session.saveOrUpdate(association);

        tx.commit();
        session.close();
    }

    public TicketPlaceAssociation getAssociation(int id) {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        String hql = "from TicketPlaceAssociation WHERE id = :id";
        Object result = session.createQuery(hql)
                .setParameter("id", id)
                .uniqueResult();

        tx.commit();
        session.close();

        return (TicketPlaceAssociation) result;
    }


    public TicketPlaceAssociation getOldestAssociation(Location location) {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        String hql = "SELECT a from TicketPlaceAssociation a INNER JOIN a.parkingPlace p WHERE a.active = true AND p.location = :location ORDER BY time asc";
        Object result = session.createQuery(hql)
                .setParameter("location", location)
                .setMaxResults(1)
                .uniqueResult();

        tx.commit();
        session.close();

        return (TicketPlaceAssociation) result;
    }


    public ArrayList<TicketPlaceAssociation> getNewCheaters(int late) {

        ArrayList<TicketPlaceAssociation> newCheaters = new ArrayList<>();

        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, (-1*late));

        String hql = "SELECT a from TicketPlaceAssociation a WHERE a.active = true AND a.ticket IS NULL AND a.time < :date";
        List result = session.createQuery(hql)
                .setParameter("date", c.getTime())
                .list();

        for (Object o:
          result   ) {
            newCheaters.add((TicketPlaceAssociation)o);
        }

        tx.commit();
        session.close();

        return newCheaters;
    }

    public ArrayList<TicketPlaceAssociation> getLateComers(int late) {

        ArrayList<TicketPlaceAssociation> newCheaters = new ArrayList<>();

        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, (-1*late));

        String hql = "SELECT a from TicketPlaceAssociation a INNER JOIN a.ticket t WHERE a.active = true AND t.endTime < :date ";
        List result = session.createQuery(hql)
                .setParameter("date", c.getTime())
                .list();

        for (Object o:
          result   ) {
            newCheaters.add((TicketPlaceAssociation)o);
        }
        System.out.println(newCheaters);


        tx.commit();
        session.close();

        return newCheaters;
    }
}
