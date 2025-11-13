package com.socraticjava.content.epoch4;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 4.5: Professional Practices - Debugging, Logging, and Documentation
 */
public class Lesson05Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-4-lesson-5", "Lesson 4.5: Debugging, Logging, and Professional Habits")
            .addTheory("The Problem: System.out.println() Everywhere",
                "Beginner debugging:\n\n" +
                "public void processOrder(Order order) {\n" +
                "    System.out.println(\"Got order: \" + order);  // Debug line\n" +
                "    calculateTotal(order);\n" +
                "    System.out.println(\"Total: \" + order.getTotal());  // Debug line\n" +
                "    saveToDatabase(order);\n" +
                "    System.out.println(\"Saved!\");  // Debug line\n" +
                "}\n\n" +
                "PROBLEMS:\n" +
                "❌ Output mixed with actual program output\n" +
                "❌ Can't turn debug messages off easily\n" +
                "❌ No way to filter by severity (error vs info)\n" +
                "❌ Forgot to remove debug prints before release\n" +
                "❌ No timestamps or context\n\n" +
                "Professional solution: LOGGING FRAMEWORKS")
            .addTheory("Logging with SLF4J and Logback",
                "LOGGING = Structured way to record what's happening\n\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n\n" +
                "public class OrderService {\n" +
                "    private static final Logger logger = \n" +
                "        LoggerFactory.getLogger(OrderService.class);\n" +
                "    \n" +
                "    public void processOrder(Order order) {\n" +
                "        logger.info(\"Processing order: {}\", order.getId());\n" +
                "        \n" +
                "        try {\n" +
                "            calculateTotal(order);\n" +
                "            saveToDatabase(order);\n" +
                "            logger.info(\"Order {} processed successfully\", order.getId());\n" +
                "        } catch (Exception e) {\n" +
                "            logger.error(\"Failed to process order {}\", order.getId(), e);\n" +
                "        }\n" +
                "    }\n" +
                "}\n\n" +
                "LOGGING LEVELS (in order):\n" +
                "TRACE - Very detailed, rarely used\n" +
                "DEBUG - Development information\n" +
                "INFO - General information (\"Server started\")\n" +
                "WARN - Something unexpected, but not critical\n" +
                "ERROR - Something failed\n\n" +
                "Production: Only show WARN and ERROR\n" +
                "Development: Show DEBUG and above")
            .addTheory("Using a Debugger - The Professional's Tool",
                "Instead of println, use your IDE's DEBUGGER:\n\n" +
                "KEY FEATURES:\n\n" +
                "1. BREAKPOINTS\n" +
                "   - Click next to line number (red dot appears)\n" +
                "   - Program PAUSES when it reaches that line\n" +
                "   - Inspect ALL variables at that moment\n\n" +
                "2. STEP THROUGH CODE\n" +
                "   - Step Over: Execute current line, move to next\n" +
                "   - Step Into: Go inside method call\n" +
                "   - Step Out: Finish current method, return to caller\n\n" +
                "3. WATCH VARIABLES\n" +
                "   - See how variables change\n" +
                "   - Evaluate expressions on-the-fly\n\n" +
                "4. CALL STACK\n" +
                "   - See path of method calls\n" +
                "   - Navigate to any point\n\n" +
                "DEBUGGING WORKFLOW:\n" +
                "1. Set breakpoint where problem might be\n" +
                "2. Run in debug mode\n" +
                "3. When paused, inspect variables\n" +
                "4. Step through code line by line\n" +
                "5. Watch how values change\n" +
                "6. Identify where logic goes wrong")
            .addAnalogy("Logging vs Debugging",
                "LOGGING = Security cameras:\n" +
                "- Always recording\n" +
                "- Review footage after incident\n" +
                "- See what happened over time\n" +
                "- Good for: production issues, patterns\n\n" +
                "Example:\n" +
                "\"Why did the server crash at 3am?\"\n" +
                "→ Check logs for ERROR messages at that time\n\n" +
                "DEBUGGING = Live investigation:\n" +
                "- Stop and examine everything NOW\n" +
                "- Step-by-step analysis\n" +
                "- Immediate feedback\n" +
                "- Good for: development, understanding flow\n\n" +
                "Example:\n" +
                "\"Why is calculateTotal() returning wrong value?\"\n" +
                "→ Set breakpoint, inspect variables, step through logic\n\n" +
                "USE BOTH!")
            .addTheory("Writing Good Documentation",
                "JAVADOC = Standard way to document Java code\n\n" +
                "/**\n" +
                " * Calculates the total price including tax.\n" +
                " * \n" +
                " * @param subtotal The price before tax\n" +
                " * @param taxRate The tax rate as decimal (0.08 for 8%)\n" +
                " * @return The total price including tax\n" +
                " * @throws IllegalArgumentException if subtotal is negative\n" +
                " */\n" +
                "public double calculateTotal(double subtotal, double taxRate) {\n" +
                "    if (subtotal < 0) {\n" +
                "        throw new IllegalArgumentException(\"Subtotal cannot be negative\");\n" +
                "    }\n" +
                "    return subtotal * (1 + taxRate);\n" +
                "}\n\n" +
                "JAVADOC TAGS:\n" +
                "@param - Describes a parameter\n" +
                "@return - Describes return value\n" +
                "@throws - Describes exceptions thrown\n" +
                "@see - Links to related code\n" +
                "@since - When this was added\n" +
                "@deprecated - Mark as obsolete\n\n" +
                "WHEN TO WRITE DOCS:\n" +
                "✓ Public methods/classes (API)\n" +
                "✓ Complex algorithms\n" +
                "✓ Non-obvious behavior\n" +
                "✗ Self-explanatory code\n" +
                "✗ Private helper methods (usually)")
            .addKeyPoint("Professional Coding Habits",
                "1. USE VERSION CONTROL (Git)\n" +
                "   - Commit frequently\n" +
                "   - Write meaningful commit messages\n" +
                "   - \"Fixed bug\" ✗\n" +
                "   - \"Fix NullPointerException in user login when email is empty\" ✓\n\n" +
                "2. CODE REVIEWS\n" +
                "   - Have others review your code\n" +
                "   - Catches bugs, improves quality\n" +
                "   - Learn from feedback\n\n" +
                "3. CONSISTENT FORMATTING\n" +
                "   - Use IDE auto-format (Ctrl+Alt+L in IntelliJ)\n" +
                "   - Follow team style guide\n" +
                "   - Consistent = readable\n\n" +
                "4. MEANINGFUL NAMES\n" +
                "   - int d; ✗\n" +
                "   - int daysSinceCreation; ✓\n\n" +
                "5. SMALL COMMITS\n" +
                "   - One logical change per commit\n" +
                "   - Easy to review and revert\n\n" +
                "6. TEST BEFORE PUSHING\n" +
                "   - Always run tests\n" +
                "   - Don't break the build for teammates\n\n" +
                "7. CONTINUOUS LEARNING\n" +
                "   - Read others' code\n" +
                "   - Stay updated on best practices\n" +
                "   - Refactor as you learn")
            .addExample("Real Logging Example",
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n\n" +
                "public class UserService {\n" +
                "    private static final Logger logger = \n" +
                "        LoggerFactory.getLogger(UserService.class);\n\n" +
                "    public User authenticateUser(String username, String password) {\n" +
                "        logger.debug(\"Attempting authentication for user: {}\", username);\n" +
                "        \n" +
                "        User user = userRepository.findByUsername(username);\n" +
                "        \n" +
                "        if (user == null) {\n" +
                "            logger.warn(\"Authentication failed: user not found - {}\", username);\n" +
                "            return null;\n" +
                "        }\n" +
                "        \n" +
                "        if (!passwordMatches(user, password)) {\n" +
                "            logger.warn(\"Authentication failed: incorrect password for {}\", username);\n" +
                "            return null;\n" +
                "        }\n" +
                "        \n" +
                "        logger.info(\"User {} authenticated successfully\", username);\n" +
                "        return user;\n" +
                "    }\n" +
                "}\n\n" +
                "OUTPUT (with timestamps automatically added):\n" +
                "2025-01-15 10:30:15 DEBUG UserService - Attempting authentication for user: alice\n" +
                "2025-01-15 10:30:15 INFO UserService - User alice authenticated successfully")
            .addChallenge(createLoggingQuiz())
            .addChallenge(createDebuggingQuiz())
            .addChallenge(createBestPracticesQuiz())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createLoggingQuiz() {
        return new Challenge.Builder("epoch-4-lesson-5-logging", "Understanding Logging", ChallengeType.MULTIPLE_CHOICE)
            .description("Which log level should you use to record that the server started successfully?")
            .addMultipleChoiceOption("A) DEBUG")
            .addMultipleChoiceOption("B) INFO")
            .addMultipleChoiceOption("C) WARN")
            .addMultipleChoiceOption("D) ERROR")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createDebuggingQuiz() {
        return new Challenge.Builder("epoch-4-lesson-5-debugging", "Debugging vs Logging", ChallengeType.MULTIPLE_CHOICE)
            .description("When is a debugger MOST useful?")
            .addMultipleChoiceOption("A) Investigating production issues")
            .addMultipleChoiceOption("B) Understanding code flow during development")
            .addMultipleChoiceOption("C) Monitoring server health")
            .addMultipleChoiceOption("D) Reviewing historical data")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createBestPracticesQuiz() {
        return new Challenge.Builder("epoch-4-lesson-5-practices", "Professional Practices", ChallengeType.MULTIPLE_CHOICE)
            .description("Which is a good Git commit message?")
            .addMultipleChoiceOption("A) \"Fixed stuff\"")
            .addMultipleChoiceOption("B) \"Update\"")
            .addMultipleChoiceOption("C) \"Fix NullPointerException when processing empty orders\"")
            .addMultipleChoiceOption("D) \"Code changes\"")
            .correctAnswer("C")
            .build();
    }
}
