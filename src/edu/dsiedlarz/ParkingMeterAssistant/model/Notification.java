package edu.dsiedlarz.ParkingMeterAssistant.model;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by private on 04.06.2017.
 */
@Entity
public class Notification {
    @Id
    @GeneratedValue
    private int id;

    private Date time;
    private boolean active;
    private String message;
    @ManyToOne
    private ParkingPlace parkingPlace;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ParkingPlace getParkingPlace() {
        return parkingPlace;
    }

    public void setParkingPlace(ParkingPlace parkingPlace) {
        this.parkingPlace = parkingPlace;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("time", time.getTime());
            jsonObject.put("active", active);
            jsonObject.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
