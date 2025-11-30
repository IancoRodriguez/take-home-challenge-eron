package com.eron.challenge.client;

import com.eron.challenge.model.dto.MovieApiResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MovieApiClientTest {

    private MockWebServer mockWebServer;
    private MovieApiClient movieApiClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        WebClient.Builder webClientBuilder = WebClient.builder();
        movieApiClient = new MovieApiClient(webClientBuilder, baseUrl);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchMoviesByPage_shouldReturnMovies() {
        String mockResponse = """
                {
                  "page": 1,
                  "per_page": 10,
                  "total": 20,
                  "total_pages": 2,
                  "data": [
                    {
                      "Title": "The Departed",
                      "Year": "2006",
                      "Rated": "R",
                      "Released": "06 Oct 2006",
                      "Runtime": "151 min",
                      "Genre": "Crime, Drama",
                      "Director": "Martin Scorsese",
                      "Writer": "William Monahan",
                      "Actors": "Leonardo DiCaprio"
                    }
                  ]
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        MovieApiResponse response = movieApiClient.fetchMoviesByPage(1);

        assertNotNull(response);
        assertEquals(1, response.page());
        assertEquals(10, response.perPage());
        assertEquals(20, response.total());
        assertEquals(2, response.totalPages());
        assertEquals(1, response.data().size());
        assertEquals("The Departed", response.data().get(0).title());
        assertEquals("Martin Scorsese", response.data().get(0).director());
    }

    @Test
    void fetchMoviesByPage_shouldThrowExceptionOnError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        assertThrows(RuntimeException.class, () -> {
            movieApiClient.fetchMoviesByPage(1);
        });
    }
}