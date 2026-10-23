package com.nicholaswalsh.canadaweathercollection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MSCClient {

    private final RestClient client;

    public MSCClient (@Value("${msc.geomet-url}") final String baseURL) {
        client = RestClient.builder()
                .baseUrl(baseURL)
                .build();
    }

    public String getObservations() {

        final String json = client.get()
                .uri(uriBuilder -> uriBuilder.path("/collections/swob-realtime/items")
                        .queryParam("f", "json")
                        .queryParam("limit", 20)
                        .queryParam("bbox", "-123.3,49.0,-122.5,49.5")
                        .queryParam("sortby", "-date_tm-value")
                        .build())
                .retrieve()
                .body(String.class);
        return json;
    }
}
