package com.socraticjava.content.epoch0;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 0.2: Your First Java Program
 * Introduction to Java syntax with Hello World
 */
public class Lesson02Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-0-lesson-2", "Lesson 0.2: Your First Java Program")
            .addTheory("The Problem: Talking to the Computer",
                "You now understand that programming is writing detailed instructions. But there's a catch: " +
                "you can't just write instructions in English. You need to use a programming language.\n\n" +
                "Why? Because English is ambiguous. \"Turn left\" could mean many things. But in a programming " +
                "language, every symbol, every word, has ONE exact meaning.\n\n" +
                "Java is one of the most popular programming languages in the world. It's used by millions of " +
                "developers to build everything from Android apps to banking systems to video games.\n\n" +
                "Today, you'll write your first Java program. It won't do much—it'll just display a message " +
                "on the screen. But you'll understand every single character of code.")
            .addAnalogy("Java Code is Like a Formal Letter",
                "Imagine you're writing a letter to a government official. You can't just scribble:\n" +
                "\"Hey! Need help. Thanks!\"\n\n" +
                "You need proper structure:\n\n" +
                "Dear Official,\n" +
                "I am writing to request...\n" +
                "Sincerely,\n" +
                "Your Name\n\n" +
                "Java is the same. It has a formal structure. Every program needs certain pieces in certain places. " +
                "Once you learn the structure, you can focus on the logic.")
            .addTheory("Anatomy of a Java Program",
                "Here's the simplest possible Java program:\n\n" +
                "public class HelloWorld {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello, World!\");\n" +
                "    }\n" +
                "}\n\n" +
                "Let's break it down, line by line:\n\n" +
                "1. 'public class HelloWorld {'\n" +
                "   - 'public' means anyone can use this\n" +
                "   - 'class' is a container for code (you'll learn more later)\n" +
                "   - 'HelloWorld' is the name we chose\n" +
                "   - '{' opens a block of code\n\n" +
                "2. 'public static void main(String[] args) {'\n" +
                "   - This is the 'main method'—the entry point where your program starts\n" +
                "   - Don't worry about the details yet, just know: every program needs this line\n\n" +
                "3. 'System.out.println(\"Hello, World!\");'\n" +
                "   - 'System.out.println' means \"print this on the screen\"\n" +
                "   - The text inside quotes is what gets printed\n" +
                "   - The semicolon ';' ends the instruction (like a period ends a sentence)\n\n" +
                "4. The closing braces '}' close the blocks we opened\n\n" +
                "KEY RULE: Java is case-sensitive. 'System' and 'system' are different. Be precise!")
            .addKeyPoint("Your First Java Program",
                "Every Java program you write will follow this basic structure:\n\n" +
                "public class ClassName {\n" +
                "    public static void main(String[] args) {\n" +
                "        // Your code goes here\n" +
                "    }\n" +
                "}\n\n" +
                "For now, accept this as the \"formal letter header.\" Soon, you'll understand why each part exists.")
            .addChallenge(createHelloWorldChallenge())
            .addChallenge(createCustomMessageChallenge())
            .estimatedMinutes(20)
            .build();
    }

    /**
     * Challenge 1: Write a basic Hello World program
     */
    private static Challenge createHelloWorldChallenge() {
        String starterCode =
            "public class HelloWorld {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Write your code here to print \"Hello, World!\"\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-0-lesson-2-hello", "Your First Hello World", ChallengeType.FREE_CODING)
            .description(
                "Write a Java program that prints exactly: Hello, World!\n\n" +
                "Use System.out.println() to print the message.\n" +
                "Remember: Java is case-sensitive, and don't forget the semicolon!"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print 'Hello, World!'",
                new Object[]{},
                "Hello, World!",
                true
            ))
            .build();
    }

    /**
     * Challenge 2: Print a custom message
     */
    private static Challenge createCustomMessageChallenge() {
        String starterCode =
            "public class CustomMessage {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Print your own custom message\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-0-lesson-2-custom", "Print Your Own Message", ChallengeType.FREE_CODING)
            .description(
                "Now it's your turn! Write a program that prints: Java is awesome!\n\n" +
                "Use the same System.out.println() approach, but with your new message."
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print 'Java is awesome!'",
                new Object[]{},
                "Java is awesome!",
                true
            ))
            .build();
    }
}
