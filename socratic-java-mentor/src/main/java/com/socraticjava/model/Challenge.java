package com.socraticjava.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a challenge (coding exercise, quiz, etc.) within a lesson
 */
public class Challenge {
    private final String id;
    private final String title;
    private final String description;
    private final ChallengeType type;
    private final String starterCode; // For CODE_COMPLETION and FREE_CODING
    private final List<TestCase> testCases; // For coding challenges
    private final List<String> multipleChoiceOptions; // For MULTIPLE_CHOICE
    private final String correctAnswer; // For MULTIPLE_CHOICE or validation
    private final String methodSignature; // Expected method signature for coding challenges

    private Challenge(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.type = builder.type;
        this.starterCode = builder.starterCode;
        this.testCases = builder.testCases;
        this.multipleChoiceOptions = builder.multipleChoiceOptions;
        this.correctAnswer = builder.correctAnswer;
        this.methodSignature = builder.methodSignature;
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

    public ChallengeType getType() {
        return type;
    }

    public String getStarterCode() {
        return starterCode;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public List<String> getMultipleChoiceOptions() {
        return multipleChoiceOptions;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    // Builder pattern for flexible construction
    public static class Builder {
        private String id;
        private String title;
        private String description;
        private ChallengeType type;
        private String starterCode = "";
        private List<TestCase> testCases = new ArrayList<>();
        private List<String> multipleChoiceOptions = new ArrayList<>();
        private String correctAnswer = "";
        private String methodSignature = "";

        public Builder(String id, String title, ChallengeType type) {
            this.id = id;
            this.title = title;
            this.type = type;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder starterCode(String starterCode) {
            this.starterCode = starterCode;
            return this;
        }

        public Builder methodSignature(String methodSignature) {
            this.methodSignature = methodSignature;
            return this;
        }

        public Builder addTestCase(TestCase testCase) {
            this.testCases.add(testCase);
            return this;
        }

        public Builder addTestCase(String description, Object[] inputs, Object expectedOutput) {
            this.testCases.add(new TestCase(description, inputs, expectedOutput));
            return this;
        }

        public Builder addMultipleChoiceOption(String option) {
            this.multipleChoiceOptions.add(option);
            return this;
        }

        public Builder correctAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
            return this;
        }

        public Challenge build() {
            return new Challenge(this);
        }
    }
}
