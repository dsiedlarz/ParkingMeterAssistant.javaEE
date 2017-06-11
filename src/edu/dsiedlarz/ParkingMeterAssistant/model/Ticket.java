package edu.dsiedlarz.ParkingMeterAssistant.model;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by private on 03.06.2017.
 */

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private int id;
    private Date startTime;
    private Date endTime;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("price", 3.5);
            jsonObject.put("startTime", startTime.getTime());
            jsonObject.put("endTime", endTime.getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  jsonObject;
    }
}
