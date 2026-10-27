package com.nicholaswalsh.canadaweathercollection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CanadaWeatherCollectionApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("DB_URL=[" + System.getenv("DB_URL") + "]");
    }

}
