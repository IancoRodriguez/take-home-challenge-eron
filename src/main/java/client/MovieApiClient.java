package client;

import model.dto.MovieApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class MovieApiClient {

    private static final Logger logger = LoggerFactory.getLogger(MovieApiClient.class);

    private final WebClient webClient;
    private final String baseUrl;

    public MovieApiClient(
            WebClient.Builder webClientBuilder,
            @Value("${movie.api.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
        logger.info("MovieApiClient initialized with base URL: {}", baseUrl);
    }

    public MovieApiResponse fetchMoviesByPage(int page) {
        logger.debug("Fetching movies for page: {}", page);

        try {
            MovieApiResponse response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/movies/search")
                            .queryParam("page", page)
                            .build())
                    .retrieve()
                    .bodyToMono(MovieApiResponse.class)
                    .block();

            if (response == null) {
                throw new RuntimeException("Received null response from API for page " + page);
            }

            logger.debug("Successfully fetched {} movies from page {}",
                    response.data().size(), page);

            return response;

        } catch (WebClientResponseException e) {
            logger.error("HTTP error fetching page {}: {} - {}",
                    page, e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Failed to fetch movies from API", e);

        } catch (Exception e) {
            logger.error("Unexpected error fetching page {}: {}", page, e.getMessage());
            throw new RuntimeException("Failed to fetch movies from API", e);
        }
    }
}
