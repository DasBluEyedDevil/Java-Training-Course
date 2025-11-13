package com.socraticjava.content.epoch2;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 2.1: Classes and Objects - The Blueprint
 * Introduction to object-oriented programming
 */
public class Lesson01Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-2-lesson-1", "Lesson 2.1: Classes and Objects - The Blueprint")
            .addTheory("The Problem: Managing Complex Data",
                "Imagine you're building a student management system. Each student has:\n" +
                "- name (String)\n" +
                "- age (int)\n" +
                "- gpa (double)\n" +
                "- isEnrolled (boolean)\n\n" +
                "For 100 students, you'd need:\n" +
                "String student1Name, student2Name, ...student100Name;\n" +
                "int student1Age, student2Age, ...student100Age;\n" +
                "// 400 variables total!\n\n" +
                "This is:\n" +
                "❌ Impossible to manage\n" +
                "❌ Error-prone (easy to mix up which age belongs to which student)\n" +
                "❌ Can't use loops or methods effectively\n\n" +
                "You need a way to group related data together and create your OWN data types.\n\n" +
                "This is what CLASSES do!")
            .addAnalogy("Classes are Blueprints, Objects are Houses",
                "Think of building houses:\n\n" +
                "BLUEPRINT (Class):\n" +
                "- Defines what a house HAS: rooms, doors, windows\n" +
                "- Defines what a house DOES: open door, turn on lights\n" +
                "- You can't live in a blueprint!\n\n" +
                "ACTUAL HOUSE (Object):\n" +
                "- Built FROM the blueprint\n" +
                "- Has actual values: 3 bedrooms, blue door, 5 windows\n" +
                "- You can interact with it\n\n" +
                "From ONE blueprint, you can build MANY houses, each with different colors, sizes, etc.\n\n" +
                "In Java:\n" +
                "- Class = Blueprint (you define it once)\n" +
                "- Object = Actual instance (you create many from the blueprint)\n\n" +
                "class Student { } // This is the blueprint\n" +
                "Student alice = new Student(); // This is an actual object")
            .addTheory("Creating a Class",
                "A class is a template for creating objects.\n\n" +
                "public class Student {\n" +
                "    // FIELDS (data the object stores)\n" +
                "    String name;\n" +
                "    int age;\n" +
                "    double gpa;\n" +
                "    \n" +
                "    // METHODS (what the object can do)\n" +
                "    void study() {\n" +
                "        System.out.println(name + \" is studying\");\n" +
                "    }\n" +
                "}\n\n" +
                "Breaking it down:\n" +
                "1. 'public class Student' - declares a new type called Student\n" +
                "2. Fields (variables inside the class) - the data each student has\n" +
                "3. Methods (functions inside the class) - actions a student can do\n\n" +
                "Creating and using objects:\n\n" +
                "// Create two student objects\n" +
                "Student alice = new Student();\n" +
                "alice.name = \"Alice\";\n" +
                "alice.age = 20;\n" +
                "alice.gpa = 3.8;\n\n" +
                "Student bob = new Student();\n" +
                "bob.name = \"Bob\";\n" +
                "bob.age = 22;\n" +
                "bob.gpa = 3.5;\n\n" +
                "// Use the objects\n" +
                "alice.study();  // Prints: Alice is studying\n" +
                "bob.study();    // Prints: Bob is studying")
            .addKeyPoint("Class vs Object - The Key Difference",
                "CLASS:\n" +
                "✓ A template/blueprint\n" +
                "✓ Defined once\n" +
                "✓ Describes structure and behavior\n" +
                "✓ Example: 'Student' class\n\n" +
                "OBJECT:\n" +
                "✓ An instance created from the class\n" +
                "✓ Created many times with 'new'\n" +
                "✓ Has actual data values\n" +
                "✓ Example: alice and bob are Student objects\n\n" +
                "Analogy:\n" +
                "Class = Cookie cutter\n" +
                "Object = Actual cookie\n\n" +
                "You use ONE cookie cutter to make MANY cookies.")
            .addChallenge(createClassQuiz())
            .addChallenge(createSimpleClassChallenge())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createClassQuiz() {
        return new Challenge.Builder("epoch-2-lesson-1-quiz", "Understanding Classes", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the relationship between a class and an object?")
            .addMultipleChoiceOption("A) They are the same thing")
            .addMultipleChoiceOption("B) A class is a blueprint, objects are instances created from it")
            .addMultipleChoiceOption("C) Objects come first, then you create classes")
            .addMultipleChoiceOption("D) A class can only create one object")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createSimpleClassChallenge() {
        String starterCode =
            "class Car {\n" +
            "    String brand;\n" +
            "    int year;\n" +
            "    \n" +
            "    void honk() {\n" +
            "        System.out.println(brand + \" goes beep!\");\n" +
            "    }\n" +
            "}\n\n" +
            "public class CarTest {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Create a Car object\n" +
            "        // Set brand to \"Toyota\" and year to 2020\n" +
            "        // Call the honk() method\n" +
            "        \n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-2-lesson-1-car", "Create and Use Objects", ChallengeType.FREE_CODING)
            .description(
                "A Car class is provided with fields: brand, year and a method honk().\n\n" +
                "In main:\n" +
                "1. Create a Car object called 'myCar'\n" +
                "2. Set myCar.brand = \"Toyota\"\n" +
                "3. Set myCar.year = 2020\n" +
                "4. Call myCar.honk()\n\n" +
                "Expected output: Toyota goes beep!"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should print Toyota goes beep!",
                new Object[]{},
                "Toyota goes beep!",
                true
            ))
            .build();
    }
}
