package edu.dsiedlarz.ParkingMeterAssistant.bean;

import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.ParkingPlace;
import edu.dsiedlarz.ParkingMeterAssistant.model.Ticket;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ejb.Stateless;

/**
 * Created by private on 12.06.2017.
 */
@Stateless(name = "TicketEJB")
public class TicketBean {
    public TicketBean() {
    }

    public Ticket getTicket(int id) {

        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        String hql = "from Ticket WHERE id = :id";
        Object result = session.createQuery(hql)
                .setParameter("id", id)
                .uniqueResult();

        tx.commit();
        session.close();

        return  (Ticket) result;
    }

    public void saveTicket(Ticket ticket) {

        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        session.saveOrUpdate(ticket);
        tx.commit();
        session.close();

    }
}
