package com.eron.challenge.controller;

import com.eron.challenge.client.MovieApiClient;
import com.eron.challenge.model.dto.MovieApiResponse;
import com.eron.challenge.model.dto.MovieDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DirectorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieApiClient movieApiClient;

    private List<MovieDto> createMockMovies() {
        return List.of(
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
    }

    @Test
    void getDirectors_shouldReturnDirectorsWithMoreThanThreshold() throws Exception {
        List<MovieDto> mockMovies = createMockMovies();
        MovieApiResponse mockResponse = new MovieApiResponse(1, 10, 6, 1, mockMovies);

        when(movieApiClient.fetchMoviesByPage(1)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/directors")
                        .param("threshold", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.directors").isArray())
                .andExpect(jsonPath("$.directors.length()").value(2))
                .andExpect(jsonPath("$.directors[0]").value("Christopher Nolan"))
                .andExpect(jsonPath("$.directors[1]").value("Martin Scorsese"));
    }

    @Test
    void getDirectors_shouldReturnBadRequestWhenThresholdIsNegative() throws Exception {
        mockMvc.perform(get("/api/directors")
                        .param("threshold", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Threshold must be non-negative"));
    }

    @Test
    void getDirectors_shouldReturnBadRequestWhenThresholdIsMissing() throws Exception {
        mockMvc.perform(get("/api/directors"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Required parameter 'threshold' is missing"));
    }

    @Test
    void getDirectors_shouldReturnBadRequestWhenThresholdIsNotANumber() throws Exception {
        mockMvc.perform(get("/api/directors")
                        .param("threshold", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void getDirectors_shouldReturnEmptyListWhenNoDirectorsMeetThreshold() throws Exception {
        List<MovieDto> mockMovies = createMockMovies();
        MovieApiResponse mockResponse = new MovieApiResponse(1, 10, 6, 1, mockMovies);

        when(movieApiClient.fetchMoviesByPage(1)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/directors")
                        .param("threshold", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.directors").isArray())
                .andExpect(jsonPath("$.directors.length()").value(0));
    }
}

