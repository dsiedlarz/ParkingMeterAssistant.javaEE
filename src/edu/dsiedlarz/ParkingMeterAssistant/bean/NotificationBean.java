package edu.dsiedlarz.ParkingMeterAssistant.bean;

import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.Location;
import edu.dsiedlarz.ParkingMeterAssistant.model.Notification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by private on 11.06.2017.
 */
@Stateless(name = "NotificationEJB")
public class NotificationBean {
    public NotificationBean() {
    }

    public Notification getNotification(int id) {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        String hql = "from Notification WHERE id = :id";
        Object result = session.createQuery(hql)
                .setParameter("id", id)
                .uniqueResult();

        tx.commit();
        session.close();

        return  (Notification) result;
    }

    public ArrayList<Notification> getActiveNotifications(Location location) {
        ArrayList<Notification> notifications = new ArrayList<>();

        if (location == null) {
            return notifications;
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
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, -1);
        String hql = "Select n from Notification n INNER JOIN n.parkingPlace p  where n.active = true AND   n.time > :date  AND p.location = :currentLocation ";
        List result = session.createQuery(hql)
                .setParameter("currentLocation", location)
                .setParameter("date", c.getTime())
                .list();

        for (Iterator iter = result.iterator(); iter.hasNext(); ) {
            notifications.add((Notification) iter.next());
        }

        System.out.println(notifications);
        tx.commit();
        session.close();

        return notifications;
    }

}
