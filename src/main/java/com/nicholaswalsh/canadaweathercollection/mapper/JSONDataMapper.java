package com.nicholaswalsh.canadaweathercollection;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONDataMapper {

    public JSONDataMapper() {

    }

    public List<Observation> mapJSONData(final String json) {
        List<Observation> list = new ArrayList<>();
        Map<String, Station> stationMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(json);

        for (JsonNode featureNode : node.path("features")) {
            JsonNode geometryNode = featureNode.path("geometry");
            JsonNode coordinatesNode = geometryNode.path("coordinates");
            Double longitude = coordinatesNode.path(0).asDouble();
            Double latitude = coordinatesNode.path(1).asDouble();

            JsonNode propertiesNode = featureNode.path("properties");
            String obsDateStr = propertiesNode.path("date_tm-value").asString();
            Instant obsDateTime = Instant.parse(obsDateStr);
            String stationID = propertiesNode.path("msc_id-value").asString();
            String stationName = propertiesNode.path("stn_nam-value").asString();
            Double temperature = asDouble(propertiesNode, "air_temp");
            Double humidity = asDouble(propertiesNode, "rel_hum");
            Double avgWindSpeed = asDouble(propertiesNode, "avg_wnd_spd_10m_pst1mt");

            Station station;
            if (stationMap.containsKey(stationID)) {
                station = stationMap.get(stationID);
            }
            else {
                station = new Station();
                station.setStationID(stationID);
                station.setStationName(stationName);
                station.setLongitude(longitude);
                station.setLatitude(latitude);
                stationMap.put(stationID, station);
            }

            ObservationId observationId = new ObservationId(stationID, obsDateTime);

            Observation observation = new Observation();
            observation.setObsId(observationId);
            observation.setStation(station);
            observation.setTemperature(temperature);
            observation.setHumidity(humidity);
            observation.setAvgWindSpeed(avgWindSpeed);

            list.add(observation);
        }

        return list;
    }

    private Double asDouble(final JsonNode node, final String fieldName) {
        if (node.path(fieldName).isMissingNode() || node.path(fieldName).isNull()) {
            return null;
        }
        else {
            return node.path(fieldName).asDouble();
        }
    }
}
