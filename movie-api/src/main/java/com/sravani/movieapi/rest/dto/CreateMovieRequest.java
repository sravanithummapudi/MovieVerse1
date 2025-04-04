package com.sravani.movieapi.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateMovieRequest(
        //@NotBlank
        //Ensures imdb and title are not empty or null.

        //@Schema(...)
        //Describes API documentation for OpenAPI (Swagger).
        //When a user makes a POST /api/movies request, they must provide this data in the request body.
        //Spring Boot automatically maps the JSON request body to this CreateMovieRequest record.
        @Schema(example = "tt0117998") @NotBlank String imdb,
        @Schema(example = "Twister") @NotBlank String title,
        @Schema(example = "https://m.media-amazon.com/images/M/MV5BODExYTM0MzEtZGY2Yy00N2ExLTkwZjItNGYzYTRmMWZlOGEzXkEyXkFqcGdeQXVyNDk3NzU2MTQ@._V1_SX300.jpg") String poster) {
}
