package nl.adeda.reisplanner;

import java.io.Serializable;

/**
 * Created by Antonio on 24-3-2017.
 */

public class ReisDirections implements Serializable {

    private String time;
    private String station;
    private String platform;

    public String getTime() { return time; }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
