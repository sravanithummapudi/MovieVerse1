package com.sravani.movieapi.rest.dto;

import com.sravani.movieapi.movie.Movie;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

// Java Record that acts as a Data Transfer Object (DTO) for movies.
// It converts Movie entities into a simpler format suitable for API responses.
//Ensures immutability and thread safety.
// Prevents exposing entity objects (Movie) directly

public record MovieDto(String imdb, String title, String poster, String createdAt) {
     //Converts a Movie entity into a MovieDto
    public static MovieDto from(Movie movie) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());
        return new MovieDto(
                movie.getImdb(),
                movie.getTitle(),
                movie.getPoster(),
                formatter.format(movie.getCreatedAt())
        );
    }
}
