package edu.dsiedlarz.ParkingMeterAssistant.model;

import org.codehaus.jettison.json.JSONObject;

/**
 * Created by private on 04.06.2017.
 */
public class Event {
    public static final String EVENT_TICKET = "ticket";
    public static final String EVENT_RELEASE_PLACE = "release_place";
    public static final String EVENT_TAKE_PLACE = "take_place";
    public static final String EVENT_NOTIFICATION = "notification";

    private String category;
    private JSONObject data;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
