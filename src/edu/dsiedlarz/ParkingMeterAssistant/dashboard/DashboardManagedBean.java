package edu.dsiedlarz.ParkingMeterAssistant.dashboard;

import edu.dsiedlarz.ParkingMeterAssistant.bean.LocationBean;
import edu.dsiedlarz.ParkingMeterAssistant.bean.NotificationBean;
import edu.dsiedlarz.ParkingMeterAssistant.bean.PlaceBean;
import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class DashboardManagedBean {

    private Location location;

    @EJB
    private LocationBean locationBean;

    @EJB
    private PlaceBean placeBean;

    @EJB
    private NotificationBean notificationBean;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<Location> getLocations() {
        return locationBean.getLocations();
    }

    public ArrayList<Notification> getActiveNotifications() {
       return notificationBean.getActiveNotifications(location);
    }

    public PlaceStats getPlacesStatus() {
        return placeBean.getPlacesStatus(location);
    }


    public ArrayList<ParkingPlace> getAllParkingPlaces() {
        return placeBean.getPlaces(location);
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

        session.saveOrUpdate(notification);
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
        return origRequest.getRemoteUser() + " " + (origRequest.isUserInRole("Admin") ? "Admin" : "Manager");


    }

    public void redirect() {

        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        if(origRequest.getRemoteUser() != null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("Dashboard.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
