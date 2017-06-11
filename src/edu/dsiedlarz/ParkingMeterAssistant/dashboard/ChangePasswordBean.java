package edu.dsiedlarz.ParkingMeterAssistant.dashboard;

import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.Notification;
import edu.dsiedlarz.ParkingMeterAssistant.model.User;
import org.codehaus.jettison.json.JSONArray;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jboss.security.Util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by private on 11.06.2017.
 */
@ManagedBean(name = "changePasswordBean", eager = true)
@SessionScoped
public class ChangePasswordBean {

    public void changePassword(String user) {
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String newPassword = origRequest.getParameter("newPassword");
        System.out.println("newPassword " + newPassword);
        String hashedPassword = Util.createPasswordHash("MD5",
                Util.BASE64_ENCODING,
                null,
                null,
                "test");
    }


    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        List result;
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        if(origRequest.isUserInRole("Admin")) {
            String hql = "from User";
            result = session.createQuery(hql)
                    .list();
        } else {
            String hql = "from User where login = :login";
            result = session.createQuery(hql)
                    .setParameter("login", origRequest.getRemoteUser())
                    .list();
        }


        for (Object aResult : result) {
            User user = ((User) aResult);
            users.add(user);
        }

        tx.commit();
        session.close();

        return users;
    }


}
