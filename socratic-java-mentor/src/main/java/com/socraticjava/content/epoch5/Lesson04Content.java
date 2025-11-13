package com.socraticjava.content.epoch5;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 5.4: JOINs and Relationships
 */
public class Lesson04Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-5-lesson-4", "Lesson 5.4: JOINs - Connecting Tables")
            .addTheory("The Problem: Data Spread Across Tables",
                "Imagine a school database:\n\n" +
                "students table:\n" +
                "| id | name  | age |\n" +
                "|----|-------|-----|\n" +
                "| 1  | Alice | 20  |\n" +
                "| 2  | Bob   | 21  |\n\n" +
                "enrollments table:\n" +
                "| id | student_id | course_name     |\n" +
                "|----|------------|----------------|\n" +
                "| 1  | 1          | Math 101        |\n" +
                "| 2  | 1          | Physics 201     |\n" +
                "| 3  | 2          | Math 101        |\n\n" +
                "How do you find all courses Alice is enrolled in?\n" +
                "You need to CONNECT (JOIN) these tables!")
            .addTheory("Foreign Keys - Linking Tables",
                "A FOREIGN KEY references another table's PRIMARY KEY:\n\n" +
                "CREATE TABLE enrollments (\n" +
                "    id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "    student_id INT,\n" +
                "    course_name VARCHAR(100),\n" +
                "    FOREIGN KEY (student_id) REFERENCES students(id)\n" +
                ");\n\n" +
                "student_id is a FOREIGN KEY:\n" +
                "- Points to id in students table\n" +
                "- Creates a relationship between tables\n" +
                "- Enforces referential integrity\n\n" +
                "You can't:\n" +
                "- Add enrollment with student_id = 999 (student doesn't exist)\n" +
                "- Delete a student who has enrollments (without CASCADE)")
            .addTheory("INNER JOIN - Matching Rows Only",
                "INNER JOIN returns rows that exist in BOTH tables:\n\n" +
                "SELECT students.name, enrollments.course_name\n" +
                "FROM students\n" +
                "INNER JOIN enrollments ON students.id = enrollments.student_id;\n\n" +
                "Result:\n" +
                "| name  | course_name |\n" +
                "|-------|-------------|\n" +
                "| Alice | Math 101    |\n" +
                "| Alice | Physics 201 |\n" +
                "| Bob   | Math 101    |\n\n" +
                "BREAKDOWN:\n" +
                "- FROM students: Start with students table\n" +
                "- INNER JOIN enrollments: Connect to enrollments\n" +
                "- ON students.id = enrollments.student_id: How they connect\n\n" +
                "Only includes students WHO HAVE enrollments.")
            .addTheory("LEFT JOIN - Keep All Left Side Rows",
                "LEFT JOIN returns ALL rows from left table, even if no match:\n\n" +
                "SELECT students.name, enrollments.course_name\n" +
                "FROM students\n" +
                "LEFT JOIN enrollments ON students.id = enrollments.student_id;\n\n" +
                "If student 'Carol' (id=3) has no enrollments:\n" +
                "| name  | course_name |\n" +
                "|-------|-------------|\n" +
                "| Alice | Math 101    |\n" +
                "| Alice | Physics 201 |\n" +
                "| Bob   | Math 101    |\n" +
                "| Carol | NULL        |  ← Carol included even with no enrollments\n\n" +
                "USE CASE:\n" +
                "Find students who haven't enrolled in anything:\n" +
                "SELECT students.name\n" +
                "FROM students\n" +
                "LEFT JOIN enrollments ON students.id = enrollments.student_id\n" +
                "WHERE enrollments.id IS NULL;")
            .addAnalogy("JOINs are Like Matching Puzzle Pieces",
                "INNER JOIN = Only matched pieces:\n" +
                "Student ⟷ Enrollment\n" +
                "  🧩─🧩  (connected)\n" +
                "  🧩─🧩  (connected)\n" +
                "  🧩 (no match, excluded)\n\n" +
                "LEFT JOIN = Keep all left pieces:\n" +
                "Student → Enrollment\n" +
                "  🧩─🧩  (connected)\n" +
                "  🧩─🧩  (connected)\n" +
                "  🧩─❓  (student kept, enrollment NULL)\n\n" +
                "Think of LEFT table as the \"main\" one you want to keep.")
            .addTheory("Many-to-Many Relationships",
                "SCENARIO: Students can take many courses, courses have many students\n\n" +
                "BAD DESIGN: Store courses in student table\n" +
                "students:\n" +
                "| id | name  | courses_taken          |\n" +
                "|----|-------|------------------------|\n" +
                "| 1  | Alice | Math 101, Physics 201  |  ← Hard to query!\n\n" +
                "GOOD DESIGN: Junction table (join table)\n\n" +
                "students:\n" +
                "| id | name  |\n\n" +
                "courses:\n" +
                "| id | course_name |\n\n" +
                "enrollments (junction table):\n" +
                "| student_id | course_id |\n" +
                "|------------|----------|\n" +
                "| 1          | 101      |\n" +
                "| 1          | 201      |\n" +
                "| 2          | 101      |\n\n" +
                "THREE-WAY JOIN:\n" +
                "SELECT students.name, courses.course_name\n" +
                "FROM students\n" +
                "INNER JOIN enrollments ON students.id = enrollments.student_id\n" +
                "INNER JOIN courses ON enrollments.course_id = courses.id;")
            .addChallenge(createJoinQuiz())
            .addChallenge(createInnerLeftQuiz())
            .addChallenge(createForeignKeyQuiz())
            .estimatedMinutes(45)
            .build();
    }

    private static Challenge createJoinQuiz() {
        return new Challenge.Builder("epoch-5-lesson-4-join", "Understanding JOINs", ChallengeType.MULTIPLE_CHOICE)
            .description("What does the ON clause do in a JOIN?")
            .addMultipleChoiceOption("A) Filters the final results")
            .addMultipleChoiceOption("B) Specifies how to match rows between tables")
            .addMultipleChoiceOption("C) Sorts the output")
            .addMultipleChoiceOption("D) Limits the number of rows")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createInnerLeftQuiz() {
        return new Challenge.Builder("epoch-5-lesson-4-innerleft", "INNER vs LEFT JOIN", ChallengeType.MULTIPLE_CHOICE)
            .description("What's the difference between INNER JOIN and LEFT JOIN?")
            .addMultipleChoiceOption("A) INNER is faster than LEFT")
            .addMultipleChoiceOption("B) INNER only includes rows with matches in both tables, LEFT keeps all left table rows")
            .addMultipleChoiceOption("C) LEFT joins more tables than INNER")
            .addMultipleChoiceOption("D) They're the same")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createForeignKeyQuiz() {
        return new Challenge.Builder("epoch-5-lesson-4-foreignkey", "Foreign Keys", ChallengeType.MULTIPLE_CHOICE)
            .description("What does a Foreign Key do?")
            .addMultipleChoiceOption("A) Makes queries faster")
            .addMultipleChoiceOption("B) References a primary key in another table, creating a relationship")
            .addMultipleChoiceOption("C) Encrypts data")
            .addMultipleChoiceOption("D) Automatically creates JOINs")
            .correctAnswer("B")
            .build();
    }
}
