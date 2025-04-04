package com.sravani.movieapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//It sets up Swagger UI to document and test your API.
//It configures JWT authentication so that secured endpoints require a Bearer Token.
@Configuration
public class SwaggerConfig {
//Reads the application name from application.properties
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components().addSecuritySchemes(BEARER_KEY_SECURITY_SCHEME,
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title(applicationName));
    }

    //Swagger security scheme :Adds Bearer Token authentication to Swagger.
    //This allows users to enter a JWT token when testing secured APIs.
   //Defines a global constant for the security scheme
    public static final String BEARER_KEY_SECURITY_SCHEME = "bearer-key";
    //In Swagger UI, you will see an "Authorize" button.
    //You can paste your JWT token to test protected endpoints.
}
