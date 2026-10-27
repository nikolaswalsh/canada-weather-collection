package com.nicholaswalsh.canadaweathercollection.client;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Disabled
public class MSCClientTest {

    @Test
    void test() throws IOException {
        final MSCClient client = new MSCClient("https://api.weather.gc.ca");
        final String response = client.getObservations();

        assertFalse(response.isBlank());
    }
}
