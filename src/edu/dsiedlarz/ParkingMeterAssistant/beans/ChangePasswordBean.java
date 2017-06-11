package edu.dsiedlarz.ParkingMeterAssistant.beans;

import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jboss.security.Util;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by private on 11.06.2017.
 */
@Stateless(name = "ChangePasswordEJB")
public class ChangePasswordBean {
    public ChangePasswordBean() {
    }

    public void changePassword(String user, String password) {
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String newPassword = origRequest.getParameter("newPassword");
        System.out.println("newPassword " + newPassword);
//        String hashedPassword = Util.createPasswordHash("MD5",
//                Util.BASE64_ENCODING,
//                null,
//                null,
//                "test");
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
