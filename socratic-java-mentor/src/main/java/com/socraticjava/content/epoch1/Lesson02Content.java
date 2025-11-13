package com.socraticjava.content.epoch1;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 1.2: Operators and Expressions
 * Teaching arithmetic operators, precedence, and compound operations
 */
public class Lesson02Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-1-lesson-2", "Lesson 1.2: Operators and Expressions")
            .addTheory("The Problem: Doing Math with Variables",
                "You can store numbers in variables, but programs need to DO THINGS with those numbers:\n\n" +
                "• A calculator adds, subtracts, multiplies, divides\n" +
                "• A game tracks score: score = score + 10\n" +
                "• A store calculates total: price * quantity\n" +
                "• A thermometer converts: fahrenheit = (celsius * 9/5) + 32\n\n" +
                "Java provides OPERATORS—symbols that perform operations on values.\n\n" +
                "Think of operators as the buttons on a calculator: +, -, ×, ÷")
            .addAnalogy("Operators are Calculator Buttons",
                "Imagine a calculator:\n\n" +
                "You press: 5 [+] 3 [=]\n" +
                "Result: 8\n\n" +
                "In Java:\n" +
                "int result = 5 + 3;  // result is 8\n\n" +
                "The '+' is an OPERATOR. It takes two values (5 and 3) and produces a result (8).\n\n" +
                "Just like a calculator has buttons for +, -, ×, ÷, Java has operator symbols:\n" +
                "+ for addition\n" +
                "- for subtraction\n" +
                "* for multiplication (can't use × symbol!)\n" +
                "/ for division\n" +
                "% for remainder (modulo)")
            .addTheory("The Five Basic Arithmetic Operators",
                "1. ADDITION (+)\n" +
                "   int sum = 10 + 5;     // sum is 15\n" +
                "   int x = 7;\n" +
                "   int y = 3;\n" +
                "   int total = x + y;    // total is 10\n\n" +
                "2. SUBTRACTION (-)\n" +
                "   int difference = 10 - 5;  // 5\n" +
                "   int age = 30;\n" +
                "   int yearsAgo = age - 10;  // 20\n\n" +
                "3. MULTIPLICATION (*)\n" +
                "   int product = 10 * 5;     // 50\n" +
                "   int price = 20;\n" +
                "   int quantity = 3;\n" +
                "   int total = price * quantity;  // 60\n\n" +
                "4. DIVISION (/)\n" +
                "   int quotient = 10 / 5;    // 2\n" +
                "   ⚠️ WARNING: int division truncates decimals!\n" +
                "   int result = 10 / 3;      // 3 (not 3.333...)\n" +
                "   double result = 10.0 / 3; // 3.333...\n\n" +
                "5. MODULO (%) - Remainder after division\n" +
                "   int remainder = 10 % 3;   // 1 (10 ÷ 3 = 3 remainder 1)\n" +
                "   int rem = 17 % 5;         // 2 (17 ÷ 5 = 3 remainder 2)\n" +
                "   Use cases: Check if even (x % 2 == 0), cycle through values")
            .addTheory("Order of Operations (PEMDAS)",
                "Just like in math, Java follows order of operations:\n\n" +
                "Parentheses → Multiplication/Division → Addition/Subtraction\n\n" +
                "Examples:\n" +
                "int result = 5 + 3 * 2;     // 11 (not 16!)\n" +
                "// Multiply first: 3 * 2 = 6, then add: 5 + 6 = 11\n\n" +
                "int result = (5 + 3) * 2;   // 16\n" +
                "// Parentheses first: 5 + 3 = 8, then multiply: 8 * 2 = 16\n\n" +
                "int result = 10 / 2 + 3;    // 8\n" +
                "// Divide first: 10 / 2 = 5, then add: 5 + 3 = 8\n\n" +
                "When in doubt, use parentheses to be explicit:\n" +
                "int total = (price * quantity) + tax;  // Clear intent")
            .addTheory("Shorthand Operators",
                "Java provides shortcuts for common patterns:\n\n" +
                "1. INCREMENT (add 1)\n" +
                "   int score = 10;\n" +
                "   score++;  // Same as: score = score + 1\n" +
                "   // score is now 11\n\n" +
                "2. DECREMENT (subtract 1)\n" +
                "   int lives = 3;\n" +
                "   lives--;  // Same as: lives = lives - 1\n" +
                "   // lives is now 2\n\n" +
                "3. COMPOUND ASSIGNMENT\n" +
                "   int x = 10;\n" +
                "   x += 5;   // Same as: x = x + 5  (x is 15)\n" +
                "   x -= 3;   // Same as: x = x - 3  (x is 12)\n" +
                "   x *= 2;   // Same as: x = x * 2  (x is 24)\n" +
                "   x /= 4;   // Same as: x = x / 4  (x is 6)\n\n" +
                "These are VERY common in real code!")
            .addKeyPoint("Common Mistakes to Avoid",
                "❌ WRONG: int result = 5 x 3;  (x is not an operator)\n" +
                "✓ CORRECT: int result = 5 * 3;\n\n" +
                "❌ WRONG: int half = 10 / 2.0;  (can't assign double to int)\n" +
                "✓ CORRECT: double half = 10 / 2.0;\n\n" +
                "❌ WRONG: int result = 10 / 3;  // Thinking this gives 3.33\n" +
                "✓ CORRECT: double result = 10.0 / 3.0;  // This gives 3.33\n\n" +
                "❌ WRONG: x =+ 5;  (This assigns 5, doesn't add!)\n" +
                "✓ CORRECT: x += 5;  (This adds 5 to x)\n\n" +
                "Remember: In integer division, the decimal part is thrown away!\n" +
                "7 / 2 = 3 (not 3.5)")
            .addChallenge(createOperatorQuiz())
            .addChallenge(createCalculatorChallenge())
            .addChallenge(createOrderOfOpsQuiz())
            .addChallenge(createCompoundChallenge())
            .estimatedMinutes(30)
            .build();
    }

    private static Challenge createOperatorQuiz() {
        return new Challenge.Builder("epoch-1-lesson-2-quiz1", "Understanding Operators", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the result of: 17 % 5")
            .addMultipleChoiceOption("A) 3")
            .addMultipleChoiceOption("B) 2")
            .addMultipleChoiceOption("C) 3.4")
            .addMultipleChoiceOption("D) 0")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createCalculatorChallenge() {
        String starterCode =
            "public class SimpleCalculator {\n" +
            "    public static void main(String[] args) {\n" +
            "        int a = 20;\n" +
            "        int b = 5;\n" +
            "        \n" +
            "        // Calculate: (a + b) * 2\n" +
            "        // Store result in variable 'result'\n" +
            "        // Then print the result\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-2-calc", "Basic Calculator", ChallengeType.FREE_CODING)
            .description(
                "Given: int a = 20 and int b = 5\n\n" +
                "Calculate: (a + b) * 2\n" +
                "Store the result in a variable called 'result'\n" +
                "Then print the result.\n\n" +
                "Expected output: 50\n" +
                "(Because 20 + 5 = 25, and 25 * 2 = 50)"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should calculate (20 + 5) * 2 = 50",
                new Object[]{},
                "50",
                true
            ))
            .build();
    }

    private static Challenge createOrderOfOpsQuiz() {
        return new Challenge.Builder("epoch-1-lesson-2-quiz2", "Order of Operations", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the value of: 5 + 3 * 2")
            .addMultipleChoiceOption("A) 16")
            .addMultipleChoiceOption("B) 11")
            .addMultipleChoiceOption("C) 10")
            .addMultipleChoiceOption("D) 13")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createCompoundChallenge() {
        String starterCode =
            "public class CompoundOps {\n" +
            "    public static void main(String[] args) {\n" +
            "        int score = 100;\n" +
            "        \n" +
            "        // Add 50 to score using += operator\n" +
            "        // Then subtract 30 using -= operator\n" +
            "        // Finally print the score\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-2-compound", "Using Compound Operators", ChallengeType.FREE_CODING)
            .description(
                "Starting with: int score = 100\n\n" +
                "1. Add 50 to score using the += operator\n" +
                "2. Subtract 30 from score using the -= operator\n" +
                "3. Print the final score\n\n" +
                "Expected output: 120\n" +
                "(100 + 50 - 30 = 120)"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should output 120",
                new Object[]{},
                "120",
                true
            ))
            .build();
    }
}
