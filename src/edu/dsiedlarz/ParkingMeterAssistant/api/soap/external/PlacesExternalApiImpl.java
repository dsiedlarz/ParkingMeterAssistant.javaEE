package edu.dsiedlarz.ParkingMeterAssistant.api.soap.external;


import edu.dsiedlarz.ParkingMeterAssistant.api.soap.ParkingPlaceDetectorApi;
import edu.dsiedlarz.ParkingMeterAssistant.bean.LocationBean;
import edu.dsiedlarz.ParkingMeterAssistant.bean.PlaceBean;
import edu.dsiedlarz.ParkingMeterAssistant.helpers.HibernateSessionFactory;
import edu.dsiedlarz.ParkingMeterAssistant.helpers.JMSEventSender;
import edu.dsiedlarz.ParkingMeterAssistant.model.*;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

//@SuppressWarnings("ALL")
@WebService(endpointInterface = "edu.dsiedlarz.ParkingMeterAssistant.api.soap.external.PlacesExternalApi")
public class PlacesExternalApiImpl implements PlacesExternalApi {

    @EJB
    private LocationBean locationBean;

    @EJB
    private PlaceBean placeBean;

    public String getStats() {
        ArrayList<Location> locations = locationBean.getLocations();
        JSONObject response = new JSONObject();
        for (Location l :
                locations) {

            PlaceStats placeStats = placeBean.getPlacesStatus(l);
            try {
                response.put(l.getName(), placeStats.toJsonObject());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return response.toString();
    }

    public String getStatsByLocation(int id) {
        Location location = locationBean.getLocation(id);

        PlaceStats placeStats = placeBean.getPlacesStatus(location);
        return placeStats.toJsonObject().toString();
    }

    public String getPlaces() {
        ArrayList<Location> locations = locationBean.getLocations();
        JSONObject response = new JSONObject();
        for (Location l :
                locations) {

            ArrayList<ParkingPlace> parkingPlaces = placeBean.getPlaces(l);
            JSONArray places = new JSONArray();
            for (ParkingPlace p:
                    parkingPlaces) {

                places.put(p.toJsonObject());

            }
            try {
                response.put(l.getName(), places);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return response.toString();
    }


    public String getLocationPlaces(int id) {
        Location location = locationBean.getLocation(id);
        ArrayList<ParkingPlace> parkingPlaces = placeBean.getPlaces(location);
        JSONObject response = new JSONObject();

        JSONArray places = new JSONArray();
        for (ParkingPlace p:
                parkingPlaces) {

            places.put(p.toJsonObject());

        }
        try {
            response.put(location.getName(), places);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}
