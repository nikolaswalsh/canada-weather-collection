package com.nicholaswalsh.canadaweathercollection.scheduler;

import com.nicholaswalsh.canadaweathercollection.service.IngestionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IngestionScheduler {

    private final IngestionService service;

    public IngestionScheduler(final IngestionService service) {
        this.service = service;
    }

    @Scheduled(fixedDelayString = "${ingestion.interval}")
    public void run() {
        service.ingest();
    }
}
