package edu.dsiedlarz.ParkingMeterAssistant.dashboard;

import edu.dsiedlarz.ParkingMeterAssistant.api.rest.TicketsApi;
import edu.dsiedlarz.ParkingMeterAssistant.helpers.JMSEventSender;
import edu.dsiedlarz.ParkingMeterAssistant.model.Event;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;

/**
 * Created by private on 04.06.2017.
 */
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

    final static Logger logger = Logger.getLogger(EventHandler.class);

    @Override
    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;



        try {
            logger.info("received: " + msg.getText());

            Event event = new Event();
            JSONObject data= new JSONObject();
            data.put("message", "siema siema");
            event.setData(data);
            event.setCategory(Event.EVENT_NOTIFICATION);

            eventSender.sendEvent(event);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

