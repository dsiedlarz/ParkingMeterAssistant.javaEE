package edu.dsiedlarz.ParkingMeterAssistant.dashboard;

import edu.dsiedlarz.ParkingMeterAssistant.bean.LocationBean;
import edu.dsiedlarz.ParkingMeterAssistant.bean.PlaceBean;
import edu.dsiedlarz.ParkingMeterAssistant.bean.TicketBean;
import edu.dsiedlarz.ParkingMeterAssistant.bean.TicketPlaceAssociationBean;
import edu.dsiedlarz.ParkingMeterAssistant.helpers.JMSEventSender;
import edu.dsiedlarz.ParkingMeterAssistant.model.*;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by private on 04.06.2017.
 */
@Startup
@MessageDriven(mappedName = "event_handler", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
                propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType",
                propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination",
                propertyValue = "java:/jms/topic/parking_meter_assistant_topic")
        ,
        @ActivationConfigProperty(propertyName = "messageSelector",
                propertyValue = "category IN ('ticket', 'release_place','take_place')")
})
public class EventHandler implements MessageListener {


    @Inject
    JMSEventSender eventSender;

    @EJB
    private PlaceBean placeBean;

    @EJB
    private LocationBean locationBean;

    @EJB
    private TicketPlaceAssociationBean ticketPlaceAssociationBean;

    @EJB
    TicketBean ticketBean;

    final static Logger logger = Logger.getLogger(EventHandler.class);

    @Override
    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;

        try {
            logger.info("received: " + msg.getText());
            JSONObject event = new JSONObject(msg.getText());
            String cateory = msg.getStringProperty("category");

            switch (cateory) {
                case Event.EVENT_RELEASE_PLACE:
                    onReleasePlace(event);
                    break;
                case Event.EVENT_TAKE_PLACE:
                    onTakePlace(event);
                    break;
                case Event.EVENT_TICKET:
                    onTicket(event);
                    break;
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onTicket(JSONObject event) {

        logger.info("onTicket");


        int locationId;
        int ticketId;

        try {
            locationId = event.getInt("location");
            ticketId = event.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Location location = locationBean.getLocation(locationId);

        Ticket ticket = ticketBean.getTicket(ticketId);
        TicketPlaceAssociation association = ticketPlaceAssociationBean.getOldestAssociation(location);

        association.setTicket(ticket);

        ParkingPlace parkingPlace = association.getParkingPlace();
        parkingPlace.setState(ParkingPlaceState.PAID);


        ticketPlaceAssociationBean.saveAssociation(association);

        placeBean.saveParkingPlace(parkingPlace);

    }

    private void onReleasePlace(JSONObject event) {
        int id = 0;
        try {
            id = event.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        TicketPlaceAssociation association = ticketPlaceAssociationBean.getAssociation(id);
        if (association != null) {
            association.setActive(false);
            ticketPlaceAssociationBean.saveAssociation(association);
        }
    }

    private void onTakePlace(JSONObject event) {
        logger.info("on ticket");
        int id = 0;
        try {
            id = event.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        ParkingPlace parkingPlace = placeBean.getParkingPlace(id);

        TicketPlaceAssociation association = new TicketPlaceAssociation();
        association.setParkingPlace(parkingPlace);
        association.setActive(true);
        association.setTime(new Date());

        ticketPlaceAssociationBean.saveAssociation(association);
    }

    @Schedule(hour = "*", minute = "*", second = "*/5", info = "Every 5 seconds timer")
    private void findCheaters() {
        logger.info("findCheaters");

        checkNewOnes();
        checkLateComers();

    }

    private void checkNewOnes(){

        ArrayList<TicketPlaceAssociation> cheaters = ticketPlaceAssociationBean.getNewCheaters(20);

        for (TicketPlaceAssociation cheater:
             cheaters) {
            ParkingPlace parkingPlace = cheater.getParkingPlace();
            parkingPlace.setState(ParkingPlaceState.SUSPICIOUS);
            cheater.setActive(false);

            ticketPlaceAssociationBean.saveAssociation(cheater);
            placeBean.saveParkingPlace(parkingPlace);
            Event event = new Event();
            JSONObject data= new JSONObject();
            try {
                data.put("message", "New");
                data.put("parkingPlaceId", parkingPlace.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            event.setData(data);
            event.setCategory(Event.EVENT_NOTIFICATION);

            eventSender.sendEvent(event);
        }
    }


    private void checkLateComers(){

        ArrayList<TicketPlaceAssociation> cheaters = ticketPlaceAssociationBean.getLateComers(10);

        for (TicketPlaceAssociation cheater:
                cheaters) {
            ParkingPlace parkingPlace = cheater.getParkingPlace();
            parkingPlace.setState(ParkingPlaceState.SUSPICIOUS);
            cheater.setActive(false);

            ticketPlaceAssociationBean.saveAssociation(cheater);
            placeBean.saveParkingPlace(parkingPlace);
            Event event = new Event();
            JSONObject data= new JSONObject();
            try {
                data.put("message", "Latecomer");
                data.put("parkingPlaceId", parkingPlace.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            event.setData(data);
            event.setCategory(Event.EVENT_NOTIFICATION);

            eventSender.sendEvent(event);
        }
    }

}

