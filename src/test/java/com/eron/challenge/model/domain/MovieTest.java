package com.eron.challenge.model.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void constructor_shouldCreateValidMovie() {
        Movie movie = new Movie("Inception", "Christopher Nolan");

        assertEquals("Inception", movie.getTitle());
        assertEquals("Christopher Nolan", movie.getDirector());
    }

    @Test
    void constructor_shouldThrowExceptionWhenTitleIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Movie(null, "Christopher Nolan");
        });
    }

    @Test
    void constructor_shouldThrowExceptionWhenTitleIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Movie("   ", "Christopher Nolan");
        });
    }

    @Test
    void constructor_shouldThrowExceptionWhenDirectorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Movie("Inception", null);
        });
    }

    @Test
    void constructor_shouldThrowExceptionWhenDirectorIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Movie("Inception", "   ");
        });
    }

    @Test
    void equals_shouldReturnTrueForSameValues() {
        Movie movie1 = new Movie("Inception", "Christopher Nolan");
        Movie movie2 = new Movie("Inception", "Christopher Nolan");

        assertEquals(movie1, movie2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentValues() {
        Movie movie1 = new Movie("Inception", "Christopher Nolan");
        Movie movie2 = new Movie("The Departed", "Martin Scorsese");

        assertNotEquals(movie1, movie2);
    }

    @Test
    void hashCode_shouldBeSameForEqualObjects() {
        Movie movie1 = new Movie("Inception", "Christopher Nolan");
        Movie movie2 = new Movie("Inception", "Christopher Nolan");

        assertEquals(movie1.hashCode(), movie2.hashCode());
    }

    @Test
    void toString_shouldContainTitleAndDirector() {
        Movie movie = new Movie("Inception", "Christopher Nolan");
        String result = movie.toString();

        assertTrue(result.contains("Inception"));
        assertTrue(result.contains("Christopher Nolan"));
    }
}