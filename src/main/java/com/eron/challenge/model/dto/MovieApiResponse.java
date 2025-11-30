package com.eron.challenge.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record MovieApiResponse(
        int page,
        @JsonProperty("per_page") int perPage,
        int total,
        @JsonProperty("total_pages") int totalPages,
        List<MovieDto> data
) {

    public MovieApiResponse {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (totalPages < 0 || page < 0) {
            throw new IllegalArgumentException("Page numbers must be non-negative");
        }
    }
}