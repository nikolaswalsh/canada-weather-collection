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
import java.util.Collection;

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

        when(client.getObservations()).thenReturn(json);

        service.ingest();

        verify(stationRepository).saveAll(stationCaptor.capture());
        verify(observationRepository).saveAll(observationCaptor.capture());

        assertEquals(5, stationCaptor.getValue().size());
        assertEquals(5, observationCaptor.getValue().size());
    }

    @Test
    void ingestEmptyStationsAndObservations() throws IOException {
        when(client.getObservations()).thenReturn("{\"type\": \"FeatureCollection\",\"features\": []}");

        service.ingest();

        verifyNoInteractions(stationRepository, observationRepository);
    }
}
