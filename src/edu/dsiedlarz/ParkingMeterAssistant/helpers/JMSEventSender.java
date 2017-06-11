package edu.dsiedlarz.ParkingMeterAssistant.helpers;

import edu.dsiedlarz.ParkingMeterAssistant.model.Event;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by private on 04.06.2017.
 */
@Default
@ApplicationScoped
public class JMSEventSender {
    //[java:/jms/topic/parking_meter_assistant_topic]
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory cf;
    @Resource(mappedName = "java:/jms/topic/parking_meter_assistant_topic")
    private Topic queueExample;
    private Connection connection;


    public JMSEventSender() {
    }

    public void sendEvent(Event event) {
        try {
            connection = cf.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer publisher = session.createProducer(queueExample);
            connection.start();
            TextMessage message = session.createTextMessage(event.getData().toString());
            message.setStringProperty("category", event.getCategory());
            publisher.send(message);
        } catch (Exception exc) {
            System.out.println("Błąd! " + exc);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (JMSException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }


}
