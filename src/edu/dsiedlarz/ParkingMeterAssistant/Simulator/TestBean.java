package edu.dsiedlarz.ParkingMeterAssistant.Simulator;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import edu.dsiedlarz.ParkingMeterAssistant.api.soap.ParkingPlaceDetectorApi;
import edu.dsiedlarz.ParkingMeterAssistant.api.soap.external.PlacesExternalApi;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by private on 04.06.2017.
 */
@ManagedBean(name = "testBean", eager = true)
@ApplicationScoped
public class TestBean {

    public void takePlace(int id) {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/ParkingMeterAssistant_war_exploded/ParkingPlaceDetectorApiImpl?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        QName qname = new QName("http://soap.api.ParkingMeterAssistant.dsiedlarz.edu/", "ParkingPlaceDetectorApiImplService");


        Service service = Service.create(url, qname);
        ParkingPlaceDetectorApi parkingPlaceDetectorApi = service.getPort(ParkingPlaceDetectorApi.class);

        parkingPlaceDetectorApi.tookPlace(id);
    }

    public void freePlace(int id) {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/ParkingMeterAssistant_war_exploded/ParkingPlaceDetectorApiImpl?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        QName qname = new QName("http://soap.api.ParkingMeterAssistant.dsiedlarz.edu/", "ParkingPlaceDetectorApiImplService");


        Service service = Service.create(url, qname);
        ParkingPlaceDetectorApi parkingPlaceDetectorApi = service.getPort(ParkingPlaceDetectorApi.class);

        parkingPlaceDetectorApi.freePlace(id);
    }


    public void buyTicket(int location, int time) {
        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:8080/ParkingMeterAssistant_war_exploded/rest/tickets");


        String input = "{\n" +
                "\t\"time\": " + time + "\n" +
                "\t\"location\": " + location + "\n" +
                "}";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("time", time);
            jsonObject.put("location", location);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(jsonObject.toString());

        ClientResponse response = webResource.accept("application/json").type("application/json")
                .post(ClientResponse.class, jsonObject.toString());

//        if (response.getStatus() != 200) {
//            throw new RuntimeException("Failed : HTTP error code : "
//                    + response.getStatus());
//        }

        String output = response.getEntity(String.class);

        System.out.println("status .... " + response.getStatus());
        System.out.println("Output from Server .... \n");
        System.out.println(output);
    }


    public String testRestGetPlaces() {

        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:8080/ParkingMeterAssistant_war_exploded/rest/api/external/places");


        ClientResponse response = webResource.accept("application/json").type("application/json")
                .get(ClientResponse.class);
        
        String output = response.getEntity(String.class);

        System.out.println("status .... " + response.getStatus());
        System.out.println("Output from Server .... \n");
        System.out.println(output);

        return output;
    }


    public String testSoapGetPlaceStats() {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/ParkingMeterAssistant_war_exploded/PlacesExternalApiImpl?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        QName qname = new QName("http://external.soap.api.ParkingMeterAssistant.dsiedlarz.edu/", "PlacesExternalApiImplService");


        Service service = Service.create(url, qname);
        PlacesExternalApi placesExternalApi = service.getPort(PlacesExternalApi.class);

        return placesExternalApi.getStats();
    }
}
