package com.nicholaswalsh.canadaweathercollection.service;

import com.nicholaswalsh.canadaweathercollection.client.MSCClient;
import com.nicholaswalsh.canadaweathercollection.model.Observation;
import com.nicholaswalsh.canadaweathercollection.model.Station;
import com.nicholaswalsh.canadaweathercollection.repository.ObservationRepository;
import com.nicholaswalsh.canadaweathercollection.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngestionServiceTest {

    @Mock
    private MSCClient client;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private ObservationRepository observationRepository;

    private IngestionService service;

    @Captor
    private ArgumentCaptor<Collection<Station>> stationCaptor;

    @Captor
    private ArgumentCaptor<Collection<Observation>> observationCaptor;

    @BeforeEach
    public void setUp() {
        service = new IngestionService(client, stationRepository, observationRepository);
    }

    @Test
    void ingestSavesStationsAndObservations() throws IOException {
        final String testFile = "swob-sample.json";
        Resource resource = new ClassPathResource(testFile);
        final String json = resource.getContentAsString(StandardCharsets.UTF_8);

        int offset = 0;
        Instant latestTime = Instant.parse("2026-11-01T20:00:00Z");
        when(observationRepository.findLatestObservationTime()).thenReturn(Optional.of(latestTime));
        when(client.getObservations(latestTime, offset)).thenReturn(json);

        service.ingest();

        verify(stationRepository).saveAll(stationCaptor.capture());
        verify(observationRepository).saveAll(observationCaptor.capture());

        assertEquals(5, stationCaptor.getValue().size());
        assertEquals(5, observationCaptor.getValue().size());
    }

    @Test
    void ingestEmptyStationsAndObservations() {
        int offset = 0;
        Instant latestTime = Instant.parse("2026-11-01T20:00:00Z");
        when(observationRepository.findLatestObservationTime()).thenReturn(Optional.of(latestTime));

        when(client.getObservations(latestTime, offset)).thenReturn("{\"type\": \"FeatureCollection\",\"features\": []}");

        service.ingest();

        verify(stationRepository, never()).saveAll(any());
        verify(observationRepository, never()).saveAll(any());
    }
}
