package com.sravani.movieapi.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        //@Schema(...) (Swagger Documentation)
        //Helps generate API documentation in Swagger UI.
        //Provides example values for each field.
        @Schema(example = "user") @NotBlank String username,
        @Schema(example = "user") @NotBlank String password) {
}
