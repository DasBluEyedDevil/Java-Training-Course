package com.socraticjava.content.epoch0;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 0.3: Understanding Variables
 * Introduction to variables and basic data types
 */
public class Lesson03Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-0-lesson-3", "Lesson 0.3: Understanding Variables")
            .addTheory("The Problem: The Computer's Memory",
                "You've written your first program that prints text. But what if you want the program to " +
                "REMEMBER something? What if you need to store a number, do some math, and then use that result later?\n\n" +
                "Right now, your programs are like sticky notes—they can display a message, but they can't " +
                "remember anything. To write useful programs, you need the ability to STORE information.\n\n" +
                "This is where variables come in.")
            .addAnalogy("Variables are Labeled Boxes",
                "Imagine you have a storage room with boxes. Each box:\n\n" +
                "1. Has a LABEL on it (the variable name) like 'age' or 'score'\n" +
                "2. Can hold ONE thing inside (the value)\n" +
                "3. You can CHANGE what's inside anytime\n" +
                "4. You can LOOK inside to see what's stored\n\n" +
                "Example:\n" +
                "- Box labeled 'playerScore' contains: 100\n" +
                "- Box labeled 'playerName' contains: 'Alex'\n" +
                "- Box labeled 'isGameOver' contains: false\n\n" +
                "In Java, when you create a variable, you're saying:\n" +
                "\"Computer, create a box, label it with this name, and put this value inside.\"\n\n" +
                "The computer reserves a tiny spot in its memory (RAM) for that box.")
            .addTheory("Creating Variables in Java",
                "To create a variable, you need THREE things:\n\n" +
                "1. TYPE: What kind of data will the box hold? (number? text? true/false?)\n" +
                "2. NAME: What should we call this box?\n" +
                "3. VALUE: What should we put inside?\n\n" +
                "The syntax:\n" +
                "type name = value;\n\n" +
                "Examples:\n\n" +
                "int age = 25;\n" +
                "  ↑    ↑    ↑\n" +
                " type name value\n\n" +
                "Breaking it down:\n" +
                "- 'int' means 'integer' (a whole number)\n" +
                "- 'age' is the name we chose\n" +
                "- '25' is the value we're storing\n" +
                "- ';' ends the statement (like a period)\n\n" +
                "More examples:\n" +
                "int score = 100;           // Whole number\n" +
                "double price = 19.99;      // Decimal number\n" +
                "String name = \"Alice\";    // Text (must use quotes!)\n" +
                "boolean isActive = true;   // true or false\n\n" +
                "Key rule: Once you declare a variable with a type, that box can ONLY hold that type of data.")
            .addTheory("Using Variables",
                "Once you create a variable, you can:\n\n" +
                "1. PRINT IT:\n" +
                "   int age = 25;\n" +
                "   System.out.println(age);  // Prints: 25\n\n" +
                "2. CHANGE IT:\n" +
                "   age = 30;  // Now the box contains 30\n\n" +
                "3. DO MATH WITH IT:\n" +
                "   int score = 100;\n" +
                "   score = score + 10;  // Now score is 110\n\n" +
                "4. COMBINE IT WITH TEXT:\n" +
                "   System.out.println(\"Your score is: \" + score);\n" +
                "   // Prints: Your score is: 110\n\n" +
                "The '+' symbol does different things:\n" +
                "- With numbers: adds them (5 + 3 = 8)\n" +
                "- With text: joins them (\"Hello\" + \"World\" = \"HelloWorld\")")
            .addKeyPoint("Variable Naming Rules",
                "You can name variables almost anything, BUT:\n\n" +
                "✓ DO:\n" +
                "- Use descriptive names: 'playerScore' not 'x'\n" +
                "- Start with a lowercase letter: 'score', 'myAge'\n" +
                "- Use camelCase for multiple words: 'highScore', 'userName'\n\n" +
                "✗ DON'T:\n" +
                "- Use spaces: 'my age' is WRONG, use 'myAge'\n" +
                "- Start with a number: '2cool' is WRONG, use 'cool2'\n" +
                "- Use special characters: 'score$' is usually wrong\n" +
                "- Use Java keywords: 'int', 'class', 'public' are reserved\n\n" +
                "Good names: age, playerScore, userName, totalCost\n" +
                "Bad names: a, x, asdfghjkl, thing123")
            .addChallenge(createVariableQuiz())
            .addChallenge(createVariableChallenge())
            .addChallenge(createMathChallenge())
            .estimatedMinutes(25)
            .build();
    }

    private static Challenge createVariableQuiz() {
        return new Challenge.Builder("epoch-0-lesson-3-quiz", "Understanding Variables", ChallengeType.MULTIPLE_CHOICE)
            .description("What does this code do?\n\nint score = 50;\nscore = score + 10;\nSystem.out.println(score);")
            .addMultipleChoiceOption("A) Prints 50")
            .addMultipleChoiceOption("B) Prints 60")
            .addMultipleChoiceOption("C) Prints score + 10")
            .addMultipleChoiceOption("D) Causes an error")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createVariableChallenge() {
        String starterCode =
            "public class VariableBasics {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Create a variable called 'age' and set it to 30\n" +
            "        // Then print it using System.out.println\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-0-lesson-3-variable", "Create and Print a Variable", ChallengeType.FREE_CODING)
            .description(
                "Create an integer variable named 'age' and set its value to 30.\n" +
                "Then print it using System.out.println().\n\n" +
                "Remember:\n" +
                "- Use 'int' for whole numbers\n" +
                "- Format: int variableName = value;\n" +
                "- Don't forget the semicolon!"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print '30'",
                new Object[]{},
                "30",
                true
            ))
            .build();
    }

    private static Challenge createMathChallenge() {
        String starterCode =
            "public class SimpleMath {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Create a variable 'x' with value 10\n" +
            "        // Create a variable 'y' with value 5\n" +
            "        // Create a variable 'sum' that adds x and y\n" +
            "        // Print the sum\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-0-lesson-3-math", "Variable Math", ChallengeType.FREE_CODING)
            .description(
                "Create three integer variables:\n" +
                "- x with value 10\n" +
                "- y with value 5\n" +
                "- sum that equals x + y\n\n" +
                "Then print the sum variable.\n\n" +
                "The output should be: 15"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print '15'",
                new Object[]{},
                "15",
                true
            ))
            .build();
    }
}
