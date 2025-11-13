package com.socraticjava.content.epoch1;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 1.5: Introduction to Methods
 * Teaching functions/methods as reusable code blocks
 */
public class Lesson05Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-1-lesson-5", "Lesson 1.5: Introduction to Methods")
            .addTheory("The Problem: Copy-Pasting Code is Dangerous",
                "Imagine you need to greet different people in your program:\n\n" +
                "System.out.println(\"Hello, Alice!\");\n" +
                "System.out.println(\"Welcome, Alice!\");\n" +
                "System.out.println(\"Goodbye, Alice!\");\n\n" +
                "System.out.println(\"Hello, Bob!\");\n" +
                "System.out.println(\"Welcome, Bob!\");\n" +
                "System.out.println(\"Goodbye, Bob!\");\n\n" +
                "This is:\n" +
                "❌ Repetitive (same pattern, different name)\n" +
                "❌ Hard to change (what if you want to add another line?)\n" +
                "❌ Error-prone (easy to make typos)\n\n" +
                "What if you could define the greeting pattern ONCE, and then just say:\n" +
                "greet(\"Alice\");\n" +
                "greet(\"Bob\");\n\n" +
                "This is what METHODS (also called functions) do! They let you package code into reusable chunks.")
            .addAnalogy("Methods are Like Vending Machine Buttons",
                "Think of a vending machine:\n\n" +
                "1. You press button \"A1\"\n" +
                "2. The machine runs a sequence: move arm → grab item → drop item\n" +
                "3. You get your snack\n\n" +
                "You don't need to know HOW the machine works internally. You just press the button " +
                "(call the method) and get the result.\n\n" +
                "In programming:\n\n" +
                "calculateTax(100)  ← Press the button (input: 100)\n" +
                "↓ Internal magic happens\n" +
                "Returns: 8  ← You get the result\n\n" +
                "A method is a button with a name. When you \"press\" it (call it), it runs some code " +
                "and can give you back a result.\n\n" +
                "Another analogy: A recipe\n" +
                "Recipe name: \"Make Pancakes\"\n" +
                "Inputs (ingredients): flour, eggs, milk\n" +
                "Steps: mix, cook, flip\n" +
                "Output: delicious pancakes\n\n" +
                "makePancakes(flour, eggs, milk) → pancakes")
            .addTheory("Method Syntax - The Basics",
                "A method has several parts:\n\n" +
                "public static RETURN_TYPE methodName(PARAMETERS) {\n" +
                "    // Code to run\n" +
                "    return result;  // If return type isn't void\n" +
                "}\n\n" +
                "Let's break it down:\n\n" +
                "1. 'public static' - For now, always write these (you'll understand later)\n" +
                "2. RETURN_TYPE - What the method gives back (int, double, String, or 'void' for nothing)\n" +
                "3. methodName - What you call it (use camelCase: calculateSum, printMessage)\n" +
                "4. PARAMETERS - Inputs in parentheses (like variables: int x, String name)\n" +
                "5. CODE BLOCK - What the method does\n" +
                "6. 'return' - Sends the result back (not needed if return type is 'void')\n\n" +
                "Example 1: Method that returns nothing (void)\n\n" +
                "public static void sayHello() {\n" +
                "    System.out.println(\"Hello!\");\n" +
                "}\n\n" +
                "Calling it:\n" +
                "sayHello();  // Prints: Hello!\n\n" +
                "Example 2: Method that returns a value\n\n" +
                "public static int addNumbers(int a, int b) {\n" +
                "    int sum = a + b;\n" +
                "    return sum;\n" +
                "}\n\n" +
                "Calling it:\n" +
                "int result = addNumbers(5, 3);  // result is 8")
            .addTheory("Parameters and Return Values",
                "PARAMETERS: Inputs to the method\n\n" +
                "public static void greet(String name) {\n" +
                "    System.out.println(\"Hello, \" + name + \"!\");\n" +
                "}\n\n" +
                "greet(\"Alice\");  // Prints: Hello, Alice!\n" +
                "greet(\"Bob\");    // Prints: Hello, Bob!\n\n" +
                "'name' is a parameter—a variable that gets its value when you call the method.\n\n" +
                "RETURN VALUES: Outputs from the method\n\n" +
                "public static int square(int number) {\n" +
                "    return number * number;\n" +
                "}\n\n" +
                "int result = square(5);  // result is 25\n" +
                "int x = square(10);      // x is 100\n\n" +
                "The 'return' keyword sends the value back to wherever the method was called.\n\n" +
                "Think of it like:\n" +
                "- Parameters = ingredients you give to a chef\n" +
                "- Return value = the dish the chef gives back")
            .addKeyPoint("void vs Returning a Value",
                "When to use 'void' (no return value):\n" +
                "✓ Method performs an action (print, modify something)\n" +
                "✓ You don't need a result back\n\n" +
                "public static void printBanner() {\n" +
                "    System.out.println(\"============\");\n" +
                "    System.out.println(\"WELCOME\");\n" +
                "    System.out.println(\"============\");\n" +
                "}\n\n" +
                "When to return a value:\n" +
                "✓ Method calculates something\n" +
                "✓ You need the result for later\n\n" +
                "public static double calculateTip(double bill, double percent) {\n" +
                "    return bill * (percent / 100);\n" +
                "}\n\n" +
                "double tip = calculateTip(50.0, 15);  // tip is 7.5\n\n" +
                "Key difference:\n" +
                "- void methods DO something\n" +
                "- Returning methods CALCULATE and give you something back")
            .addChallenge(createMethodQuiz())
            .addChallenge(createSimpleMethodChallenge())
            .addChallenge(createReturnMethodChallenge())
            .addChallenge(createParametersChallenge())
            .estimatedMinutes(35)
            .build();
    }

    private static Challenge createMethodQuiz() {
        return new Challenge.Builder("epoch-1-lesson-5-quiz1", "Understanding Methods", ChallengeType.MULTIPLE_CHOICE)
            .description(
                "What does this method return?\n\n" +
                "public static int multiply(int a, int b) {\n" +
                "    return a * b;\n" +
                "}\n\n" +
                "int result = multiply(4, 5);"
            )
            .addMultipleChoiceOption("A) 9")
            .addMultipleChoiceOption("B) 20")
            .addMultipleChoiceOption("C) 4")
            .addMultipleChoiceOption("D) 5")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createSimpleMethodChallenge() {
        String starterCode =
            "public class MethodPractice {\n" +
            "    // Create a method called printWelcome that prints \"Welcome!\"\n" +
            "    // Make it public static void (no parameters, no return)\n" +
            "    \n" +
            "    public static void main(String[] args) {\n" +
            "        printWelcome();  // Call your method\n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-5-simple", "Create a Simple Method", ChallengeType.FREE_CODING)
            .description(
                "Create a method called 'printWelcome' that prints \"Welcome!\"\n\n" +
                "The method should:\n" +
                "- Be public static void\n" +
                "- Have no parameters\n" +
                "- Print: Welcome!\n\n" +
                "Then call it from main.\n\n" +
                "Expected output: Welcome!"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print Welcome!",
                new Object[]{},
                "Welcome!",
                true
            ))
            .build();
    }

    private static Challenge createReturnMethodChallenge() {
        String starterCode =
            "public class ReturnMethod {\n" +
            "    // Create a method called double that takes an int\n" +
            "    // and returns that number multiplied by 2\n" +
            "    \n" +
            "    public static void main(String[] args) {\n" +
            "        int result = doubleNumber(5);\n" +
            "        System.out.println(result);\n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-5-return", "Method with Return Value", ChallengeType.FREE_CODING)
            .description(
                "Create a method called 'doubleNumber' that:\n" +
                "- Takes one int parameter called 'number'\n" +
                "- Returns number * 2\n" +
                "- Return type is int\n\n" +
                "The main method will call it with 5 and print the result.\n\n" +
                "Expected output: 10"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should return 5 * 2 = 10",
                new Object[]{},
                "10",
                true
            ))
            .build();
    }

    private static Challenge createParametersChallenge() {
        String starterCode =
            "public class MethodWithParams {\n" +
            "    // Create a method called add that takes two ints\n" +
            "    // and returns their sum\n" +
            "    \n" +
            "    public static void main(String[] args) {\n" +
            "        int sum = add(7, 3);\n" +
            "        System.out.println(sum);\n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-5-params", "Method with Parameters", ChallengeType.FREE_CODING)
            .description(
                "Create a method called 'add' that:\n" +
                "- Takes two int parameters: a and b\n" +
                "- Returns a + b\n" +
                "- Return type is int\n\n" +
                "The main method will call it with 7 and 3.\n\n" +
                "Expected output: 10"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should return 7 + 3 = 10",
                new Object[]{},
                "10",
                true
            ))
            .build();
    }
}
