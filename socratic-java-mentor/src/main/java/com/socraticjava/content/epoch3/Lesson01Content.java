package com.socraticjava.content.epoch3;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 3.1: Arrays - The Row of Boxes
 */
public class Lesson01Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-3-lesson-1", "Lesson 3.1: Arrays - Storing Multiple Values")
            .addTheory("The Problem: Many Related Values",
                "You want to store the scores of 5 students:\n\n" +
                "int score1 = 85;\n" +
                "int score2 = 92;\n" +
                "int score3 = 78;\n" +
                "int score4 = 95;\n" +
                "int score5 = 88;\n\n" +
                "To calculate the average:\n" +
                "int total = score1 + score2 + score3 + score4 + score5;\n" +
                "double avg = total / 5.0;\n\n" +
                "This doesn't scale! What if you have 100 students? 1000?\n\n" +
                "You need a way to store MANY values of the same type under ONE name.\n\n" +
                "This is what ARRAYS do!")
            .addAnalogy("Arrays are Like a Row of Numbered Boxes",
                "Imagine a row of mail boxes in an apartment building:\n\n" +
                "[Box 0] [Box 1] [Box 2] [Box 3] [Box 4]\n\n" +
                "Each box:\n" +
                "- Holds ONE item\n" +
                "- Has a NUMBER (index)\n" +
                "- Contains the SAME TYPE of thing\n\n" +
                "In Java:\n" +
                "int[] scores = new int[5];\n" +
                "scores[0] = 85;  // Put 85 in box 0\n" +
                "scores[1] = 92;  // Put 92 in box 1\n\n" +
                "⚠️ Arrays start at index 0, not 1!")
            .addTheory("Array Syntax",
                "Creating an array:\n\n" +
                "// Method 1: Declare size, fill later\n" +
                "int[] numbers = new int[5];\n" +
                "numbers[0] = 10;\n" +
                "numbers[1] = 20;\n\n" +
                "// Method 2: Declare with values\n" +
                "int[] numbers = {10, 20, 30, 40, 50};\n\n" +
                "Accessing elements:\n" +
                "System.out.println(numbers[0]);  // 10\n" +
                "System.out.println(numbers[4]);  // 50\n\n" +
                "Array length:\n" +
                "numbers.length  // 5\n\n" +
                "Looping through array:\n" +
                "for (int i = 0; i < numbers.length; i++) {\n" +
                "    System.out.println(numbers[i]);\n" +
                "}")
            .addChallenge(createArrayChallenge())
            .estimatedMinutes(35)
            .build();
    }

    private static Challenge createArrayChallenge() {
        String starterCode =
            "public class ArraySum {\n" +
            "    public static void main(String[] args) {\n" +
            "        int[] numbers = {5, 10, 15, 20, 25};\n" +
            "        \n" +
            "        // Calculate and print the sum of all elements\n" +
            "        // Use a for loop\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-3-lesson-1-sum", "Sum Array Elements", ChallengeType.FREE_CODING)
            .description(
                "Given an array: {5, 10, 15, 20, 25}\n\n" +
                "Use a for loop to calculate the sum of all elements.\n" +
                "Print the total sum.\n\n" +
                "Expected output: 75"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should calculate sum = 75",
                new Object[]{},
                "75",
                true
            ))
            .build();
    }
}
