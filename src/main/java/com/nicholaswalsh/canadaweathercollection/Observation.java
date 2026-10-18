package com.nicholaswalsh.canadaweathercollection;

import jakarta.persistence.*;

@Entity
public class Observation {
    @EmbeddedId
    private ObservationId obsId = new ObservationId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("stationID")
    private Station station;

    private Double temperature;

    private Double humidity;

    private Double avgWindSpeed;

    public Observation() {

    }

    public ObservationId getObsId() {
        return obsId;
    }

    public void setObsId(final ObservationId obsId) {
        this.obsId = obsId;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(final Station station) {
        this.station = station;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(final Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(final Double humidity) {
        this.humidity = humidity;
    }

    public Double getAvgWindSpeed() {
        return avgWindSpeed;
    }

    public void setAvgWindSpeed(final Double avgWindSpeed) {
        this.avgWindSpeed = avgWindSpeed;
    }
}
