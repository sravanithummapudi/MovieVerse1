package com.sravani.movieapi.movie;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {



    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    @Override
    public List<Movie> getMovies() {
        return movieRepository.findAllByOrderByTitle();
    }

    @Override
    public List<Movie> getMoviesContainingText(String text) {
        return movieRepository.findByImdbContainingOrTitleContainingIgnoreCaseOrderByTitle(text, text);
    }

    @Override
    public Movie validateAndGetMovie(String imdb) {
        return movieRepository.findById(imdb)
                .orElseThrow(() -> new MovieNotFoundException(String.format("Movie with imdb %s not found", imdb)));
    }

    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }
}
