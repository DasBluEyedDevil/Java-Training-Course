package com.socraticjava.content.epoch5;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 5.3: Advanced SQL Queries
 */
public class Lesson03Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-5-lesson-3", "Lesson 5.3: SQL Queries - Filtering, Sorting, and Aggregating")
            .addTheory("WHERE Clause - Filtering Rows",
                "The WHERE clause filters which rows to return:\n\n" +
                "COMPARISON OPERATORS:\n" +
                "SELECT * FROM students WHERE age = 21;      // Equal\n" +
                "SELECT * FROM students WHERE age != 21;     // Not equal\n" +
                "SELECT * FROM students WHERE age > 20;      // Greater than\n" +
                "SELECT * FROM students WHERE age >= 20;     // Greater or equal\n" +
                "SELECT * FROM students WHERE age BETWEEN 18 AND 22;\n\n" +
                "PATTERN MATCHING:\n" +
                "SELECT * FROM students WHERE name LIKE 'A%';      // Starts with A\n" +
                "SELECT * FROM students WHERE email LIKE '%gmail.com';  // Ends with gmail.com\n" +
                "SELECT * FROM students WHERE name LIKE '%son%';   // Contains 'son'\n\n" +
                "NULL CHECKS:\n" +
                "SELECT * FROM students WHERE email IS NULL;\n" +
                "SELECT * FROM students WHERE email IS NOT NULL;\n\n" +
                "IN OPERATOR:\n" +
                "SELECT * FROM students WHERE age IN (18, 19, 20);\n" +
                "SELECT * FROM students WHERE name IN ('Alice', 'Bob', 'Carol');")
            .addTheory("ORDER BY - Sorting Results",
                "ORDER BY sorts results:\n\n" +
                "ASCENDING (default):\n" +
                "SELECT * FROM students ORDER BY age;\n" +
                "SELECT * FROM students ORDER BY name ASC;\n\n" +
                "DESCENDING:\n" +
                "SELECT * FROM students ORDER BY gpa DESC;\n\n" +
                "MULTIPLE COLUMNS:\n" +
                "SELECT * FROM students ORDER BY age ASC, gpa DESC;\n" +
                "// Sort by age first, then by GPA within same age\n\n" +
                "LIMIT - Restricting Results:\n" +
                "SELECT * FROM students ORDER BY gpa DESC LIMIT 5;\n" +
                "// Top 5 students by GPA\n\n" +
                "OFFSET:\n" +
                "SELECT * FROM students LIMIT 10 OFFSET 20;\n" +
                "// Skip first 20 results, then get 10\n" +
                "// Useful for pagination!")
            .addTheory("Aggregate Functions - Calculations",
                "Calculate values across multiple rows:\n\n" +
                "COUNT - How many rows:\n" +
                "SELECT COUNT(*) FROM students;\n" +
                "SELECT COUNT(*) FROM students WHERE age > 20;\n\n" +
                "SUM - Total of all values:\n" +
                "SELECT SUM(age) FROM students;\n\n" +
                "AVG - Average:\n" +
                "SELECT AVG(gpa) FROM students;\n\n" +
                "MIN and MAX:\n" +
                "SELECT MIN(age) FROM students;\n" +
                "SELECT MAX(gpa) FROM students;\n\n" +
                "COMBINING:\n" +
                "SELECT \n" +
                "    COUNT(*) as total_students,\n" +
                "    AVG(gpa) as average_gpa,\n" +
                "    MIN(age) as youngest,\n" +
                "    MAX(age) as oldest\n" +
                "FROM students;")
            .addTheory("GROUP BY - Grouping Results",
                "GROUP BY combines rows with same values:\n\n" +
                "SELECT age, COUNT(*) as student_count\n" +
                "FROM students\n" +
                "GROUP BY age;\n\n" +
                "Result:\n" +
                "| age | student_count |\n" +
                "|-----|---------------|\n" +
                "| 18  | 5             |\n" +
                "| 19  | 8             |\n" +
                "| 20  | 12            |\n" +
                "| 21  | 7             |\n\n" +
                "WITH AGGREGATES:\n" +
                "SELECT age, AVG(gpa) as avg_gpa\n" +
                "FROM students\n" +
                "GROUP BY age;\n\n" +
                "HAVING - Filtering Groups:\n" +
                "SELECT age, COUNT(*) as count\n" +
                "FROM students\n" +
                "GROUP BY age\n" +
                "HAVING count > 5;\n" +
                "// Only show age groups with more than 5 students\n\n" +
                "NOTE: WHERE filters ROWS, HAVING filters GROUPS")
            .addKeyPoint("Query Order Matters!",
                "SQL queries are executed in this order:\n\n" +
                "1. FROM - Which table(s)\n" +
                "2. WHERE - Filter rows\n" +
                "3. GROUP BY - Group rows\n" +
                "4. HAVING - Filter groups\n" +
                "5. SELECT - Choose columns\n" +
                "6. ORDER BY - Sort results\n" +
                "7. LIMIT - Restrict number\n\n" +
                "Example query using all:\n\n" +
                "SELECT age, COUNT(*) as count, AVG(gpa) as avg_gpa\n" +
                "FROM students\n" +
                "WHERE email IS NOT NULL\n" +
                "GROUP BY age\n" +
                "HAVING count > 3\n" +
                "ORDER BY avg_gpa DESC\n" +
                "LIMIT 5;")
            .addChallenge(createWhereQuiz())
            .addChallenge(createAggregateQuiz())
            .addChallenge(createGroupByQuiz())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createWhereQuiz() {
        return new Challenge.Builder("epoch-5-lesson-3-where", "Understanding WHERE", ChallengeType.MULTIPLE_CHOICE)
            .description("Which SQL query finds students whose name starts with 'J'?")
            .addMultipleChoiceOption("A) SELECT * FROM students WHERE name = 'J%';")
            .addMultipleChoiceOption("B) SELECT * FROM students WHERE name LIKE 'J%';")
            .addMultipleChoiceOption("C) SELECT * FROM students WHERE name STARTS 'J';")
            .addMultipleChoiceOption("D) SELECT * FROM students WHERE name CONTAINS 'J';")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createAggregateQuiz() {
        return new Challenge.Builder("epoch-5-lesson-3-aggregate", "Aggregate Functions", ChallengeType.MULTIPLE_CHOICE)
            .description("Which function calculates the average GPA of all students?")
            .addMultipleChoiceOption("A) SELECT SUM(gpa) FROM students;")
            .addMultipleChoiceOption("B) SELECT AVG(gpa) FROM students;")
            .addMultipleChoiceOption("C) SELECT MEAN(gpa) FROM students;")
            .addMultipleChoiceOption("D) SELECT AVERAGE(gpa) FROM students;")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createGroupByQuiz() {
        return new Challenge.Builder("epoch-5-lesson-3-groupby", "GROUP BY vs WHERE", ChallengeType.MULTIPLE_CHOICE)
            .description("What's the difference between WHERE and HAVING?")
            .addMultipleChoiceOption("A) They're the same thing")
            .addMultipleChoiceOption("B) WHERE filters rows, HAVING filters groups")
            .addMultipleChoiceOption("C) WHERE is for text, HAVING is for numbers")
            .addMultipleChoiceOption("D) HAVING is faster than WHERE")
            .correctAnswer("B")
            .build();
    }
}
