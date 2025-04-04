package com.sravani.movieapi.rest;

import com.sravani.movieapi.movie.MovieService;
import com.sravani.movieapi.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/public")
public class PublicController {


//This controller provides public API endpoints that do not require
// authentication. Any user (even unauthenticated) can access these
// APIs to get general statistics about users and movies.
    private final UserService userService;
    private final MovieService movieService;

    public PublicController(UserService userService, MovieService movieService) {
        this.userService = userService;
        this.movieService = movieService;
    }
// Get Total Users
    @GetMapping("/numberOfUsers")
    public Integer getNumberOfUsers() {
        return userService.getUsers().size();
    }
//Get Total Movies
    @GetMapping("/numberOfMovies")
    public Integer getNumberOfMovies() {
        return movieService.getMovies().size();
    }
}
