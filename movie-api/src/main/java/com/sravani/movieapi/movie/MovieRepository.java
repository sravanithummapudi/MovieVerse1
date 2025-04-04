package com.sravani.movieapi.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

    List<Movie> findAllByOrderByTitle();
    //Searches for movies where:
    //
    //imdb field contains the given imdb string.
    //
    //OR title contains the given title string (case-insensitive).
    //
    //Results are sorted by title in ascending order.
    List<Movie> findByImdbContainingOrTitleContainingIgnoreCaseOrderByTitle(String imdb, String title);
}
