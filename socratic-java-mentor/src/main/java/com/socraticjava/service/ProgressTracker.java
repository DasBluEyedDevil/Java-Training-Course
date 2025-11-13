package com.socraticjava.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socraticjava.model.Progress;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;

/**
 * Service for persisting and loading student progress
 */
public class ProgressTracker {

    private static final String PROGRESS_DIR = System.getProperty("user.home") + "/.socratic-java";
    private static final String PROGRESS_FILE = PROGRESS_DIR + "/progress.json";
    private final Gson gson;

    public ProgressTracker() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    }

    /**
     * Saves progress to disk
     */
    public void saveProgress(Progress progress) {
        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(PROGRESS_DIR));

            // Serialize to JSON
            String json = gson.toJson(progress);

            // Write to file
            Files.writeString(Paths.get(PROGRESS_FILE), json);

        } catch (IOException e) {
            System.err.println("Failed to save progress: " + e.getMessage());
        }
    }

    /**
     * Loads progress from disk
     * Returns a new Progress object if no saved progress exists
     */
    public Progress loadProgress() {
        try {
            File file = new File(PROGRESS_FILE);
            if (!file.exists()) {
                return new Progress();
            }

            String json = Files.readString(Paths.get(PROGRESS_FILE));
            return gson.fromJson(json, Progress.class);

        } catch (IOException e) {
            System.err.println("Failed to load progress: " + e.getMessage());
            return new Progress();
        }
    }

    /**
     * Clears all saved progress
     */
    public void clearProgress() {
        try {
            Files.deleteIfExists(Paths.get(PROGRESS_FILE));
        } catch (IOException e) {
            System.err.println("Failed to clear progress: " + e.getMessage());
        }
    }

    /**
     * Checks if saved progress exists
     */
    public boolean hasProgress() {
        return Files.exists(Paths.get(PROGRESS_FILE));
    }
}
