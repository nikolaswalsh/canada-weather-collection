package com.nicholaswalsh.canadaweathercollection;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Station {

    @Id
    private String stationID;

    private String stationName;

    private Double longitude;

    private Double latitude;

    public String getStationID() {
        return stationID;
    }

    public void setStationID(final String stationID) {
        this.stationID = stationID;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(final String stationName) {
        this.stationName = stationName;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }
}
