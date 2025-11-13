package com.socraticjava.content.epoch1;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 1.3: While Loops - Repetition
 * Introduction to loops and the while statement
 */
public class Lesson03Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-1-lesson-3", "Lesson 1.3: While Loops - Mastering Repetition")
            .addTheory("The Problem: Repeating Tasks",
                "Imagine you need to print \"Hello\" 100 times. You COULD write:\n\n" +
                "System.out.println(\"Hello\");\n" +
                "System.out.println(\"Hello\");\n" +
                "System.out.println(\"Hello\");\n" +
                "// ...97 more times!\n\n" +
                "This is:\n" +
                "❌ Tedious to write\n" +
                "❌ Error-prone (did you type exactly 100?)\n" +
                "❌ Hard to change (what if you need 1000?)\n\n" +
                "Real-world examples of repetition:\n" +
                "• A game runs the same game loop thousands of times\n" +
                "• A server checks for new requests continuously\n" +
                "• You scroll through a list of items one by one\n\n" +
                "You need a way to say: \"KEEP DOING THIS until a condition is met.\"\n\n" +
                "This is what LOOPS do!")
            .addAnalogy("Loops are Like \"Simon Says\" Rules",
                "Remember the game \"Simon Says\"?\n\n" +
                "\"Keep jumping while I'm clapping\"\n\n" +
                "You KEEP jumping (repeating the action) WHILE the condition (clapping) is true.\n" +
                "When the clapping stops (condition becomes false), you stop jumping.\n\n" +
                "A while loop works the same way:\n\n" +
                "while (condition is true) {\n" +
                "    keep doing this\n" +
                "}\n\n" +
                "Another analogy: Brushing your teeth\n\n" +
                "while (teeth are dirty) {\n" +
                "    brush teeth;\n" +
                "}\n\n" +
                "You keep brushing WHILE the condition (teeth are dirty) is true.\n" +
                "Once your teeth are clean (condition is false), you stop.")
            .addTheory("While Loop Syntax",
                "The basic structure:\n\n" +
                "while (condition) {\n" +
                "    // Code to repeat\n" +
                "}\n\n" +
                "Real example - Count to 5:\n\n" +
                "int count = 1;\n" +
                "while (count <= 5) {\n" +
                "    System.out.println(count);\n" +
                "    count++;  // CRITICAL: Change the condition!\n" +
                "}\n\n" +
                "How it works:\n" +
                "1. Check: Is count <= 5? (1 <= 5? YES)\n" +
                "2. Run code: Print 1, then count becomes 2\n" +
                "3. Check again: Is count <= 5? (2 <= 5? YES)\n" +
                "4. Run code: Print 2, then count becomes 3\n" +
                "5. Repeat until count is 6\n" +
                "6. Check: Is count <= 5? (6 <= 5? NO)\n" +
                "7. STOP - exit the loop\n\n" +
                "Output: 1 2 3 4 5")
            .addTheory("The Infinite Loop Danger",
                "⚠️ CRITICAL MISTAKE: Forgetting to change the condition\n\n" +
                "WRONG:\n" +
                "int count = 1;\n" +
                "while (count <= 5) {\n" +
                "    System.out.println(count);\n" +
                "    // Forgot count++!\n" +
                "}\n\n" +
                "This creates an INFINITE LOOP:\n" +
                "- count is always 1\n" +
                "- 1 <= 5 is always true\n" +
                "- Loop runs FOREVER\n" +
                "- Your program hangs/crashes\n\n" +
                "GOLDEN RULE: Always modify the loop variable inside the loop!\n\n" +
                "Common patterns:\n" +
                "• Counting up: count++\n" +
                "• Counting down: count--\n" +
                "• Reading input: get next value from user")
            .addKeyPoint("When to Use While Loops",
                "Use a while loop when:\n\n" +
                "✓ You DON'T know how many times to repeat in advance\n" +
                "   - \"Keep asking until user enters valid input\"\n" +
                "   - \"Keep running game until player loses all lives\"\n\n" +
                "✓ The repetition depends on a condition\n" +
                "   - while (userIsLoggedIn) { ... }\n" +
                "   - while (hasMoreData) { ... }\n\n" +
                "Example patterns:\n\n" +
                "// Sum numbers from 1 to 10\n" +
                "int sum = 0;\n" +
                "int n = 1;\n" +
                "while (n <= 10) {\n" +
                "    sum += n;  // Add n to sum\n" +
                "    n++;       // Move to next number\n" +
                "}\n" +
                "// sum is now 55\n\n" +
                "// Countdown\n" +
                "int countdown = 5;\n" +
                "while (countdown > 0) {\n" +
                "    System.out.println(countdown);\n" +
                "    countdown--;\n" +
                "}\n" +
                "System.out.println(\"Blast off!\");\n" +
                "// Prints: 5 4 3 2 1 Blast off!")
            .addChallenge(createWhileQuiz())
            .addChallenge(createCountingChallenge())
            .addChallenge(createInfiniteLoopQuiz())
            .addChallenge(createSumChallenge())
            .estimatedMinutes(35)
            .build();
    }

    private static Challenge createWhileQuiz() {
        return new Challenge.Builder("epoch-1-lesson-3-quiz1", "Understanding While Loops", ChallengeType.MULTIPLE_CHOICE)
            .description(
                "What will this code print?\n\n" +
                "int x = 3;\n" +
                "while (x > 0) {\n" +
                "    System.out.println(x);\n" +
                "    x--;\n" +
                "}"
            )
            .addMultipleChoiceOption("A) 3 2 1")
            .addMultipleChoiceOption("B) 3 2 1 0")
            .addMultipleChoiceOption("C) 2 1 0")
            .addMultipleChoiceOption("D) Infinite loop")
            .correctAnswer("A")
            .build();
    }

    private static Challenge createCountingChallenge() {
        String starterCode =
            "public class CountToTen {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Use a while loop to print numbers 1 to 10\n" +
            "        // Each number should be on its own line\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-3-count", "Count to 10", ChallengeType.FREE_CODING)
            .description(
                "Write a while loop that prints the numbers 1 through 10, each on a separate line.\n\n" +
                "Steps:\n" +
                "1. Create a variable 'i' starting at 1\n" +
                "2. Create a while loop that runs while i <= 10\n" +
                "3. Inside the loop, print i\n" +
                "4. Increment i (i++)\n\n" +
                "Expected output:\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print numbers 1 to 10",
                new Object[]{},
                "1\n2\n3\n4\n5\n6\n7\n8\n9\n10",
                true
            ))
            .build();
    }

    private static Challenge createInfiniteLoopQuiz() {
        return new Challenge.Builder("epoch-1-lesson-3-quiz2", "Avoiding Infinite Loops", ChallengeType.MULTIPLE_CHOICE)
            .description(
                "Which of these creates an infinite loop?\n\n" +
                "A) int x = 5;\n" +
                "   while (x > 0) { x--; }\n\n" +
                "B) int x = 5;\n" +
                "   while (x > 0) { System.out.println(x); }\n\n" +
                "C) int x = 0;\n" +
                "   while (x < 5) { x++; }\n\n" +
                "D) int x = 5;\n" +
                "   while (x < 10) { x++; }"
            )
            .addMultipleChoiceOption("A) Option A")
            .addMultipleChoiceOption("B) Option B")
            .addMultipleChoiceOption("C) Option C")
            .addMultipleChoiceOption("D) Option D")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createSumChallenge() {
        String starterCode =
            "public class SumNumbers {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Calculate the sum of numbers from 1 to 5\n" +
            "        // Expected result: 1 + 2 + 3 + 4 + 5 = 15\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-3-sum", "Sum with While Loop", ChallengeType.FREE_CODING)
            .description(
                "Use a while loop to calculate the sum of numbers from 1 to 5.\n\n" +
                "Steps:\n" +
                "1. Create variable 'sum' starting at 0\n" +
                "2. Create variable 'n' starting at 1\n" +
                "3. While n <= 5:\n" +
                "   - Add n to sum (sum += n)\n" +
                "   - Increment n\n" +
                "4. Print the final sum\n\n" +
                "Expected output: 15\n" +
                "(Because 1 + 2 + 3 + 4 + 5 = 15)"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should calculate sum 1+2+3+4+5 = 15",
                new Object[]{},
                "15",
                true
            ))
            .build();
    }
}
