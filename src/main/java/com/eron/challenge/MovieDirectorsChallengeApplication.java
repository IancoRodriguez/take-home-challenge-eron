package com.eron.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class MovieDirectorsChallengeApplication {

    private static final Logger logger = LoggerFactory.getLogger(MovieDirectorsChallengeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MovieDirectorsChallengeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openSwaggerUI() {
        String swaggerUrl = "http://localhost:8080/swagger-ui/index.html";
        logger.info("Application started. Opening Swagger UI at: {}", swaggerUrl);

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(swaggerUrl));
                    logger.info("Swagger UI opened successfully");
                } else {
                    logger.warn("Browser action not supported. Please open manually: {}", swaggerUrl);
                }
            } else {
                logger.warn("Desktop not supported. Please open manually: {}", swaggerUrl);
            }
        } catch (Exception e) {
            logger.error("Failed to open Swagger UI automatically: {}", e.getMessage());
            logger.info("Please open manually: {}", swaggerUrl);
        }
    }
}
