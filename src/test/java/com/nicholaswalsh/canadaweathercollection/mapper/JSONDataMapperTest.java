package com.nicholaswalsh.canadaweathercollection.mapper;

import com.nicholaswalsh.canadaweathercollection.model.Observation;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JSONDataMapperTest {

    @Test
    void test() throws IOException {
        final String testFile = "swob-sample.json";

        Resource resource = new ClassPathResource(testFile);

        JSONDataMapper dataMapper = new JSONDataMapper();
        List<Observation> observationList = dataMapper.mapJSONData(resource.getContentAsString(StandardCharsets.UTF_8));

        assertEquals(5, observationList.size());

        String stationID = observationList.get(0).getStation().getStationID();
        assertTrue(stationID.equalsIgnoreCase("7024745"));
    }
}
