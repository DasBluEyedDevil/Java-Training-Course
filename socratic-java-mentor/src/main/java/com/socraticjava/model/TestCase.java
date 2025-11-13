package com.socraticjava.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a single test case for validating student code
 */
public class TestCase {
    private final String description;
    private final Object[] inputs;
    private final Object expectedOutput;
    private final boolean isVisible; // If true, student sees this test before running

    public TestCase(String description, Object[] inputs, Object expectedOutput, boolean isVisible) {
        this.description = description;
        this.inputs = inputs;
        this.expectedOutput = expectedOutput;
        this.isVisible = isVisible;
    }

    public TestCase(String description, Object[] inputs, Object expectedOutput) {
        this(description, inputs, expectedOutput, true);
    }

    // Getters
    public String getDescription() {
        return description;
    }

    public Object[] getInputs() {
        return inputs;
    }

    public Object getExpectedOutput() {
        return expectedOutput;
    }

    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "description='" + description + '\'' +
                ", inputs=" + Arrays.toString(inputs) +
                ", expectedOutput=" + expectedOutput +
                ", visible=" + isVisible +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCase testCase = (TestCase) o;
        return isVisible == testCase.isVisible &&
                Objects.equals(description, testCase.description) &&
                Arrays.equals(inputs, testCase.inputs) &&
                Objects.equals(expectedOutput, testCase.expectedOutput);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(description, expectedOutput, isVisible);
        result = 31 * result + Arrays.hashCode(inputs);
        return result;
    }
}
