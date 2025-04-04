package com.sravani.movieapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource(@Value("${app.cors.allowed-origins}") List<String> allowedOrigins) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);// Allows sending cookies and authorization headers.
        configuration.setAllowedOrigins(allowedOrigins);//Reads allowed origins from the properties file (app.cors.allowed-origins).
        configuration.addAllowedMethod("*");//llows all HTTP methods (GET, POST, PUT, DELETE, etc.).
        configuration.addAllowedHeader("*");// Allows all headers.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();//you can apply different CORS policies for different URL patterns:
        //source.registerCorsConfiguration("/public/**", openConfig);  // Public API rules
        // source.registerCorsConfiguration("/admin/**", adminConfig);Admin API rules
        source.registerCorsConfiguration("/**", configuration);//Applies these rules to all API endpoints ("/**").
        return source;
    }
}