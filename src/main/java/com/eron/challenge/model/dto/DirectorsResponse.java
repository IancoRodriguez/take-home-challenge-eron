package com.eron.challenge.model.dto;

import java.util.List;

public record DirectorsResponse(List<String> directors) {

    public DirectorsResponse {
        if (directors == null) {
            throw new IllegalArgumentException("Directors list cannot be null");
        }
    }
}
