package com.nicholaswalsh.canadaweathercollection.service;

import com.nicholaswalsh.canadaweathercollection.model.Observation;
import com.nicholaswalsh.canadaweathercollection.model.Station;
import com.nicholaswalsh.canadaweathercollection.client.MSCClient;
import com.nicholaswalsh.canadaweathercollection.mapper.JSONDataMapper;
import com.nicholaswalsh.canadaweathercollection.repository.ObservationRepository;
import com.nicholaswalsh.canadaweathercollection.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class IngestionService {

    private final StationRepository stationRepository;

    private final ObservationRepository observationRepository;

    private final MSCClient client;

    public IngestionService(final MSCClient client, final StationRepository stationRepository,
                            final ObservationRepository observationRepository) {
        this.client = client;
        this.stationRepository = stationRepository;
        this.observationRepository = observationRepository;
    }

    @Transactional
    public void ingest() {
        int offset = 0;
        boolean newPagesExist = true;
        Optional<Instant> latestTimeOpt = observationRepository.findLatestObservationTime();
        Instant latestTime = latestTimeOpt.orElse(Instant.now().minus(1, ChronoUnit.HOURS));

        while (newPagesExist) {
            final String response = client.getObservations(latestTime, offset);
            JSONDataMapper mapper = new JSONDataMapper();
            JSONDataMapper.MappedPage mappedPage = mapper.mapJSONData(response);
            List<Observation> mappedData = mappedPage.observations();
            if (mappedData.isEmpty()) {
                return;
            }
            if (offset + mappedPage.returned() < mappedPage.matched()) {
                offset += mappedPage.returned();
            }
            else {
                newPagesExist = false;
            }

            Set<Station> stations = new HashSet<>();

            for (Observation observation : mappedData) {
                Station station = observation.getStation();
                stations.add(station);
            }

            stationRepository.saveAll(stations);

            observationRepository.saveAll(mappedData);
        }
    }

}
