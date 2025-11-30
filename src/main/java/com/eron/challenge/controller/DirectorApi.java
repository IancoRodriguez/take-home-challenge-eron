package com.eron.challenge.controller;

import com.eron.challenge.model.dto.DirectorsResponse;
import com.eron.challenge.model.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Directors", description = "Operations related to movie directors")
public interface DirectorApi {

    @Operation(
            summary = "Get directors by movie count threshold",
            description = "Returns a list of directors who have directed more than the specified number of movies, sorted alphabetically"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved directors",
                    content = @Content(schema = @Schema(implementation = DirectorsResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid threshold value",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<DirectorsResponse> getDirectors(
            @Parameter(description = "Minimum number of movies (exclusive)", required = true, example = "4")
            @RequestParam
            @Min(value = 0, message = "Threshold must be non-negative")
            int threshold
    );
}