package com.sravani.movieapi.rest;

import com.sravani.movieapi.movie.Movie;
import com.sravani.movieapi.rest.dto.CreateMovieRequest;
import com.sravani.movieapi.rest.dto.MovieDto;
import com.sravani.movieapi.movie.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.sravani.movieapi.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;


@RestController
 //All endpoints in this class will be under /api/movies
@RequestMapping("/api/movies")
public class MovieController {



    private final MovieService movieService;
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
//This enforces JWT authentication, meaning the client must provide a Bearer token in the request header:
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    //Filters by text (optional): If text is provided, it searches for movies containing that text.
    // Returns a list of movies (MovieDto).
    public List<MovieDto> getMovies(@RequestParam(value = "text", required = false) String text) {
        List<Movie> movies = (text == null) ? movieService.getMovies() : movieService.getMoviesContainingText(text);
        return movies.stream()
                .map(MovieDto::from)
                .collect(Collectors.toList());
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MovieDto createMovie(@Valid @RequestBody CreateMovieRequest createMovieRequest) {
      //  Creates a new movie from CreateMovieRequest.
      //Saves and returns the new movie as MovieDto.
        Movie movie = Movie.from(createMovieRequest);
        return MovieDto.from(movieService.saveMovie(movie));
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{imdb}")
    //This method is an API endpoint to delete a movie by its imdb identifier.
    // It uses JWT-based security (Bearer Token) to ensure that only authorized users can delete movies.
    public MovieDto deleteMovie(@PathVariable String imdb) {
        Movie movie = movieService.validateAndGetMovie(imdb);
        movieService.deleteMovie(movie);
        return MovieDto.from(movie);
    }
}
