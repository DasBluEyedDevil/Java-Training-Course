package com.socraticjava.content.epoch2;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;
import com.socraticjava.model.TestCase;

/**
 * Lesson 2.2: Constructors - Better Object Creation
 */
public class Lesson02Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-2-lesson-2", "Lesson 2.2: Constructors - Better Object Creation")
            .addTheory("The Problem: Tedious Object Setup",
                "Creating objects the current way is tedious:\n\n" +
                "Student student = new Student();\n" +
                "student.name = \"Alice\";\n" +
                "student.age = 20;\n" +
                "student.gpa = 3.8;\n\n" +
                "Four lines just to create one student! And you might forget to set a field.\n\n" +
                "Wouldn't it be nice to write:\n" +
                "Student student = new Student(\"Alice\", 20, 3.8);\n\n" +
                "This is what CONSTRUCTORS allow!")
            .addTheory("Constructor Syntax",
                "A constructor is a special method that runs when you create an object.\n\n" +
                "public class Student {\n" +
                "    String name;\n" +
                "    int age;\n" +
                "    double gpa;\n" +
                "    \n" +
                "    // Constructor\n" +
                "    public Student(String name, int age, double gpa) {\n" +
                "        this.name = name;\n" +
                "        this.age = age;\n" +
                "        this.gpa = gpa;\n" +
                "    }\n" +
                "}\n\n" +
                "Key points:\n" +
                "1. Constructor name MUST match class name\n" +
                "2. No return type (not even void)\n" +
                "3. 'this.name' refers to the field, 'name' refers to the parameter\n\n" +
                "Usage:\n" +
                "Student alice = new Student(\"Alice\", 20, 3.8);")
            .addKeyPoint("The 'this' Keyword",
                "'this' refers to the current object.\n\n" +
                "this.name = name;\n" +
                "  ↑          ↑\n" +
                " field    parameter\n\n" +
                "Without 'this', Java gets confused about which 'name' you mean!")
            .addChallenge(createConstructorChallenge())
            .estimatedMinutes(30)
            .build();
    }

    private static Challenge createConstructorChallenge() {
        String starterCode =
            "class Book {\n" +
            "    String title;\n" +
            "    String author;\n" +
            "    int pages;\n" +
            "    \n" +
            "    // Add a constructor that takes title, author, and pages\n" +
            "    // Use 'this' to set the fields\n" +
            "    \n" +
            "    void display() {\n" +
            "        System.out.println(title + \" by \" + author);\n" +
            "    }\n" +
            "}\n\n" +
            "public class BookTest {\n" +
            "    public static void main(String[] args) {\n" +
            "        Book book = new Book(\"1984\", \"Orwell\", 328);\n" +
            "        book.display();\n" +
            "    }\n" +
            "}";

        return new Challenge.Builder("epoch-2-lesson-2-constructor", "Create a Constructor", ChallengeType.FREE_CODING)
            .description(
                "Add a constructor to the Book class that takes three parameters:\n" +
                "- String title\n" +
                "- String author\n" +
                "- int pages\n\n" +
                "Use 'this' to assign the parameters to the fields.\n\n" +
                "Expected output: 1984 by Orwell"
            )
            .starterCode(starterCode)
            .methodSignature("main")
            .addTestCase(new TestCase(
                "Should create book and display it",
                new Object[]{},
                "1984 by Orwell",
                true
            ))
            .build();
    }
}
