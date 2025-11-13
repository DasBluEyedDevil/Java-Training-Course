package com.socraticjava.content.epoch5;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 5.2: SQL Basics - Creating Tables and Inserting Data
 */
public class Lesson02Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-5-lesson-2", "Lesson 5.2: SQL Basics - Your First Database")
            .addTheory("Creating a Table - The Foundation",
                "Before storing data, you need a TABLE (like a spreadsheet):\n\n" +
                "CREATE TABLE students (\n" +
                "    id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "    name VARCHAR(100) NOT NULL,\n" +
                "    age INT,\n" +
                "    email VARCHAR(255) UNIQUE,\n" +
                "    gpa DECIMAL(3, 2)\n" +
                ");\n\n" +
                "BREAKING IT DOWN:\n" +
                "- id: Unique identifier for each student\n" +
                "- PRIMARY KEY: This field uniquely identifies each row\n" +
                "- AUTO_INCREMENT: Database automatically assigns 1, 2, 3...\n" +
                "- VARCHAR(100): Text up to 100 characters\n" +
                "- NOT NULL: This field is required (can't be empty)\n" +
                "- UNIQUE: No two students can have the same email\n" +
                "- DECIMAL(3, 2): Numbers like 3.75 (3 total digits, 2 after decimal)")
            .addTheory("Common Data Types in SQL",
                "TEXT TYPES:\n" +
                "- CHAR(n): Fixed-length text (CHAR(5) for \"HELLO\")\n" +
                "- VARCHAR(n): Variable-length text (VARCHAR(255) for emails)\n" +
                "- TEXT: Very long text (articles, comments)\n\n" +
                "NUMBER TYPES:\n" +
                "- INT: Whole numbers (-2147483648 to 2147483647)\n" +
                "- BIGINT: Bigger whole numbers\n" +
                "- DECIMAL(p, s): Exact decimals (DECIMAL(10, 2) for money: 12345678.90)\n" +
                "- FLOAT/DOUBLE: Approximate decimals (scientific calculations)\n\n" +
                "DATE/TIME TYPES:\n" +
                "- DATE: Just date (2025-01-15)\n" +
                "- TIME: Just time (14:30:00)\n" +
                "- DATETIME: Both (2025-01-15 14:30:00)\n" +
                "- TIMESTAMP: Date + time with timezone\n\n" +
                "BOOLEAN:\n" +
                "- BOOLEAN: true/false (stored as 0 or 1)")
            .addTheory("Inserting Data - Adding Rows",
                "INSERT INTO table_name (columns) VALUES (values);\n\n" +
                "SINGLE ROW:\n" +
                "INSERT INTO students (name, age, email, gpa)\n" +
                "VALUES ('Alice Johnson', 20, 'alice@example.com', 3.75);\n\n" +
                "MULTIPLE ROWS:\n" +
                "INSERT INTO students (name, age, email, gpa) VALUES\n" +
                "    ('Bob Smith', 21, 'bob@example.com', 3.50),\n" +
                "    ('Carol Davis', 19, 'carol@example.com', 3.90),\n" +
                "    ('Dave Wilson', 22, 'dave@example.com', 3.25);\n\n" +
                "AUTO_INCREMENT ID:\n" +
                "Notice we didn't specify 'id' - database assigns it automatically!\n\n" +
                "NULL VALUES:\n" +
                "INSERT INTO students (name, age) VALUES ('Eve', 20);\n" +
                "// email and gpa will be NULL")
            .addTheory("Selecting Data - Querying Tables",
                "SELECT columns FROM table WHERE conditions;\n\n" +
                "GET EVERYTHING:\n" +
                "SELECT * FROM students;\n" +
                "// * means \"all columns\"\n\n" +
                "SPECIFIC COLUMNS:\n" +
                "SELECT name, gpa FROM students;\n\n" +
                "WITH CONDITIONS:\n" +
                "SELECT * FROM students WHERE age >= 21;\n" +
                "SELECT * FROM students WHERE gpa > 3.5;\n" +
                "SELECT name FROM students WHERE email LIKE '%gmail.com';\n\n" +
                "COMBINING CONDITIONS:\n" +
                "SELECT * FROM students WHERE age > 20 AND gpa > 3.0;\n" +
                "SELECT * FROM students WHERE age < 20 OR gpa > 3.8;")
            .addAnalogy("Tables are Like Spreadsheets",
                "Excel spreadsheet:\n" +
                "| ID | Name          | Age | Email              | GPA  |\n" +
                "|----|---------------|-----|--------------------| -----|\n" +
                "| 1  | Alice Johnson | 20  | alice@example.com  | 3.75 |\n" +
                "| 2  | Bob Smith     | 21  | bob@example.com    | 3.50 |\n" +
                "| 3  | Carol Davis   | 19  | carol@example.com  | 3.90 |\n\n" +
                "Database table = Same thing!\n" +
                "- Each ROW = One student\n" +
                "- Each COLUMN = One property\n" +
                "- Each CELL = One value\n\n" +
                "SQL just lets you manipulate this \"spreadsheet\" with commands.")
            .addKeyPoint("Primary Keys - The Unique Identifier",
                "Every table should have a PRIMARY KEY:\n\n" +
                "WHY?\n" +
                "- Uniquely identifies each row\n" +
                "- Prevents duplicate entries\n" +
                "- Allows referencing from other tables\n\n" +
                "GOOD PRIMARY KEYS:\n" +
                "✓ id INT AUTO_INCREMENT\n" +
                "✓ email (if truly unique)\n" +
                "✓ username (if truly unique)\n\n" +
                "BAD PRIMARY KEYS:\n" +
                "✗ name (not unique - many \"John Smith\"s)\n" +
                "✗ age (definitely not unique)\n\n" +
                "BEST PRACTICE:\n" +
                "Always use an auto-incrementing ID as primary key.\n" +
                "Simple, guaranteed unique, fast.")
            .addChallenge(createDataTypeQuiz())
            .addChallenge(createInsertQuiz())
            .addChallenge(createSelectQuiz())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createDataTypeQuiz() {
        return new Challenge.Builder("epoch-5-lesson-2-datatypes", "SQL Data Types", ChallengeType.MULTIPLE_CHOICE)
            .description("Which data type is best for storing a person's age?")
            .addMultipleChoiceOption("A) VARCHAR(3)")
            .addMultipleChoiceOption("B) INT")
            .addMultipleChoiceOption("C) TEXT")
            .addMultipleChoiceOption("D) DECIMAL(10, 2)")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createInsertQuiz() {
        return new Challenge.Builder("epoch-5-lesson-2-insert", "Understanding INSERT", ChallengeType.MULTIPLE_CHOICE)
            .description("What happens to the 'id' field if you don't specify it in an INSERT and it's AUTO_INCREMENT?")
            .addMultipleChoiceOption("A) Error occurs")
            .addMultipleChoiceOption("B) It becomes NULL")
            .addMultipleChoiceOption("C) The database automatically assigns the next available number")
            .addMultipleChoiceOption("D) It becomes 0")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createSelectQuiz() {
        return new Challenge.Builder("epoch-5-lesson-2-select", "Understanding SELECT", ChallengeType.MULTIPLE_CHOICE)
            .description("Which SQL query selects all students with age greater than 21?")
            .addMultipleChoiceOption("A) SELECT * FROM students WHERE age > 21;")
            .addMultipleChoiceOption("B) GET * FROM students IF age > 21;")
            .addMultipleChoiceOption("C) SELECT ALL FROM students WHERE age > 21;")
            .addMultipleChoiceOption("D) FIND students WHERE age > 21;")
            .correctAnswer("A")
            .build();
    }
}
