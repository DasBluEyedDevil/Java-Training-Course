package com.socraticjava.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single lesson within an epoch
 */
public class Lesson {
    private final String id;
    private final String title;
    private final List<ContentSection> contentSections;
    private final List<Challenge> challenges;
    private final int estimatedMinutes;

    private Lesson(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.contentSections = builder.contentSections;
        this.challenges = builder.challenges;
        this.estimatedMinutes = builder.estimatedMinutes;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<ContentSection> getContentSections() {
        return contentSections;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    /**
     * Represents a section of lesson content (theory, explanation, etc.)
     */
    public static class ContentSection {
        private final String heading;
        private final String content;
        private final ContentType type;

        public ContentSection(String heading, String content, ContentType type) {
            this.heading = heading;
            this.content = content;
            this.type = type;
        }

        public String getHeading() {
            return heading;
        }

        public String getContent() {
            return content;
        }

        public ContentType getType() {
            return type;
        }
    }

    /**
     * Types of content sections
     */
    public enum ContentType {
        THEORY,      // Main explanation
        ANALOGY,     // Real-world analogy
        EXAMPLE,     // Code example with explanation
        KEY_POINT,   // Important takeaway
        WARNING      // Common pitfall or warning
    }

    // Builder pattern
    public static class Builder {
        private String id;
        private String title;
        private List<ContentSection> contentSections = new ArrayList<>();
        private List<Challenge> challenges = new ArrayList<>();
        private int estimatedMinutes = 15;

        public Builder(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public Builder addContent(String heading, String content, ContentType type) {
            this.contentSections.add(new ContentSection(heading, content, type));
            return this;
        }

        public Builder addTheory(String heading, String content) {
            return addContent(heading, content, ContentType.THEORY);
        }

        public Builder addAnalogy(String heading, String content) {
            return addContent(heading, content, ContentType.ANALOGY);
        }

        public Builder addExample(String heading, String content) {
            return addContent(heading, content, ContentType.EXAMPLE);
        }

        public Builder addKeyPoint(String heading, String content) {
            return addContent(heading, content, ContentType.KEY_POINT);
        }

        public Builder addChallenge(Challenge challenge) {
            this.challenges.add(challenge);
            return this;
        }

        public Builder estimatedMinutes(int minutes) {
            this.estimatedMinutes = minutes;
            return this;
        }

        public Lesson build() {
            return new Lesson(this);
        }
    }
}
