package com.socraticjava.content.epoch5;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 5.1: Why Databases?
 */
public class Lesson01Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-5-lesson-1", "Lesson 5.1: Why Do We Need Databases?")
            .addTheory("The Problem: Data Disappears",
                "Your program can store data in variables and arrays:\n\n" +
                "ArrayList<Student> students = new ArrayList<>();\n" +
                "students.add(new Student(\"Alice\", 20));\n\n" +
                "But when the program ends... POOF! All data is gone.\n\n" +
                "Next time you run it, you start from scratch.\n\n" +
                "Real applications need PERSISTENCE:\n" +
                "- User accounts must survive restarts\n" +
                "- Orders must be saved\n" +
                "- Posts, messages, files must persist\n\n" +
                "This is why we need DATABASES.")
            .addAnalogy("Databases are Like Libraries",
                "Your program's memory (variables) = Your desk\n" +
                "- Fast access\n" +
                "- Limited space\n" +
                "- Temporary (clear when you leave)\n\n" +
                "A database = A library\n" +
                "- Massive storage\n" +
                "- Organized (card catalog)\n" +
                "- Permanent (survives computer shutdowns)\n" +
                "- Shared (multiple programs can access)\n\n" +
                "You work with data on your desk (RAM), but save important things to the library (database).")
            .addTheory("SQL - The Database Language",
                "SQL (Structured Query Language) is how you talk to databases.\n\n" +
                "Four essential operations (CRUD):\n\n" +
                "CREATE (Insert data):\n" +
                "INSERT INTO students (name, age) VALUES ('Alice', 20);\n\n" +
                "READ (Query data):\n" +
                "SELECT * FROM students WHERE age > 18;\n\n" +
                "UPDATE (Modify data):\n" +
                "UPDATE students SET age = 21 WHERE name = 'Alice';\n\n" +
                "DELETE (Remove data):\n" +
                "DELETE FROM students WHERE age < 18;")
            .addChallenge(createDatabaseQuiz())
            .estimatedMinutes(30)
            .build();
    }

    private static Challenge createDatabaseQuiz() {
        return new Challenge.Builder("epoch-5-lesson-1-quiz", "Understanding Databases", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the main advantage of using a database instead of just variables?")
            .addMultipleChoiceOption("A) Databases are faster")
            .addMultipleChoiceOption("B) Data persists even when the program stops")
            .addMultipleChoiceOption("C) Databases are easier to use")
            .addMultipleChoiceOption("D) You don't need to write SQL")
            .correctAnswer("B")
            .build();
    }
}
