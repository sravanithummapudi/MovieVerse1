package com.sravani.movieapi.movie;

import com.sravani.movieapi.rest.dto.CreateMovieRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    private String imdb;

    private String title;
    private String poster;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    public Movie(String imdb, String title, String poster) {
        this.imdb = imdb;
        this.title = title;
        this.poster = poster;
    }

    public Movie() {
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    public void onPrePersist() {
        createdAt = Instant.now();
    }

    public static Movie from(CreateMovieRequest createMovieRequest) {
        return new Movie(createMovieRequest.imdb(), createMovieRequest.title(), createMovieRequest.poster());
    }
}
