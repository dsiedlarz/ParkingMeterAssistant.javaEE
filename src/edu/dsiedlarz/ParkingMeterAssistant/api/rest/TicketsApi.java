package edu.dsiedlarz.ParkingMeterAssistant.api.rest;

import edu.dsiedlarz.ParkingMeterAssistant.bean.LocationBean;
import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.helpers.JMSEventSender;
import edu.dsiedlarz.ParkingMeterAssistant.model.Event;
import edu.dsiedlarz.ParkingMeterAssistant.model.Location;
import edu.dsiedlarz.ParkingMeterAssistant.model.ParkingPlace;
import edu.dsiedlarz.ParkingMeterAssistant.model.Ticket;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by private on 03.06.2017.
 */
@Path("/tickets")
public class TicketsApi {
    final static Logger logger = Logger.getLogger(TicketsApi.class);

    @Inject
    JMSEventSender eventSender;

    @EJB
    private LocationBean locationBean;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTicket(JSONObject data) {
        try {
            logger.info("postTicket");

            Ticket ticket = createTicket(data);
            saveTicket(ticket);
            sendEvent(ticket);

            return Response.status(201).entity(data).build();
        } catch (JSONException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.error(e);
        }
        return Response.status(400).entity(data).build();
    }

    private Ticket createTicket(JSONObject data) throws JSONException {

        long startTime = new Date().getTime();
        long endTime =  startTime + data.getLong("time")*1000;

        int locationId = data.getInt("location");
        Location location = locationBean.getLocation(locationId);
        Ticket ticket = new Ticket();

        ticket.setLocation(location);
        ticket.setPrice(1);
        ticket.setStartTime(new Date(startTime));
        ticket.setEndTime(new Date(endTime));

        return ticket;
    }

    private void saveTicket(Ticket ticket) {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (org.hibernate.HibernateException he) {
            session = sessionFactory.openSession();
        }
        Transaction tx = session.beginTransaction();

        session.saveOrUpdate(ticket);
        logger.info("save ticket");
        logger.info(ticket.toJsonObject().toString());
        tx.commit();
        session.close();
    }

    private void sendEvent(Ticket ticket) {
        Event event = new Event();
        event.setCategory(Event.EVENT_TICKET);
        event.setData(ticket.toJsonObject());

        eventSender.sendEvent(event);
    }
}
