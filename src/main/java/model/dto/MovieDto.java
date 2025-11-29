package model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieDto(
        @JsonProperty("Title") String title,
        @JsonProperty("Year") String year,
        @JsonProperty("Rated") String rated,
        @JsonProperty("Released") String released,
        @JsonProperty("Runtime") String runtime,
        @JsonProperty("Genre") String genre,
        @JsonProperty("Director") String director,
        @JsonProperty("Writer") String writer,
        @JsonProperty("Actors") String actors
) {

    public MovieDto {
        if (director == null || director.isBlank()) {
            throw new IllegalArgumentException("Director is required");
        }
    }
}