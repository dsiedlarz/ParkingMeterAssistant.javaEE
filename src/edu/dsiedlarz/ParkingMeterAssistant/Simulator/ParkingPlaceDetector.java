package edu.dsiedlarz.ParkingMeterAssistant.Simulator;

import edu.dsiedlarz.ParkingMeterAssistant.api.soap.ParkingPlaceDetectorApi;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by private on 03.06.2017.
 */
public class ParkingPlaceDetector {

    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://localhost:8080/ParkingMeterAssistant_war_exploded/ParkingPlaceDetectorApiImpl?wsdl");
        QName qname = new QName("http://soap.api.ParkingMeterAssistant.dsiedlarz.edu/", "ParkingPlaceDetectorApiImplService");


        Service service = Service.create(url, qname);
        ParkingPlaceDetectorApi parkingPlaceDetectorApi = service.getPort(ParkingPlaceDetectorApi.class);

        System.out.println(parkingPlaceDetectorApi.tookPlace( 134));
        System.out.println(parkingPlaceDetectorApi.freePlace( 523));
}
}
