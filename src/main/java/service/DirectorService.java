package challenge.service;

import client.MovieApiClient;
import model.domain.Movie;
import model.dto.DirectorsResponse;
import model.dto.MovieApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import service.MovieMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DirectorService {

    private static final Logger logger = LoggerFactory.getLogger(DirectorService.class);

    private final MovieApiClient movieApiClient;
    private final MovieMapper movieMapper;

    public DirectorService(MovieApiClient movieApiClient, MovieMapper movieMapper) {
        this.movieApiClient = movieApiClient;
        this.movieMapper = movieMapper;
    }

    @Cacheable(value = "directors", key = "#threshold")
    public DirectorsResponse getDirectorsByThreshold(int threshold) {
        logger.info("Finding directors with more than {} movies", threshold);

        List<Movie> allMovies = fetchAllMovies();

        Map<String, Long> directorMovieCount = countMoviesByDirector(allMovies);

        List<String> filteredDirectors = directorMovieCount.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());

        logger.info("Found {} directors with more than {} movies",
                filteredDirectors.size(), threshold);

        return new DirectorsResponse(filteredDirectors);
    }

    private List<Movie> fetchAllMovies() {
        logger.debug("Fetching all movies from API");

        List<Movie> allMovies = new ArrayList<>();

        MovieApiResponse firstPage = movieApiClient.fetchMoviesByPage(1);
        List<Movie> firstPageMovies = firstPage.data().stream()
                .map(movieMapper::toDomain)
                .toList();
        allMovies.addAll(firstPageMovies);

        int totalPages = firstPage.totalPages();
        logger.debug("Total pages to fetch: {}", totalPages);

        for (int page = 2; page <= totalPages; page++) {
            MovieApiResponse response = movieApiClient.fetchMoviesByPage(page);
            List<Movie> pageMovies = response.data().stream()
                    .map(movieMapper::toDomain)
                    .toList();
            allMovies.addAll(pageMovies);
        }

        logger.info("Fetched total of {} movies from {} pages", allMovies.size(), totalPages);

        return allMovies;
    }

    private Map<String, Long> countMoviesByDirector(List<Movie> movies) {
        logger.debug("Counting movies by director");

        Map<String, Long> counts = movies.stream()
                .collect(Collectors.groupingBy(
                        Movie::getDirector,
                        Collectors.counting()
                ));

        logger.debug("Found {} unique directors", counts.size());

        return counts;
    }
}