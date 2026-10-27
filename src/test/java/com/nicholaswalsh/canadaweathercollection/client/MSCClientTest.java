package com.nicholaswalsh.canadaweathercollection;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MSCClientTest {

    @Test
    void test() throws IOException {
        final MSCClient client = new MSCClient("https://api.weather.gc.ca");
        final String response = client.getObservations();
    }
}
