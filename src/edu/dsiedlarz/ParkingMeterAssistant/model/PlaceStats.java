package edu.dsiedlarz.ParkingMeterAssistant.model;

/**
 * Created by private on 07.06.2017.
 */
public class PlaceStats {
    int free = 0;
    int taken = 0;

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

    @Override
    public String toString() {
        return "PlaceStats{" +
                "free=" + free +
                ", taken=" + taken +
                '}';
    }
}
