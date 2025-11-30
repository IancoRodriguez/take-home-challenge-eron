package com.eron.challenge.controller;


import com.eron.challenge.model.dto.DirectorsResponse;

import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.eron.challenge.service.interfaces.DirectorService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class DirectorController implements DirectorApi {

    private static final Logger logger = LoggerFactory.getLogger(DirectorController.class);

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @Override
    @GetMapping("/directors")
    public ResponseEntity<DirectorsResponse> getDirectors(
            @RequestParam @Min(value = 0, message = "Threshold must be non-negative") int threshold) {

        logger.info("GET /api/directors called with threshold={}", threshold);

        List<String> directors = directorService.getDirectorsByThreshold(threshold);
        DirectorsResponse response = new DirectorsResponse(directors);

        logger.info("Returning {} directors", directors.size());

        return ResponseEntity.ok(response);
    }
}
