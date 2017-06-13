package edu.dsiedlarz.ParkingMeterAssistant.dashboard;

/**
 * Created by private on 13.06.2017.
 */
import javax.faces.application.FacesMessage;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

@PushEndpoint("/notify")
public class NotifyResource {

    @OnMessage(encoders = {JSONEncoder.class})
    public FacesMessage onMessage(FacesMessage message) {
        System.out.println("message" + message.toString());
        return message;
    }

}