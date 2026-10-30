package com.nicholaswalsh.canadaweathercollection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CanadaWeatherCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanadaWeatherCollectionApplication.class, args);
    }

}
