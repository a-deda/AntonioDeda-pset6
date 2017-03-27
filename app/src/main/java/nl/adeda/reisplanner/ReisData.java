package nl.adeda.reisplanner;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Antonio on 24-3-2017.
 */

public class ReisData implements Serializable {

    // Default constructor for Firebase
    public ReisData() {  }

    private String key;
    private String departure;
    private String arrival;
    private String changes;
    private String travelTime;
    private ArrayList<ReisDirections> directions;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public ArrayList<ReisDirections> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<ReisDirections> directions) {
        this.directions = directions;
    }
}
