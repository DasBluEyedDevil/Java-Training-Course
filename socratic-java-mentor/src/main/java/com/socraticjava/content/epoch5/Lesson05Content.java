package com.socraticjava.content.epoch5;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 5.5: JDBC - Connecting Java to Databases
 */
public class Lesson05Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-5-lesson-5", "Lesson 5.5: JDBC - Databases + Java")
            .addTheory("The Problem: Java Needs to Talk to Databases",
                "So far:\n" +
                "- You know Java (objects, methods, classes)\n" +
                "- You know SQL (SELECT, INSERT, JOIN)\n\n" +
                "But they're separate!\n\n" +
                "How do you run SQL from Java code?\n" +
                "How do you get query results into Java objects?\n\n" +
                "Solution: JDBC (Java Database Connectivity)\n" +
                "- Standard Java API for database access\n" +
                "- Works with MySQL, PostgreSQL, Oracle, etc.\n" +
                "- Lets you execute SQL from Java code")
            .addTheory("JDBC Connection Steps",
                "1. LOAD DRIVER (usually automatic now)\n" +
                "2. ESTABLISH CONNECTION\n" +
                "3. CREATE STATEMENT\n" +
                "4. EXECUTE QUERY\n" +
                "5. PROCESS RESULTS\n" +
                "6. CLOSE RESOURCES\n\n" +
                "Code example:\n\n" +
                "import java.sql.*;\n\n" +
                "public class DatabaseExample {\n" +
                "    public static void main(String[] args) {\n" +
                "        String url = \"jdbc:mysql://localhost:3306/school\";\n" +
                "        String user = \"root\";\n" +
                "        String password = \"password\";\n" +
                "        \n" +
                "        try (Connection conn = DriverManager.getConnection(url, user, password)) {\n" +
                "            // Connection established!\n" +
                "        } catch (SQLException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "    }\n" +
                "}\n\n" +
                "try-with-resources automatically closes connection!")
            .addTheory("Executing Queries - SELECT",
                "Use Statement or PreparedStatement:\n\n" +
                "String sql = \"SELECT * FROM students WHERE age > 20\";\n\n" +
                "try (Connection conn = DriverManager.getConnection(url, user, password);\n" +
                "     Statement stmt = conn.createStatement();\n" +
                "     ResultSet rs = stmt.executeQuery(sql)) {\n" +
                "    \n" +
                "    while (rs.next()) {  // Loop through results\n" +
                "        int id = rs.getInt(\"id\");\n" +
                "        String name = rs.getString(\"name\");\n" +
                "        int age = rs.getInt(\"age\");\n" +
                "        \n" +
                "        System.out.println(id + \": \" + name + \", \" + age);\n" +
                "    }\n" +
                "}\n\n" +
                "KEY POINTS:\n" +
                "- ResultSet = cursor over query results\n" +
                "- rs.next() = move to next row (returns false when done)\n" +
                "- rs.getInt(\"column_name\") = get value by column name\n" +
                "- rs.getString(1) = get by position (1-indexed)")
            .addTheory("Executing Updates - INSERT, UPDATE, DELETE",
                "Use executeUpdate() instead of executeQuery():\n\n" +
                "INSERT:\n" +
                "String sql = \"INSERT INTO students (name, age) VALUES ('Alice', 20)\";\n" +
                "try (Connection conn = DriverManager.getConnection(url, user, password);\n" +
                "     Statement stmt = conn.createStatement()) {\n" +
                "    \n" +
                "    int rowsAffected = stmt.executeUpdate(sql);\n" +
                "    System.out.println(\"Inserted \" + rowsAffected + \" row(s)\");\n" +
                "}\n\n" +
                "UPDATE:\n" +
                "String sql = \"UPDATE students SET age = 21 WHERE name = 'Alice'\";\n" +
                "int rowsAffected = stmt.executeUpdate(sql);\n\n" +
                "DELETE:\n" +
                "String sql = \"DELETE FROM students WHERE age < 18\";\n" +
                "int rowsAffected = stmt.executeUpdate(sql);")
            .addWarning("SQL Injection - A Critical Security Risk!",
                "NEVER concatenate user input directly into SQL:\n\n" +
                "❌ DANGEROUS CODE:\n" +
                "String userInput = \"Alice'; DROP TABLE students; --\";\n" +
                "String sql = \"SELECT * FROM students WHERE name = '\" + userInput + \"'\";\n" +
                "// Results in: SELECT * FROM students WHERE name = 'Alice'; DROP TABLE students; --'\n" +
                "// YOUR TABLE JUST GOT DELETED!\n\n" +
                "✓ SAFE CODE (PreparedStatement):\n" +
                "String sql = \"SELECT * FROM students WHERE name = ?\";\n" +
                "try (PreparedStatement pstmt = conn.prepareStatement(sql)) {\n" +
                "    pstmt.setString(1, userInput);  // Safe: input is escaped\n" +
                "    ResultSet rs = pstmt.executeQuery();\n" +
                "}\n\n" +
                "ALWAYS USE PREPAREDSTATEMENT WITH USER INPUT!")
            .addTheory("PreparedStatement - The Right Way",
                "PreparedStatement benefits:\n" +
                "✓ Prevents SQL injection\n" +
                "✓ Better performance (compiled once, reused)\n" +
                "✓ Handles special characters automatically\n\n" +
                "String sql = \"INSERT INTO students (name, age, email) VALUES (?, ?, ?)\";\n\n" +
                "try (PreparedStatement pstmt = conn.prepareStatement(sql)) {\n" +
                "    // Set parameters (1-indexed)\n" +
                "    pstmt.setString(1, \"Alice Johnson\");\n" +
                "    pstmt.setInt(2, 20);\n" +
                "    pstmt.setString(3, \"alice@example.com\");\n" +
                "    \n" +
                "    int rowsInserted = pstmt.executeUpdate();\n" +
                "    System.out.println(\"Inserted: \" + rowsInserted);\n" +
                "}\n\n" +
                "BATCH INSERT (multiple rows):\n" +
                "pstmt.setString(1, \"Alice\");\n" +
                "pstmt.setInt(2, 20);\n" +
                "pstmt.addBatch();\n\n" +
                "pstmt.setString(1, \"Bob\");\n" +
                "pstmt.setInt(2, 21);\n" +
                "pstmt.addBatch();\n\n" +
                "int[] results = pstmt.executeBatch();  // Execute all at once")
            .addKeyPoint("JDBC Best Practices",
                "1. ALWAYS USE TRY-WITH-RESOURCES\n" +
                "   - Auto-closes connections, statements, resultsets\n" +
                "   - Prevents resource leaks\n\n" +
                "2. USE PREPAREDSTATEMENT, NOT STATEMENT\n" +
                "   - Security (prevents SQL injection)\n" +
                "   - Performance (pre-compiled)\n\n" +
                "3. DON'T CONCATENATE SQL WITH USER INPUT\n" +
                "   - Use ? placeholders\n" +
                "   - Set parameters with setString(), setInt(), etc.\n\n" +
                "4. USE CONNECTION POOLING IN PRODUCTION\n" +
                "   - Libraries: HikariCP, Apache DBCP\n" +
                "   - Reuse connections (expensive to create)\n\n" +
                "5. HANDLE EXCEPTIONS PROPERLY\n" +
                "   - Log errors\n" +
                "   - Don't expose SQL details to users\n\n" +
                "6. CLOSE RESOURCES IN FINALLY OR USE TRY-WITH-RESOURCES\n" +
                "   - Connection leaks kill performance")
            .addChallenge(createJDBCQuiz())
            .addChallenge(createPreparedStatementQuiz())
            .addChallenge(createSQLInjectionQuiz())
            .estimatedMinutes(45)
            .build();
    }

    private static Challenge createJDBCQuiz() {
        return new Challenge.Builder("epoch-5-lesson-5-jdbc", "Understanding JDBC", ChallengeType.MULTIPLE_CHOICE)
            .description("What does ResultSet.next() do?")
            .addMultipleChoiceOption("A) Executes the query")
            .addMultipleChoiceOption("B) Closes the connection")
            .addMultipleChoiceOption("C) Moves to the next row in the result set")
            .addMultipleChoiceOption("D) Returns the next column value")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createPreparedStatementQuiz() {
        return new Challenge.Builder("epoch-5-lesson-5-prepared", "PreparedStatement Benefits", ChallengeType.MULTIPLE_CHOICE)
            .description("Why should you use PreparedStatement instead of Statement?")
            .addMultipleChoiceOption("A) It's easier to write")
            .addMultipleChoiceOption("B) It prevents SQL injection and improves performance")
            .addMultipleChoiceOption("C) It works with more databases")
            .addMultipleChoiceOption("D) It automatically creates tables")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createSQLInjectionQuiz() {
        return new Challenge.Builder("epoch-5-lesson-5-injection", "SQL Injection", ChallengeType.MULTIPLE_CHOICE)
            .description("Which code is vulnerable to SQL injection?")
            .addMultipleChoiceOption("A) pstmt.setString(1, userInput);")
            .addMultipleChoiceOption("B) String sql = \"SELECT * FROM users WHERE name = ?\"")
            .addMultipleChoiceOption("C) String sql = \"SELECT * FROM users WHERE name = '\" + userInput + \"'\";")
            .addMultipleChoiceOption("D) try-with-resources statement")
            .correctAnswer("C")
            .build();
    }
}
