package com.socraticjava.service;

import com.socraticjava.content.EpochRegistry;
import com.socraticjava.model.Epoch;
import com.socraticjava.model.Lesson;

import java.util.List;

/**
 * Service for managing lesson retrieval and navigation
 */
public class LessonService {

    /**
     * Gets all epochs
     */
    public List<Epoch> getAllEpochs() {
        return EpochRegistry.getAllEpochs();
    }

    /**
     * Gets a specific epoch by ID
     */
    public Epoch getEpoch(String epochId) {
        return EpochRegistry.getEpochById(epochId);
    }

    /**
     * Gets a specific lesson by epoch and lesson ID
     */
    public Lesson getLesson(String epochId, String lessonId) {
        Epoch epoch = getEpoch(epochId);
        if (epoch == null) {
            return null;
        }

        return epoch.getLessons().stream()
            .filter(lesson -> lesson.getId().equals(lessonId))
            .findFirst()
            .orElse(null);
    }

    /**
     * Gets the next lesson after the current one
     * Returns null if at the end of the epoch
     */
    public Lesson getNextLesson(String epochId, String currentLessonId) {
        Epoch epoch = getEpoch(epochId);
        if (epoch == null) {
            return null;
        }

        List<Lesson> lessons = epoch.getLessons();
        for (int i = 0; i < lessons.size() - 1; i++) {
            if (lessons.get(i).getId().equals(currentLessonId)) {
                return lessons.get(i + 1);
            }
        }
        return null;
    }

    /**
     * Gets the previous lesson before the current one
     * Returns null if at the beginning of the epoch
     */
    public Lesson getPreviousLesson(String epochId, String currentLessonId) {
        Epoch epoch = getEpoch(epochId);
        if (epoch == null) {
            return null;
        }

        List<Lesson> lessons = epoch.getLessons();
        for (int i = 1; i < lessons.size(); i++) {
            if (lessons.get(i).getId().equals(currentLessonId)) {
                return lessons.get(i - 1);
            }
        }
        return null;
    }

    /**
     * Gets the first lesson of an epoch
     */
    public Lesson getFirstLesson(String epochId) {
        Epoch epoch = getEpoch(epochId);
        if (epoch == null || epoch.getLessons().isEmpty()) {
            return null;
        }
        return epoch.getLessons().get(0);
    }

    /**
     * Gets the total number of lessons across all epochs
     */
    public int getTotalLessonCount() {
        return getAllEpochs().stream()
            .mapToInt(Epoch::getLessonCount)
            .sum();
    }
}
