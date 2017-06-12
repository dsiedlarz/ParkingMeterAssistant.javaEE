package edu.dsiedlarz.ParkingMeterAssistant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by private on 11.06.2017.
 */
@Entity
public class UserLocationAssociation {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Location location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
