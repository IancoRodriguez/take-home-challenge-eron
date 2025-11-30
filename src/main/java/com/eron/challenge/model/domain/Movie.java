package com.eron.challenge.model.domain;

import java.util.Objects;

public class Movie {
    private final String title;
    private final String director;

    public Movie(String title, String director) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (director == null || director.isBlank()) {
            throw new IllegalArgumentException("Director is required");
        }
        this.title = title;
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return title.equals(movie.title) && director.equals(movie.director);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, director);
    }

    @Override
    public String toString() {
        return "Movie{title='" + title + "', director='" + director + "'}";
    }
}