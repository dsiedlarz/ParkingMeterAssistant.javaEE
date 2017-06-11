package edu.dsiedlarz.ParkingMeterAssistant.dashboard;

import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.*;
import org.apache.tools.ant.taskdefs.condition.Not;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.security.AuthenticationManager;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.security.auth.login.LoginContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by private on 05.06.2017.
 */
@ManagedBean(name = "dashboardBean", eager = true)
@SessionScoped
public class DashboardBean {

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public ArrayList<Notification> getActiveNotifications() {
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
//        String hql = "Select n from Notification n  ";
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

    public PlaceStats getPlacesStatus() {
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
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, -1);
        String hql = "SELECT count(*), p.state FROM ParkingPlace p WHERE p.location = :location GROUP BY p.state ";
//        String hql = "Select n from Notification n  ";
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
            System.out.println("Total entities under Master with id " + line[1] + " is " + line[0]);
        }

        System.out.println(placeStats);
        tx.commit();
        session.close();

        return placeStats;
    }


    public void closeNotification(int id) {
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
        String hql = "Select n from Notification n WHERE n.id = :id";
//        String hql = "Select n from Notification n  ";
        Notification notification = (Notification) session.createQuery(hql)
                .setParameter("id", id)
                .uniqueResult();

        notification.setActive(false);

        session.save(notification);
        tx.commit();
        session.close();
    }

    public void logout() {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);


        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        try {
            origRequest.logout();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

            System.out.println("log out");
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public String getLoggedUser() {
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return origRequest.getRemoteUser() + (origRequest.isUserInRole("Admin") ? "Admin" : "Manager");


    }

}
