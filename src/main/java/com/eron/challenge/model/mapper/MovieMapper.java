package com.eron.challenge.model.mapper;

import com.eron.challenge.model.domain.Movie;
import com.eron.challenge.model.dto.MovieDto;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public Movie toDomain(MovieDto dto) {
        return new Movie(
                dto.title(),
                dto.director()
        );
    }
}