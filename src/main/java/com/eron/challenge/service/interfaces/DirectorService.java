package com.eron.challenge.service.interfaces;

import java.util.List;

public interface DirectorService {
    List<String> getDirectorsByThreshold(int threshold);
}