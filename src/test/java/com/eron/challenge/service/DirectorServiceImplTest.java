package com.eron.challenge.service;

import com.eron.challenge.client.MovieApiClient;
import com.eron.challenge.model.domain.Movie;
import com.eron.challenge.model.dto.MovieApiResponse;
import com.eron.challenge.model.dto.MovieDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.eron.challenge.model.mapper.MovieMapper;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.eron.challenge.service.implementations.*;

@ExtendWith(MockitoExtension.class)

class DirectorServiceImplTest {

    @Mock
    private MovieApiClient movieApiClient;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private DirectorServiceImpl directorService;

    private MovieApiResponse mockResponse;
    private List<MovieDto> mockMovieDtos;
    private List<Movie> mockMovies;

    @BeforeEach
    void setUp() {
        mockMovieDtos = List.of(
                new MovieDto("The Departed", "2006", "R", "06 Oct 2006", "151 min",
                        "Crime, Drama", "Martin Scorsese", "William Monahan", "Leonardo DiCaprio"),
                new MovieDto("Goodfellas", "1990", "R", "19 Sep 1990", "146 min",
                        "Crime, Drama", "Martin Scorsese", "Nicholas Pileggi", "Robert De Niro"),
                new MovieDto("Casino", "1995", "R", "22 Nov 1995", "178 min",
                        "Crime, Drama", "Martin Scorsese", "Nicholas Pileggi", "Robert De Niro"),
                new MovieDto("Inception", "2010", "PG-13", "16 Jul 2010", "148 min",
                        "Action, Sci-Fi", "Christopher Nolan", "Christopher Nolan", "Leonardo DiCaprio"),
                new MovieDto("The Dark Knight", "2008", "PG-13", "18 Jul 2008", "152 min",
                        "Action, Crime", "Christopher Nolan", "Jonathan Nolan", "Christian Bale"),
                new MovieDto("Midnight in Paris", "2011", "PG-13", "20 May 2011", "94 min",
                        "Comedy, Fantasy", "Woody Allen", "Woody Allen", "Owen Wilson")
        );

        mockMovies = List.of(
                new Movie("The Departed", "Martin Scorsese"),
                new Movie("Goodfellas", "Martin Scorsese"),
                new Movie("Casino", "Martin Scorsese"),
                new Movie("Inception", "Christopher Nolan"),
                new Movie("The Dark Knight", "Christopher Nolan"),
                new Movie("Midnight in Paris", "Woody Allen")
        );

        mockResponse = new MovieApiResponse(1, 10, 6, 1, mockMovieDtos);
    }

    @Test
    void getDirectorsByThreshold_shouldReturnDirectorsWithMoreThanThresholdMovies() {
        when(movieApiClient.fetchMoviesByPage(1)).thenReturn(mockResponse);

        for (int i = 0; i < mockMovieDtos.size(); i++) {
            when(movieMapper.toDomain(mockMovieDtos.get(i))).thenReturn(mockMovies.get(i));
        }

        List<String> result = directorService.getDirectorsByThreshold(1);

        assertEquals(2, result.size());
        assertTrue(result.contains("Christopher Nolan"));
        assertTrue(result.contains("Martin Scorsese"));

        verify(movieApiClient, times(1)).fetchMoviesByPage(1);
    }

    @Test
    void getDirectorsByThreshold_shouldReturnEmptyListWhenNoDirectorsMeetThreshold() {
        when(movieApiClient.fetchMoviesByPage(1)).thenReturn(mockResponse);

        for (int i = 0; i < mockMovieDtos.size(); i++) {
            when(movieMapper.toDomain(mockMovieDtos.get(i))).thenReturn(mockMovies.get(i));
        }

        List<String> result = directorService.getDirectorsByThreshold(10);

        assertTrue(result.isEmpty());

        verify(movieApiClient, times(1)).fetchMoviesByPage(1);
    }

    @Test
    void getDirectorsByThreshold_shouldReturnDirectorsInAlphabeticalOrder() {
        when(movieApiClient.fetchMoviesByPage(1)).thenReturn(mockResponse);

        for (int i = 0; i < mockMovieDtos.size(); i++) {
            when(movieMapper.toDomain(mockMovieDtos.get(i))).thenReturn(mockMovies.get(i));
        }

        List<String> result = directorService.getDirectorsByThreshold(1);

        assertEquals("Christopher Nolan", result.get(0));
        assertEquals("Martin Scorsese", result.get(1));

        verify(movieApiClient, times(1)).fetchMoviesByPage(1);
    }

    @Test
    void getDirectorsByThreshold_shouldHandleMultiplePages() {
        MovieApiResponse firstPage = new MovieApiResponse(1, 3, 6, 2,
                mockMovieDtos.subList(0, 3));
        MovieApiResponse secondPage = new MovieApiResponse(2, 3, 6, 2,
                mockMovieDtos.subList(3, 6));

        when(movieApiClient.fetchMoviesByPage(1)).thenReturn(firstPage);
        when(movieApiClient.fetchMoviesByPage(2)).thenReturn(secondPage);

        for (int i = 0; i < mockMovieDtos.size(); i++) {
            when(movieMapper.toDomain(mockMovieDtos.get(i))).thenReturn(mockMovies.get(i));
        }

        List<String> result = directorService.getDirectorsByThreshold(1);

        assertEquals(2, result.size());

        verify(movieApiClient, times(1)).fetchMoviesByPage(1);
        verify(movieApiClient, times(1)).fetchMoviesByPage(2);
    }

    @Test
    void getDirectorsByThreshold_shouldUseStrictlyGreaterThanComparison() {
        when(movieApiClient.fetchMoviesByPage(1)).thenReturn(mockResponse);

        for (int i = 0; i < mockMovieDtos.size(); i++) {
            when(movieMapper.toDomain(mockMovieDtos.get(i))).thenReturn(mockMovies.get(i));
        }

        List<String> resultWith2 = directorService.getDirectorsByThreshold(2);
        assertTrue(resultWith2.contains("Martin Scorsese"));
        assertFalse(resultWith2.contains("Christopher Nolan"));

        List<String> resultWith3 = directorService.getDirectorsByThreshold(3);
        assertTrue(resultWith3.isEmpty());
    }
}