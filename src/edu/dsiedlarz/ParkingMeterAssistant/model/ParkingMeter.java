package edu.dsiedlarz.ParkingMeterAssistant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by private on 03.06.2017.
 */
@Entity
public class ParkingMeter {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Location location;

    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
