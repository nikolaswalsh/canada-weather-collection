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
import java.util.List;
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

    @Test
    void ingestPagesStationsAndObservations() throws IOException {
        final String testFile1 = "swob-page1.json";
        final String testFile2 = "swob-page2.json";
        Resource resource1 = new ClassPathResource(testFile1);
        Resource resource2 = new ClassPathResource(testFile2);
        final String json1 = resource1.getContentAsString(StandardCharsets.UTF_8);
        final String json2 = resource2.getContentAsString(StandardCharsets.UTF_8);

        int offset1 = 0;
        int offset2 = 2;
        Instant latestTime = Instant.parse("2026-11-01T20:00:00Z");
        when(observationRepository.findLatestObservationTime()).thenReturn(Optional.of(latestTime));

        when(client.getObservations(latestTime, offset1)).thenReturn(json1);
        when(client.getObservations(latestTime, offset2)).thenReturn(json2);

        service.ingest();

        verify(stationRepository, times(2)).saveAll(stationCaptor.capture());
        List<Collection<Station>> capturedStations = stationCaptor.getAllValues();
        assertEquals(2, capturedStations.get(0).size());
        assertEquals(1, capturedStations.get(1).size());

        verify(observationRepository, times(2)).saveAll(observationCaptor.capture());
        List<Collection<Observation>> capturedObservations = observationCaptor.getAllValues();
        assertEquals(2, capturedObservations.get(0).size());
        assertEquals(1, capturedObservations.get(1).size());

        verify(client).getObservations(latestTime, 0);
        verify(client).getObservations(latestTime, 2);
        verifyNoMoreInteractions(client);
    }
}
