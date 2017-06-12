package edu.dsiedlarz.ParkingMeterAssistant.bean;

import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.Location;
import edu.dsiedlarz.ParkingMeterAssistant.model.Notification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by private on 11.06.2017.
 */
@Stateless(name = "LocationEJB")
public class LocationBean {

    @Resource
    SessionContext ctx;


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

        System.out.println("principal "+ ctx.getCallerPrincipal());
        if (ctx.isCallerInRole("Admin") || ctx.getCallerPrincipal().toString().compareToIgnoreCase("anonymous") == 0) {
            List locationsList = session.createQuery("from Location").list();
            for (Iterator iter = locationsList.iterator(); iter.hasNext(); ) {
                locations.add((Location) iter.next());
            }
        } else {
            List locationsList = session.createQuery("from Location l WHERE l IN (SELECT a.location FROM UserLocationAssociation a INNER JOIN a.user u WHERE u.login = :login )")
                    .setParameter("login", ctx.getCallerPrincipal().getName()).list();
            for (Iterator iter = locationsList.iterator(); iter.hasNext(); ) {
                locations.add((Location) iter.next());
            }
        }


        tx.commit();
        session.close();

        return locations;
    }


    public Location getLocation(int id) {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        String hql = "from Location WHERE id = :id";
        Object result = session.createQuery(hql)
                .setParameter("id", id)
                .uniqueResult();

        tx.commit();
        session.close();

        return  (Location) result;
    }
}
