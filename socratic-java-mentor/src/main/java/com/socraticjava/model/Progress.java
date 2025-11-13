package com.socraticjava.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Tracks student progress through the curriculum
 */
public class Progress {
    private String currentEpochId;
    private String currentLessonId;
    private Map<String, LessonProgress> completedLessons;
    private LocalDateTime lastAccessed;
    private int totalTimeMinutes;

    public Progress() {
        this.completedLessons = new HashMap<>();
        this.lastAccessed = LocalDateTime.now();
        this.totalTimeMinutes = 0;
    }

    /**
     * Marks a lesson as completed
     */
    public void completeLesson(String lessonId, int challengesCompleted, int challengesTotal) {
        LessonProgress lessonProgress = new LessonProgress(
            lessonId,
            challengesCompleted,
            challengesTotal,
            LocalDateTime.now()
        );
        completedLessons.put(lessonId, lessonProgress);
    }

    /**
     * Marks a specific challenge as completed
     */
    public void completeChallenge(String lessonId, String challengeId) {
        LessonProgress lessonProgress = completedLessons.get(lessonId);
        if (lessonProgress == null) {
            lessonProgress = new LessonProgress(lessonId, 0, 0, LocalDateTime.now());
            completedLessons.put(lessonId, lessonProgress);
        }
        lessonProgress.addCompletedChallenge(challengeId);
    }

    /**
     * Checks if a lesson is completed
     */
    public boolean isLessonCompleted(String lessonId) {
        return completedLessons.containsKey(lessonId) &&
               completedLessons.get(lessonId).isFullyCompleted();
    }

    /**
     * Checks if a specific challenge is completed
     */
    public boolean isChallengeCompleted(String lessonId, String challengeId) {
        LessonProgress lessonProgress = completedLessons.get(lessonId);
        return lessonProgress != null && lessonProgress.isChallengeCompleted(challengeId);
    }

    // Getters and setters
    public String getCurrentEpochId() {
        return currentEpochId;
    }

    public void setCurrentEpochId(String currentEpochId) {
        this.currentEpochId = currentEpochId;
    }

    public String getCurrentLessonId() {
        return currentLessonId;
    }

    public void setCurrentLessonId(String currentLessonId) {
        this.currentLessonId = currentLessonId;
        this.lastAccessed = LocalDateTime.now();
    }

    public Map<String, LessonProgress> getCompletedLessons() {
        return completedLessons;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void updateLastAccessed() {
        this.lastAccessed = LocalDateTime.now();
    }

    public int getTotalTimeMinutes() {
        return totalTimeMinutes;
    }

    public void addTimeMinutes(int minutes) {
        this.totalTimeMinutes += minutes;
    }

    /**
     * Progress for a single lesson
     */
    public static class LessonProgress {
        private String lessonId;
        private int challengesCompleted;
        private int challengesTotal;
        private Map<String, Boolean> completedChallenges;
        private LocalDateTime completedAt;

        public LessonProgress(String lessonId, int challengesCompleted, int challengesTotal, LocalDateTime completedAt) {
            this.lessonId = lessonId;
            this.challengesCompleted = challengesCompleted;
            this.challengesTotal = challengesTotal;
            this.completedAt = completedAt;
            this.completedChallenges = new HashMap<>();
        }

        public void addCompletedChallenge(String challengeId) {
            completedChallenges.put(challengeId, true);
        }

        public boolean isChallengeCompleted(String challengeId) {
            return completedChallenges.getOrDefault(challengeId, false);
        }

        public boolean isFullyCompleted() {
            return challengesTotal > 0 && challengesCompleted >= challengesTotal;
        }

        // Getters
        public String getLessonId() {
            return lessonId;
        }

        public int getChallengesCompleted() {
            return challengesCompleted;
        }

        public int getChallengesTotal() {
            return challengesTotal;
        }

        public LocalDateTime getCompletedAt() {
            return completedAt;
        }
    }
}
