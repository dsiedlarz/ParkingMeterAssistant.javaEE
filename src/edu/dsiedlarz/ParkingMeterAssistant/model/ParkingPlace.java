package edu.dsiedlarz.ParkingMeterAssistant.model;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by private on 03.06.2017.
 */
@Entity
public class ParkingPlace {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Location location;
    private ParkingPlaceState state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ParkingPlaceState getState() {
        return state;
    }

    public void setState(ParkingPlaceState state) {
        this.state = state;
    }

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("location", location.getId());
            jsonObject.put("state", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
