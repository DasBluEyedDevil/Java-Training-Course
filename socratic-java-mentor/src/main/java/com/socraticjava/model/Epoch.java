package com.socraticjava.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a major learning epoch (e.g., "The Foundation", "Thinking in Objects")
 */
public class Epoch {
    private final String id;
    private final String title;
    private final String description;
    private final List<Lesson> lessons;
    private final int estimatedHours;

    public Epoch(String id, String title, String description, int estimatedHours) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lessons = new ArrayList<>();
        this.estimatedHours = estimatedHours;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Lesson> getLessons() {
        return new ArrayList<>(lessons); // Return copy for immutability
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public int getLessonCount() {
        return lessons.size();
    }

    @Override
    public String toString() {
        return title + " (" + lessons.size() + " lessons, ~" + estimatedHours + "h)";
    }
}
