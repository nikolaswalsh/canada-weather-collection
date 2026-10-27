package com.nicholaswalsh.canadaweathercollection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
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
        final String response = client.getObservations();

        JSONDataMapper mapper = new JSONDataMapper();
        List<Observation> mappedData = mapper.mapJSONData(response);
        Set<Station> stations = new HashSet<>();

        for (Observation  observation : mappedData) {
            Station station = observation.getStation();
            stations.add(station);
        }

        stationRepository.saveAll(stations);

        observationRepository.saveAll(mappedData);
    }

}
