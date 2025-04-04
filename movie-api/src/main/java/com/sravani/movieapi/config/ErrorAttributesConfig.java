package com.sravani.movieapi.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

//customizes the error response structure
@Configuration
public class ErrorAttributesConfig {

    @Bean
    //Defines a custom ErrorAttributes bean that modifies default error handling behavior.
    ErrorAttributes errorAttributes() {
        //Overrides the default method to include additional details in error responses
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                return super.getErrorAttributes(webRequest, options.including(Include.EXCEPTION, Include.MESSAGE, Include.BINDING_ERRORS));
            }
        };
        //Controls which error details are included:
        //Include.EXCEPTION → Adds the exception class name.
        //Include.MESSAGE → Adds the error message.
        //Include.BINDING_ERRORS → Includes validation errors (e.g., invalid request body).
    }
}
