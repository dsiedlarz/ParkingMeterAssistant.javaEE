package edu.dsiedlarz.ParkingMeterAssistant.api.rest.external;

import com.sun.net.httpserver.HttpServer;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import edu.dsiedlarz.ParkingMeterAssistant.bean.LocationBean;
import edu.dsiedlarz.ParkingMeterAssistant.bean.PlaceBean;
import edu.dsiedlarz.ParkingMeterAssistant.model.Location;
import edu.dsiedlarz.ParkingMeterAssistant.model.ParkingPlace;
import edu.dsiedlarz.ParkingMeterAssistant.model.PlaceStats;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by private on 12.06.2017.
 */
@Path("/api/external/")
public class PlacesExternalApi {

    @EJB
    private LocationBean locationBean;

    @EJB
    private PlaceBean placeBean;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("places/stats")
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("location/{id}/places/stats/")
    public String getStats(@QueryParam("id") int id) {
        Location location = locationBean.getLocation(id);

        PlaceStats placeStats = placeBean.getPlacesStatus(location);
        return placeStats.toJsonObject().toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/places")
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


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("location/{id}/places/")
    public String getLocationPlaces(@QueryParam("id") int id) {
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
