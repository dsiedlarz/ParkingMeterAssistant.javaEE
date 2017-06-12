package edu.dsiedlarz.ParkingMeterAssistant.api.rest;

import edu.dsiedlarz.ParkingMeterAssistant.bean.NotificationBean;
import edu.dsiedlarz.ParkingMeterAssistant.bean.PlaceBean;
import edu.dsiedlarz.ParkingMeterAssistant.dashboard.EventHandler;
import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.model.Event;
import edu.dsiedlarz.ParkingMeterAssistant.model.Notification;
import edu.dsiedlarz.ParkingMeterAssistant.model.ParkingPlace;
import edu.dsiedlarz.ParkingMeterAssistant.model.Ticket;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by private on 04.06.2017.
 */
@MessageDriven(mappedName = "notification_handler", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
                propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType",
                propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination",
                propertyValue = "java:/jms/topic/parking_meter_assistant_topic")
        ,
        @ActivationConfigProperty(propertyName = "messageSelector",
                propertyValue = "category = '" + Event.EVENT_NOTIFICATION + "'")
})
@Path("/notifications")
public class NotificationHandler implements MessageListener {

    final static Logger logger = Logger.getLogger(EventHandler.class);

    @EJB
    private NotificationBean notificationBean;

    @EJB
    private PlaceBean placeBean;

    @Override
    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;

        try {
            JSONObject event = new JSONObject(msg.getText());

            String category = msg.getStringProperty("category");

            int parkingPlaceId = event.getInt("parkingPlaceId");

            ParkingPlace parkingPlace = placeBean.getParkingPlace(parkingPlaceId);
            Notification notification = new Notification();
            notification.setMessage(event.getString("message"));
            notification.setParkingPlace(parkingPlace);
            notification.setActive(true);
            notification.setTime(new Date());

            saveNotification(notification);
            logger.info("notification: " + msg.getText());

        } catch (JMSException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveNotification(Notification notification) {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        session.saveOrUpdate(notification);
        tx.commit();
        session.close();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getNotifications() {
        ArrayList<Notification> movies = new ArrayList<>();
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        String hql = "from Notification";
        List result = session.createQuery(hql)
                .list();

        JSONArray jsonArray = new JSONArray();
        for (Object aResult : result) {
            Notification notification = ((Notification) aResult);
            jsonArray.put(notification.toJSONObject());
        }

        tx.commit();
        session.close();
        return jsonArray.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getNotifications(@PathParam("id") int id) {
        ArrayList<Notification> movies = new ArrayList<>();
        Notification notification =notificationBean.getNotification(id);
        return Response.ok(notification.toJSONObject().toString()).build();
    }
}
