package edu.dsiedlarz.ParkingMeterAssistant.bean;

import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by private on 11.06.2017.
 */
@Stateless(name = "ChangePasswordEJB")
public class ChangePasswordBean {
    public ChangePasswordBean() {
    }



    public void changePassword(String login, String newPassword) {
//        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.reset();
        md.update(newPassword.getBytes());

        String hashedPassword = String.format("%016x", new BigInteger(1, md.digest()));



        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        Object result;
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        if(origRequest.isUserInRole("Admin")) {
            String hql = "from User where login = :login";
            result = session.createQuery(hql)
                    .setParameter("login", login)

                    .uniqueResult();
        } else {
            String hql = "from User where login = :login";
            result = session.createQuery(hql)
                    .setParameter("login", origRequest.getRemoteUser())
                    .uniqueResult();
        }

        User user = (User) result;
        user.setPasswd(hashedPassword);

        session.saveOrUpdate(user);
        tx.commit();
        session.close();




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
