package com.socraticjava.content.epoch0;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 0.4: Making Decisions with If/Else
 * Introduction to conditional logic
 */
public class Lesson04Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-0-lesson-4", "Lesson 0.4: Making Decisions with If/Else")
            .addTheory("The Problem: Programs That Adapt",
                "So far, your programs do the same thing every time. They print the same message, calculate " +
                "the same numbers. But what if you need a program that ADAPTS based on the situation?\n\n" +
                "Examples:\n" +
                "- A game that says \"You win!\" if score > 100, but \"Try again\" if score < 100\n" +
                "- A program that checks if you're old enough to vote\n" +
                "- A calculator that prevents division by zero\n\n" +
                "You need a way to make the computer ASK A QUESTION and then DO DIFFERENT THINGS based on the answer.\n\n" +
                "This is called CONDITIONAL LOGIC, and in Java, we use 'if' statements.")
            .addAnalogy("If Statements are Like a Fork in the Road",
                "Imagine you're walking and you reach a fork in the road. There's a sign that says:\n\n" +
                "\"If it's raining → Go left to the covered path\"\n" +
                "\"Otherwise → Go right to the scenic route\"\n\n" +
                "You CHECK the condition (\"Is it raining?\"), and based on the answer (yes/no), you take " +
                "a different path.\n\n" +
                "In Java:\n\n" +
                "if (it's raining) {\n" +
                "    take the covered path\n" +
                "} else {\n" +
                "    take the scenic route\n" +
                "}\n\n" +
                "The computer CHECKS the condition in parentheses. If it's true, it runs the code inside " +
                "the first curly braces. If it's false, it runs the code inside the 'else' block.")
            .addTheory("If Statement Syntax",
                "The basic structure:\n\n" +
                "if (condition) {\n" +
                "    // Code to run if condition is TRUE\n" +
                "}\n\n" +
                "Real example:\n\n" +
                "int age = 20;\n" +
                "if (age >= 18) {\n" +
                "    System.out.println(\"You can vote!\");\n" +
                "}\n\n" +
                "Breaking it down:\n" +
                "- 'if' is the keyword\n" +
                "- (age >= 18) is the CONDITION being tested\n" +
                "- '>=' means 'greater than or equal to'\n" +
                "- If the condition is TRUE, the code inside { } runs\n" +
                "- If the condition is FALSE, the code is skipped\n\n" +
                "In this example, since age is 20, and 20 >= 18 is TRUE, it prints \"You can vote!\"")
            .addTheory("Adding 'Else' for Two Paths",
                "What if you want to do something different when the condition is false?\n" +
                "Use 'else':\n\n" +
                "int age = 15;\n" +
                "if (age >= 18) {\n" +
                "    System.out.println(\"You can vote!\");\n" +
                "} else {\n" +
                "    System.out.println(\"Too young to vote.\");\n" +
                "}\n\n" +
                "Now:\n" +
                "- If age >= 18 is TRUE → prints \"You can vote!\"\n" +
                "- If age >= 18 is FALSE → prints \"Too young to vote.\"\n\n" +
                "Only ONE of these blocks will run, never both!")
            .addTheory("Comparison Operators",
                "To write conditions, you need to compare values. Java has these operators:\n\n" +
                "==  (equals)              5 == 5  → true\n" +
                "!=  (not equals)          5 != 3  → true\n" +
                ">   (greater than)        7 > 3   → true\n" +
                "<   (less than)           3 < 7   → true\n" +
                ">=  (greater or equal)    5 >= 5  → true\n" +
                "<=  (less or equal)       3 <= 7  → true\n\n" +
                "⚠️ WARNING: A common mistake!\n" +
                "- Use '==' to COMPARE: if (age == 18)\n" +
                "- Use '=' to ASSIGN: age = 18\n\n" +
                "if (age = 18) is WRONG! (You're assigning, not comparing)\n" +
                "if (age == 18) is CORRECT!")
            .addKeyPoint("Key Takeaways",
                "✓ 'if' statements let your program make decisions\n" +
                "✓ The condition in parentheses must be true or false\n" +
                "✓ Only the code in the TRUE block runs\n" +
                "✓ Use 'else' to handle the FALSE case\n" +
                "✓ Use == to compare (not =)\n\n" +
                "Think of it as:\n" +
                "if (something is true) {\n" +
                "    do this\n" +
                "} else {\n" +
                "    do that instead\n" +
                "}")
            .addChallenge(createIfElseQuiz())
            .addChallenge(createVotingChallenge())
            .addChallenge(createNumberCheckChallenge())
            .estimatedMinutes(30)
            .build();
    }

    private static Challenge createIfElseQuiz() {
        return new Challenge.Builder("epoch-0-lesson-4-quiz", "Understanding If/Else", ChallengeType.MULTIPLE_CHOICE)
            .description(
                "What will this code print?\n\n" +
                "int score = 85;\n" +
                "if (score >= 90) {\n" +
                "    System.out.println(\"A\");\n" +
                "} else {\n" +
                "    System.out.println(\"B\");\n" +
                "}"
            )
            .addMultipleChoiceOption("A) A")
            .addMultipleChoiceOption("B) B")
            .addMultipleChoiceOption("C) Both A and B")
            .addMultipleChoiceOption("D) Nothing")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createVotingChallenge() {
        String starterCode =
            "public class VotingAge {\n" +
            "    public static void main(String[] args) {\n" +
            "        int age = 20;\n" +
            "        \n" +
            "        // Write an if/else statement:\n" +
            "        // If age >= 18, print: Can vote\n" +
            "        // Otherwise, print: Cannot vote\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-0-lesson-4-voting", "Voting Eligibility Checker", ChallengeType.FREE_CODING)
            .description(
                "Complete the program to check voting eligibility.\n\n" +
                "The variable 'age' is already created with value 20.\n\n" +
                "Write an if/else statement that:\n" +
                "- Prints 'Can vote' if age >= 18\n" +
                "- Prints 'Cannot vote' otherwise\n\n" +
                "Since age is 20, it should print: Can vote"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print 'Can vote' when age is 20",
                new Object[]{},
                "Can vote",
                true
            ))
            .build();
    }

    private static Challenge createNumberCheckChallenge() {
        String starterCode =
            "public class NumberCheck {\n" +
            "    public static void main(String[] args) {\n" +
            "        int number = 15;\n" +
            "        \n" +
            "        // Write an if/else statement:\n" +
            "        // If number > 10, print: Big number\n" +
            "        // Otherwise, print: Small number\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-0-lesson-4-number", "Number Comparison", ChallengeType.FREE_CODING)
            .description(
                "Write a program that checks if a number is big or small.\n\n" +
                "The variable 'number' is set to 15.\n\n" +
                "Write an if/else statement:\n" +
                "- If number > 10, print: Big number\n" +
                "- Otherwise, print: Small number\n\n" +
                "Expected output: Big number"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print 'Big number' when number is 15",
                new Object[]{},
                "Big number",
                true
            ))
            .build();
    }
}
