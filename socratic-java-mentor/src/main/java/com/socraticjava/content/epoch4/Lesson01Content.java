package com.socraticjava.content.epoch4;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 4.1: Introduction to Testing
 */
public class Lesson01Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-4-lesson-1", "Lesson 4.1: Why Test Your Code?")
            .addTheory("The Problem: How Do You Know It Works?",
                "You've written a method:\n\n" +
                "public static int add(int a, int b) {\n" +
                "    return a + b;\n" +
                "}\n\n" +
                "How do you KNOW it works correctly for all inputs?\n" +
                "- What if a is negative?\n" +
                "- What if both are zero?\n" +
                "- What if they're very large numbers?\n\n" +
                "Manual testing (running main and checking output) is:\n" +
                "❌ Tedious\n" +
                "❌ Error-prone\n" +
                "❌ Doesn't scale\n\n" +
                "Professional developers write AUTOMATED TESTS that run instantly and check everything.")
            .addAnalogy("Tests are Like a Parachute",
                "Would you jump out of a plane with a parachute you've never tested?\n\n" +
                "Same with code:\n" +
                "- Tests verify your code works\n" +
                "- They catch bugs BEFORE users see them\n" +
                "- They give you confidence to make changes\n\n" +
                "A well-tested codebase is like a safety net.")
            .addTheory("JUnit - The Testing Framework",
                "JUnit is Java's most popular testing framework.\n\n" +
                "Example test:\n\n" +
                "@Test\n" +
                "public void testAdd() {\n" +
                "    int result = Calculator.add(2, 3);\n" +
                "    assertEquals(5, result);\n" +
                "}\n\n" +
                "If add(2,3) returns 5, test PASSES ✓\n" +
                "If it returns anything else, test FAILS ✗")
            .addChallenge(createTestingQuiz())
            .estimatedMinutes(25)
            .build();
    }

    private static Challenge createTestingQuiz() {
        return new Challenge.Builder("epoch-4-lesson-1-quiz", "Understanding Testing", ChallengeType.MULTIPLE_CHOICE)
            .description("Why do professional developers write automated tests?")
            .addMultipleChoiceOption("A) To make their code longer")
            .addMultipleChoiceOption("B) Because their boss requires it")
            .addMultipleChoiceOption("C) To verify code works correctly and catch bugs early")
            .addMultipleChoiceOption("D) Testing is optional and rarely used")
            .correctAnswer("C")
            .build();
    }
}
