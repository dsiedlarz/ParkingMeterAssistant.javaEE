package edu.dsiedlarz.ParkingMeterAssistant.helpers;

import edu.dsiedlarz.ParkingMeterAssistant.model.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;


/**
 * Created by private on 24.05.2017.
 */
public class Main {


    public static void main(String[] args) {
        /** Getting the Session Factory and session */
//        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
//        Session session = null;
//
//
//        try {
//            session = sessionFactory.getCurrentSession();
//        } catch (org.hibernate.HibernateException he) {
//            session = sessionFactory.openSession();
//        }
//
//        /** Starting the Transaction */
//        Transaction tx = session.beginTransaction();
//        /** Creating Pojo */
//        Book book = new Book();
//
//        book.setImię("Jan");
//        book.setNazwisko("Kowalski");
//        /** Saving POJO */
//        session.saveOrUpdate(book);
//        /** Commiting the changes */
//        tx.commit();
//        System.out.println("Record Inserted");  /** Closing Session */
//        session.close();
        generateDatabase();


        addPlaces();

    }


    private static void addPlaces() {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }

        /** Starting the Transaction */
        Transaction tx = session.beginTransaction();

        Location l1 = new Location();
        l1.setName("ul. Długa");
        Location l2 = new Location();
        l2.setName("ul. Krótka");
        session.saveOrUpdate(l1);
        session.saveOrUpdate(l2);

        Query query = session.createQuery("from Location WHERE id = 1");
        Location location = (Location) query.list().get(0);

        for (int i = 0; i < 25; i++) {

            ParkingPlace parkingPlace = new ParkingPlace();
            parkingPlace.setState(ParkingPlaceState.FREE);
            parkingPlace.setLocation(location);
            /** Saving POJO */
            session.saveOrUpdate(parkingPlace);

        }

         query = session.createQuery("from Location WHERE id = 2");
         location = (Location) query.list().get(0);

        for (int i = 0; i < 25; i++) {

            ParkingPlace parkingPlace = new ParkingPlace();
            parkingPlace.setState(ParkingPlaceState.FREE);
            parkingPlace.setLocation(location);
            /** Saving POJO */
            session.saveOrUpdate(parkingPlace);

        }

        tx.commit();
        System.out.println("Record Inserted");  /** Closing Session */
        session.close();
    }

    private static void generateDatabase() {
        AnnotationConfiguration config = new AnnotationConfiguration();
//        config.addAnnotatedClass(Location.class);
//        config.addAnnotatedClass(ParkingMeter.class);
//        config.addAnnotatedClass(ParkingPlace.class);
//        config.addAnnotatedClass(Ticket.class);
//        config.addAnnotatedClass(Notification.class);
//        config.addAnnotatedClass(TicketPlaceAssociation.class);
        config.addAnnotatedClass(UserLocationAssociation.class);
        config.configure("hibernate.cfg.xml");
        new SchemaExport(config).create(true, true);

    }
}
