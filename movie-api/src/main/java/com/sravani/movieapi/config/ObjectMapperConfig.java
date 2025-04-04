package com.sravani.movieapi.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//to handle JSON serialization and deserialization globally.
//New fields in API responses won't break the application.
//Prevents errors due to unexpected JSON properties.
//consider example : String json = """
//    {
//        "name": "John Doe",
//        "age": 30,
//        "extraField": "unexpected"
//    }
//""";
//Unrecognized field "extraField" (Ignores extraField instead of failing.)
@Configuration
public class ObjectMapperConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
