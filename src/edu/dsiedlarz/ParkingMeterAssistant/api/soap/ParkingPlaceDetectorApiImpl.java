package edu.dsiedlarz.ParkingMeterAssistant.api.soap;


import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.helpers.JMSEventSender;
import edu.dsiedlarz.ParkingMeterAssistant.model.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.inject.Inject;
import javax.jws.WebService;

//@SuppressWarnings("ALL")
@WebService(endpointInterface = "edu.dsiedlarz.ParkingMeterAssistant.api.soap.ParkingPlaceDetectorApi")
public class ParkingPlaceDetectorApiImpl implements ParkingPlaceDetectorApi {

    @Inject
    JMSEventSender eventSender;

    @Override
    public boolean tookPlace(int id) {
        return changeParkingPlaceStatus(id, ParkingPlaceState.TAKEN);
    }

    @Override
    public boolean freePlace(int id) {
        return changeParkingPlaceStatus(id, ParkingPlaceState.FREE);
    }

    private boolean changeParkingPlaceStatus(int id, ParkingPlaceState state) {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();
        //noinspection JpaQlInspection
        Query query = session.createQuery("from ParkingPlace WHERE id = " + id);
        boolean response;
        if (query.list().size() == 1) {
            ParkingPlace parkingPlace = (ParkingPlace) query.list().get(0);
            parkingPlace.setState(state);
            session.saveOrUpdate(parkingPlace);
            sendEvent(id, state);
            response = true;
        } else {
            response = false;
        }
        tx.commit();
        session.close();
        return response;
    }

    private void sendEvent(int id, ParkingPlaceState state) {
        Event event = new Event();
        String category = null;
        switch (state) {
            case FREE:
                category = Event.EVENT_RELEASE_PLACE;
                break;
            case TAKEN:
                category = Event.EVENT_TAKE_PLACE;
                break;
        }

        event.setCategory(category);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event.setData(jsonObject);

        eventSender.sendEvent(event);
    }
}
