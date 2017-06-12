package edu.dsiedlarz.ParkingMeterAssistant.model;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by private on 07.06.2017.
 */
public class PlaceStats {
    private int free = 0;
    private int taken = 0;
    private int paid = 0;
    private int suspicious = 0;

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getTaken() {
        return taken;
    }

    public void setTaken(int taken) {
        this.taken = taken;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getSuspicious() {
        return suspicious;
    }

    public void setSuspicious(int suspicious) {
        this.suspicious = suspicious;
    }

    @Override
    public String toString() {
        return "PlaceStats{" +
                "free=" + free +
                ", taken=" + taken +
                '}';
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("free", free);
            jsonObject.put("taken", taken);
            jsonObject.put("paid", paid);
            jsonObject.put("suspicious", suspicious);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
