package com.socraticjava.content.epoch1;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 1.4: For Loops - Counting Made Easy
 * Introduction to for loops and when to use them
 */
public class Lesson04Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-1-lesson-4", "Lesson 1.4: For Loops - Counting Made Easy")
            .addTheory("The Problem: While Loops for Counting Feel Clunky",
                "You learned to count with a while loop:\n\n" +
                "int i = 1;\n" +
                "while (i <= 10) {\n" +
                "    System.out.println(i);\n" +
                "    i++;\n" +
                "}\n\n" +
                "This works, but notice the pattern:\n" +
                "1. INITIALIZE a counter (int i = 1)\n" +
                "2. CHECK a condition (i <= 10)\n" +
                "3. UPDATE the counter (i++)\n\n" +
                "These three pieces are scattered across 4 lines! When you're just COUNTING, " +
                "this feels messy.\n\n" +
                "Java provides a cleaner syntax for this exact pattern: the FOR LOOP.\n\n" +
                "The same code with a for loop:\n\n" +
                "for (int i = 1; i <= 10; i++) {\n" +
                "    System.out.println(i);\n" +
                "}\n\n" +
                "All three pieces (init, condition, update) are on ONE line!")
            .addAnalogy("For Loops are Like a Recipe's Prep List",
                "Imagine a recipe that says:\n\n" +
                "\"For each of the 12 muffin cups: fill with batter, bake for 20 minutes\"\n\n" +
                "In Java:\n\n" +
                "for (cup 1 to 12) {\n" +
                "    fill cup with batter;\n" +
                "    bake cup;\n" +
                "}\n\n" +
                "The recipe header tells you:\n" +
                "- START: cup 1\n" +
                "- STOP WHEN: you've done 12\n" +
                "- EACH TIME: move to the next cup\n\n" +
                "A for loop packages all this information upfront, making it crystal clear:\n" +
                "\"I'm going to do this task a specific number of times.\"")
            .addTheory("For Loop Syntax",
                "for (initialization; condition; update) {\n" +
                "    // Code to repeat\n" +
                "}\n\n" +
                "Three parts separated by semicolons:\n\n" +
                "1. INITIALIZATION: Run once at the start\n" +
                "   int i = 0  (Create counter, set starting value)\n\n" +
                "2. CONDITION: Check before each iteration\n" +
                "   i < 10  (Keep going while this is true)\n\n" +
                "3. UPDATE: Run after each iteration\n" +
                "   i++  (Change the counter)\n\n" +
                "Example - Print 0 to 4:\n\n" +
                "for (int i = 0; i < 5; i++) {\n" +
                "    System.out.println(i);\n" +
                "}\n\n" +
                "Execution flow:\n" +
                "1. int i = 0  (i is 0)\n" +
                "2. Check: i < 5? YES → run code (print 0)\n" +
                "3. Update: i++ (i is now 1)\n" +
                "4. Check: i < 5? YES → run code (print 1)\n" +
                "5. Update: i++ (i is now 2)\n" +
                "... continues until i is 5\n" +
                "6. Check: i < 5? NO → stop\n\n" +
                "Output: 0 1 2 3 4")
            .addTheory("Common For Loop Patterns",
                "1. COUNT FROM 0 TO N (exclusive)\n" +
                "for (int i = 0; i < 10; i++) {\n" +
                "    // Runs 10 times: i = 0, 1, 2...9\n" +
                "}\n" +
                "This is THE most common pattern in programming!\n\n" +
                "2. COUNT FROM 1 TO N (inclusive)\n" +
                "for (int i = 1; i <= 10; i++) {\n" +
                "    // Runs 10 times: i = 1, 2, 3...10\n" +
                "}\n\n" +
                "3. COUNT BACKWARDS\n" +
                "for (int i = 10; i > 0; i--) {\n" +
                "    // Runs 10 times: i = 10, 9, 8...1\n" +
                "}\n\n" +
                "4. COUNT BY 2s\n" +
                "for (int i = 0; i < 10; i += 2) {\n" +
                "    // Runs 5 times: i = 0, 2, 4, 6, 8\n" +
                "}\n\n" +
                "5. ITERATE OVER CHARACTERS\n" +
                "String word = \"Hello\";\n" +
                "for (int i = 0; i < word.length(); i++) {\n" +
                "    System.out.println(word.charAt(i));\n" +
                "    // Prints: H e l l o (each on new line)\n" +
                "}")
            .addKeyPoint("While vs For: When to Use Each",
                "Use FOR when:\n" +
                "✓ You know how many times to loop\n" +
                "✓ You're counting or iterating\n" +
                "✓ You have a clear start, end, and step\n\n" +
                "for (int i = 0; i < 100; i++) { ... }  // \"Do this 100 times\"\n\n" +
                "Use WHILE when:\n" +
                "✓ You DON'T know how many times to loop\n" +
                "✓ The condition is complex\n" +
                "✓ Not necessarily counting\n\n" +
                "while (userWantsMore) { ... }  // \"Keep going until user says stop\"\n\n" +
                "Rule of thumb:\n" +
                "Counting → for\n" +
                "Condition-based → while")
            .addChallenge(createForLoopQuiz())
            .addChallenge(createPrintNumbersChallenge())
            .addChallenge(createMultiplicationTableChallenge())
            .addChallenge(createCountdownChallenge())
            .estimatedMinutes(30)
            .build();
    }

    private static Challenge createForLoopQuiz() {
        return new Challenge.Builder("epoch-1-lesson-4-quiz1", "Understanding For Loops", ChallengeType.MULTIPLE_CHOICE)
            .description(
                "How many times does this loop run?\n\n" +
                "for (int i = 0; i < 5; i++) {\n" +
                "    System.out.println(i);\n" +
                "}"
            )
            .addMultipleChoiceOption("A) 4 times")
            .addMultipleChoiceOption("B) 5 times")
            .addMultipleChoiceOption("C) 6 times")
            .addMultipleChoiceOption("D) Infinite times")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createPrintNumbersChallenge() {
        String starterCode =
            "public class ForLoopPractice {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Use a for loop to print numbers 1 to 5\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-4-numbers", "Print 1 to 5", ChallengeType.FREE_CODING)
            .description(
                "Write a for loop that prints the numbers 1 through 5, each on a separate line.\n\n" +
                "Use the pattern: for (int i = 1; i <= 5; i++)\n\n" +
                "Expected output:\n1\n2\n3\n4\n5"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print 1 through 5",
                new Object[]{},
                "1\n2\n3\n4\n5",
                true
            ))
            .build();
    }

    private static Challenge createMultiplicationTableChallenge() {
        String starterCode =
            "public class MultiplicationTable {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Print the 5 times table (5, 10, 15, 20, 25)\n" +
            "        // Use a for loop from 1 to 5\n" +
            "        // In each iteration, print i * 5\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-4-multable", "5 Times Table", ChallengeType.FREE_CODING)
            .description(
                "Use a for loop to print the 5 times table.\n\n" +
                "Loop from i = 1 to i = 5\n" +
                "Each iteration, print: i * 5\n\n" +
                "Expected output:\n5\n10\n15\n20\n25"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print 5, 10, 15, 20, 25",
                new Object[]{},
                "5\n10\n15\n20\n25",
                true
            ))
            .build();
    }

    private static Challenge createCountdownChallenge() {
        String starterCode =
            "public class Countdown {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Count down from 5 to 1 using a for loop\n" +
            "        // Then print \"Liftoff!\"\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-4-countdown", "Rocket Countdown", ChallengeType.FREE_CODING)
            .description(
                "Create a countdown from 5 to 1, then print \"Liftoff!\"\n\n" +
                "Use a for loop that:\n" +
                "- Starts at 5\n" +
                "- Runs while i > 0\n" +
                "- Decrements with i--\n\n" +
                "Expected output:\n5\n4\n3\n2\n1\nLiftoff!"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should countdown and print Liftoff!",
                new Object[]{},
                "5\n4\n3\n2\n1\nLiftoff!",
                true
            ))
            .build();
    }
}
