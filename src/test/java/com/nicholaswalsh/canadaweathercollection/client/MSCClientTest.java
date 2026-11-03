package com.nicholaswalsh.canadaweathercollection.client;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Disabled
public class MSCClientTest {

    @Test
    void test() throws IOException {
        int offset = 0;
        Instant latestTime = Instant.now().minus(1, ChronoUnit.HOURS);
        final MSCClient client = new MSCClient("https://api.weather.gc.ca");
        final String response = client.getObservations(latestTime, offset);

        assertFalse(response.isBlank());
    }
}
