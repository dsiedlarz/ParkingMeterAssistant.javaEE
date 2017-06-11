package edu.dsiedlarz.ParkingMeterAssistant.Simulator;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import edu.dsiedlarz.ParkingMeterAssistant.helpers.JMSEventSender;
import org.apache.tools.ant.util.DateUtils;
import org.codehaus.jettison.json.JSONObject;
import org.jboss.security.Util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by private on 04.06.2017.
 */
public class TicketMachine {


    public static void main(String[] args) {

        try {

            Client client = Client.create();

            WebResource webResource = client
                    .resource("http://localhost:8080/ParkingMeterAssistant_war_exploded/rest/tickets");

            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.now().plusHours(3);


            JSONObject ticket = new JSONObject();
            ticket.put("price", 3.5);
            ticket.put("startTime", Timestamp.valueOf(startDate).getTime());
            ticket.put("endTime", Timestamp.valueOf(endDate).getTime());

            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, ticket);
//
//            if (response.getStatus() != 201) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + response.getStatus());
//            }



            System.out.println("Output from Server .... \n");
            String output = response.getEntity(String.class);
            System.out.println(output);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
