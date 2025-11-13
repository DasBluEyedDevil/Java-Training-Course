package com.socraticjava.content.epoch1;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 1.1: Data Types in Depth
 * Deep dive into Java's primitive data types and Strings
 */
public class Lesson01Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-1-lesson-1", "Lesson 1.1: Data Types in Depth")
            .addTheory("The Problem: Different Kinds of Information",
                "You've learned that variables store information. But not all information is the same:\n\n" +
                "• Your age: a whole number (25)\n" +
                "• Your bank balance: a decimal number (1,234.56)\n" +
                "• Your name: text (\"Alice\")\n" +
                "• Whether you're logged in: true or false\n\n" +
                "Java needs to know WHAT KIND of data each variable holds because:\n" +
                "1. Different types use different amounts of memory\n" +
                "2. Different types support different operations (you can't multiply text!)\n" +
                "3. This prevents bugs (accidentally treating a number as text)\n\n" +
                "Think of it like containers: you wouldn't store soup in a paper bag or carry books in a bucket. " +
                "The container (type) must match what you're storing (data).")
            .addAnalogy("Data Types are Like Kitchen Containers",
                "In a kitchen, you have different containers for different things:\n\n" +
                "🥛 Glass (int) - For whole items: 5 apples, 12 eggs, 100 cookies\n" +
                "   Can't hold half items. Either you have 5 apples or 6, not 5.5.\n\n" +
                "🍶 Measuring Cup (double) - For liquids: 2.5 cups of water, 1.75 liters\n" +
                "   Can hold precise decimal amounts.\n\n" +
                "📝 Label Maker (String) - For text: \"Flour\", \"Sugar\", \"Hello World\"\n" +
                "   Stores words and sentences.\n\n" +
                "🔲 Checkbox (boolean) - For yes/no: oven on? true/false\n" +
                "   Only two possible values: true or false.\n\n" +
                "Each container (data type) is designed for a specific purpose!")
            .addTheory("The Four Essential Data Types",
                "Java has several data types, but beginners need to master these four:\n\n" +
                "1. int (INTEGER - whole numbers)\n" +
                "   int age = 25;\n" +
                "   int score = -10;  // Can be negative\n" +
                "   int population = 8000000;\n" +
                "   Range: -2 billion to +2 billion (approximately)\n" +
                "   Use for: counts, ages, scores, quantities\n\n" +
                "2. double (DECIMAL NUMBERS)\n" +
                "   double price = 19.99;\n" +
                "   double temperature = -3.5;\n" +
                "   double pi = 3.14159;\n" +
                "   Use for: money, measurements, scientific calculations\n" +
                "   Note: Called 'double' because it uses double the memory of old 'float' type\n\n" +
                "3. String (TEXT)\n" +
                "   String name = \"Alice\";\n" +
                "   String message = \"Hello, World!\";\n" +
                "   String empty = \"\";  // Empty string\n" +
                "   MUST use double quotes: \"like this\"\n" +
                "   Note: String is capitalized (it's technically an object, not primitive)\n\n" +
                "4. boolean (TRUE/FALSE)\n" +
                "   boolean isLoggedIn = true;\n" +
                "   boolean gameOver = false;\n" +
                "   boolean hasWon = score > 100;\n" +
                "   Only two values: true or false (lowercase!)\n" +
                "   Use for: flags, conditions, yes/no questions")
            .addTheory("Type Conversion and Common Mistakes",
                "⚠️ Java is STRICT about types:\n\n" +
                "WRONG:\n" +
                "int x = 3.14;  // ERROR! Can't put decimal in int\n" +
                "String age = 25;  // ERROR! Numbers need quotes to be Strings\n\n" +
                "CORRECT:\n" +
                "int x = 3;  // Whole number for int\n" +
                "double y = 3.14;  // Decimal for double\n" +
                "String age = \"25\";  // Text needs quotes\n\n" +
                "Converting between types:\n" +
                "int age = 25;\n" +
                "String ageText = \"\" + age;  // Converts to \"25\"\n" +
                "// OR\n" +
                "String ageText = String.valueOf(age);\n\n" +
                "double price = 19.99;\n" +
                "int dollars = (int) price;  // Truncates to 19 (loses .99!)\n\n" +
                "Key rule: Java won't automatically convert if data could be lost.")
            .addKeyPoint("Choosing the Right Type",
                "How to decide which type to use:\n\n" +
                "Ask yourself:\n" +
                "1. Is it a number? → int or double\n" +
                "   - Whole number? → int\n" +
                "   - Needs decimals? → double\n\n" +
                "2. Is it text? → String\n" +
                "   - Even if it looks like a number (\"123\") but you're not doing math\n\n" +
                "3. Is it yes/no, on/off, true/false? → boolean\n\n" +
                "Examples:\n" +
                "✓ int numberOfStudents = 30;  // Counting people\n" +
                "✓ double temperature = 98.6;  // Needs precision\n" +
                "✓ String username = \"alice2024\";  // Text identifier\n" +
                "✓ boolean isValid = true;  // Yes/no flag\n\n" +
                "✗ double numberOfStudents = 30.0;  // Wasteful (don't need decimals)\n" +
                "✗ int price = 20;  // Risky (what about $20.50?)\n" +
                "✗ String isValid = \"true\";  // Wrong type (should be boolean)")
            .addChallenge(createDataTypeQuiz())
            .addChallenge(createMultipleVariablesChallenge())
            .addChallenge(createTypeConversionQuiz())
            .addChallenge(createPracticalTypesChallenge())
            .estimatedMinutes(30)
            .build();
    }

    private static Challenge createDataTypeQuiz() {
        return new Challenge.Builder("epoch-1-lesson-1-quiz1", "Choosing Data Types", ChallengeType.MULTIPLE_CHOICE)
            .description("Which data type should you use to store someone's email address?")
            .addMultipleChoiceOption("A) int")
            .addMultipleChoiceOption("B) double")
            .addMultipleChoiceOption("C) String")
            .addMultipleChoiceOption("D) boolean")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createMultipleVariablesChallenge() {
        String starterCode =
            "public class DataTypes {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Create these variables:\n" +
            "        // 1. int variable 'age' with value 28\n" +
            "        // 2. double variable 'price' with value 15.99\n" +
            "        // 3. String variable 'name' with value \"Bob\"\n" +
            "        // 4. boolean variable 'isActive' with value true\n" +
            "        \n" +
            "        // Print the name variable\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-1-variables", "Creating Multiple Data Types", ChallengeType.FREE_CODING)
            .description(
                "Create four variables with different data types:\n" +
                "1. int age = 28\n" +
                "2. double price = 15.99\n" +
                "3. String name = \"Bob\"\n" +
                "4. boolean isActive = true\n\n" +
                "Then print the 'name' variable.\n\n" +
                "Remember: Strings need double quotes!"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print 'Bob'",
                new Object[]{},
                "Bob",
                true
            ))
            .build();
    }

    private static Challenge createTypeConversionQuiz() {
        return new Challenge.Builder("epoch-1-lesson-1-quiz2", "Understanding Type Rules", ChallengeType.MULTIPLE_CHOICE)
            .description(
                "What happens with this code?\n\n" +
                "int x = 10;\n" +
                "int y = 3;\n" +
                "double result = x / y;"
            )
            .addMultipleChoiceOption("A) result is 3.333...")
            .addMultipleChoiceOption("B) result is 3.0")
            .addMultipleChoiceOption("C) Compilation error")
            .addMultipleChoiceOption("D) result is 3")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createPracticalTypesChallenge() {
        String starterCode =
            "public class StudentInfo {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Create a student profile with:\n" +
            "        // - String studentName = \"Alice\"\n" +
            "        // - int studentAge = 20\n" +
            "        // - double gpa = 3.85\n" +
            "        // - boolean isEnrolled = true\n" +
            "        \n" +
            "        // Print: Alice is 20 years old\n" +
            "        // (Hint: use + to combine String and numbers)\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-1-lesson-1-student", "Student Profile", ChallengeType.FREE_CODING)
            .description(
                "Create a student profile with the following variables:\n" +
                "- String studentName = \"Alice\"\n" +
                "- int studentAge = 20\n" +
                "- double gpa = 3.85\n" +
                "- boolean isEnrolled = true\n\n" +
                "Then print: Alice is 20 years old\n\n" +
                "Hint: System.out.println(studentName + \" is \" + studentAge + \" years old\");"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print student info correctly",
                new Object[]{},
                "Alice is 20 years old",
                true
            ))
            .build();
    }
}
