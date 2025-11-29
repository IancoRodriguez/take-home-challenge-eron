package service;

import model.domain.Movie;
import model.dto.MovieDto;
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