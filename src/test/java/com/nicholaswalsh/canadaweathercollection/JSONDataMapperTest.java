package com.nicholaswalsh.canadaweathercollection;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JSONDataMapperTest {

    @Test
    void test() throws IOException {
        final String testFile = "swob-sample.json";

        Resource resource = new ClassPathResource(testFile);

        JSONDataMapper dataMapper = new JSONDataMapper();
        dataMapper.mapJSONData(resource.getContentAsString(StandardCharsets.UTF_8));
    }
}
