package com.sravani.movieapi.runner;

import com.sravani.movieapi.movie.Movie;
import com.sravani.movieapi.user.User;
import com.sravani.movieapi.security.oauth2.OAuth2Provider;
import com.sravani.movieapi.security.SecurityConfig;
import com.sravani.movieapi.movie.MovieService;
import com.sravani.movieapi.user.UserService;
import com.sravani.movieapi.movie.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);



    private final UserService userService;
    private final MovieService movieService;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserService userService, MovieService movieService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.movieService = movieService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // If users are already in the database, initialization is skipped.
        if (!userService.getUsers().isEmpty()) {
            return;
        }
        //Encodes user passwords before saving them
        USERS.forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        });
        //Saves initial users and movies into the database.
        MOVIES.forEach(movieService::saveMovie);
        log.info("Database initialized");
    }
    //pre defined users Admin user: Has higher privileges, Regular user: Has standard user access
    private static final List<User> USERS = Arrays.asList(
            new User("admin", "admin", "Admin", "admin@mycompany.com", SecurityConfig.ADMIN, null, OAuth2Provider.LOCAL, "1"),
            new User("user", "user", "User", "user@mycompany.com", SecurityConfig.USER, null, OAuth2Provider.LOCAL, "2")
    );
// Each movie is initialized with an IMDB ID, title, and poster URL.
    private static final List<Movie> MOVIES = Arrays.asList(
            new Movie("tt5580036", "I, Tonya", "https://m.media-amazon.com/images/M/MV5BMjI5MDY1NjYzMl5BMl5BanBnXkFtZTgwNjIzNDAxNDM@._V1_SX300.jpg"),
            new Movie("tt0163651", "American Pie", "https://m.media-amazon.com/images/M/MV5BMTg3ODY5ODI1NF5BMl5BanBnXkFtZTgwMTkxNTYxMTE@._V1_SX300.jpg"),
            new Movie("tt0480249", "I Am Legend", "https://m.media-amazon.com/images/M/MV5BYTE1ZTBlYzgtNmMyNS00ZTQ2LWE4NjEtZjUxNDJkNTg2MzlhXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg"),
            new Movie("tt0120804", "Resident Evil", "https://m.media-amazon.com/images/M/MV5BZmI1ZGRhNDYtOGVjZC00MmUyLThlNTktMTQyZGE3MzE1ZTdlXkEyXkFqcGdeQXVyNDE5MTU2MDE@._V1_SX300.jpg"),
            new Movie("tt0075148", "Rocky", "https://m.media-amazon.com/images/M/MV5BMTY5MDMzODUyOF5BMl5BanBnXkFtZTcwMTQ3NTMyNA@@._V1_SX300.jpg"),
            new Movie("tt8540796", "The Green Line", "https://m.media-amazon.com/images/M/MV5BMzZkNTRjZjEtNDVhNi00NGEyLWE2NWYtNTUzOTFlNGVmMDFjXkEyXkFqcGdeQXVyODg0NjM4MDg@._V1_SX300.jpg"),
            new Movie("tt7286456", "Joker", "https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDctN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SX300.jpg"),
            new Movie("tt0112573", "Braveheart", "https://m.media-amazon.com/images/M/MV5BMzkzMmU0YTYtOWM3My00YzBmLWI0YzctOGYyNTkwMWE5MTJkXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg"),
            new Movie("tt0758758", "Into the Wild", "https://m.media-amazon.com/images/M/MV5BMTAwNDEyODU1MjheQTJeQWpwZ15BbWU2MDc3NDQwNw@@._V1_SX300.jpg"),
            new Movie("tt0088763", "Back to the Future", "https://m.media-amazon.com/images/M/MV5BZmU0M2Y1OGUtZjIxNi00ZjBkLTg1MjgtOWIyNThiZWIwYjRiXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg")
    );
}
