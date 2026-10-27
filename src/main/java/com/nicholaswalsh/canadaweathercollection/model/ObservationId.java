package com.nicholaswalsh.canadaweathercollection;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Embeddable
public class ObservationId implements Serializable {
    private String stationID;
    private Instant obsDateTime;

    public ObservationId() {}

    public ObservationId(final String stationID, final Instant obsDateTime) {
        this.stationID = stationID;
        this.obsDateTime = obsDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObservationId that = (ObservationId) o;
        return Objects.equals(stationID, that.stationID) && Objects.equals(obsDateTime, that.obsDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationID, obsDateTime);
    }

    public String getStationID() {
        return stationID;
    }

    public void setStationID(final String stationID) {
        this.stationID = stationID;
    }

    public Instant getObsDateTime() {
        return obsDateTime;
    }

    public void setObsDateTime(final Instant obsDateTime) {
        this.obsDateTime = obsDateTime;
    }
}
