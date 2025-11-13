package com.socraticjava.content.epoch4;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 4.4: Maven and Build Tools
 */
public class Lesson04Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-4-lesson-4", "Lesson 4.4: Maven - Managing Projects Like a Pro")
            .addTheory("The Problem: Managing Dependencies Manually is a Nightmare",
                "Imagine building a real project:\n\n" +
                "You need external libraries:\n" +
                "- JUnit for testing\n" +
                "- Gson for JSON parsing\n" +
                "- Apache Commons for utilities\n\n" +
                "Manual approach:\n" +
                "1. Download each JAR file from the internet\n" +
                "2. Put them in your project folder\n" +
                "3. Add to classpath manually\n" +
                "4. What if library needs OTHER libraries?\n" +
                "5. What if versions conflict?\n" +
                "6. How do teammates get same libraries?\n\n" +
                "This is CHAOS! Welcome to dependency hell.\n\n" +
                "Solution: BUILD TOOLS like Maven")
            .addTheory("What is Maven?",
                "MAVEN is a build automation tool that:\n\n" +
                "✓ Manages dependencies (downloads libraries)\n" +
                "✓ Compiles your code\n" +
                "✓ Runs tests\n" +
                "✓ Packages your app (creates JAR file)\n" +
                "✓ Enforces standard project structure\n\n" +
                "Instead of:\n" +
                "- \"Download JUnit JAR\"\n" +
                "- \"Add to classpath\"\n" +
                "- \"Hope it works\"\n\n" +
                "You write in pom.xml:\n" +
                "<dependency>\n" +
                "    <groupId>junit</groupId>\n" +
                "    <artifactId>junit</artifactId>\n" +
                "    <version>5.9.3</version>\n" +
                "</dependency>\n\n" +
                "Maven downloads it automatically! ✨")
            .addAnalogy("Maven is Like a Restaurant Manager",
                "WITHOUT MAVEN (You're a cook doing everything):\n" +
                "- Buy ingredients yourself\n" +
                "- Track which suppliers have what\n" +
                "- Check if ingredients are compatible\n" +
                "- Organize kitchen yourself\n" +
                "- Cook AND manage\n\n" +
                "WITH MAVEN (Restaurant manager):\n" +
                "- You tell manager: \"I need tomatoes, version 2.0\"\n" +
                "- Manager orders from suppliers\n" +
                "- Manager organizes kitchen layout\n" +
                "- Manager ensures everything's in the right place\n" +
                "- You just focus on cooking (coding)\n\n" +
                "Maven handles the boring project management stuff!")
            .addTheory("Maven Project Structure",
                "Maven enforces a STANDARD structure:\n\n" +
                "my-project/\n" +
                "├── pom.xml                 (Project config file)\n" +
                "├── src/\n" +
                "│   ├── main/\n" +
                "│   │   ├── java/          (Your application code)\n" +
                "│   │   │   └── com/yourcompany/App.java\n" +
                "│   │   └── resources/     (Config files, properties)\n" +
                "│   └── test/\n" +
                "│       ├── java/          (Your test code)\n" +
                "│       │   └── com/yourcompany/AppTest.java\n" +
                "│       └── resources/     (Test resources)\n" +
                "└── target/                 (Compiled output - Maven creates this)\n\n" +
                "BENEFITS:\n" +
                "✓ Every Maven project looks the same\n" +
                "✓ New developers know where everything is\n" +
                "✓ Tools work automatically\n\n" +
                "This is \"Convention Over Configuration\"")
            .addTheory("The pom.xml File - Project Object Model",
                "pom.xml is the heart of a Maven project:\n\n" +
                "<project>\n" +
                "    <groupId>com.yourcompany</groupId>\n" +
                "    <artifactId>my-app</artifactId>\n" +
                "    <version>1.0.0</version>\n" +
                "    \n" +
                "    <properties>\n" +
                "        <maven.compiler.source>17</maven.compiler.source>\n" +
                "        <maven.compiler.target>17</maven.compiler.target>\n" +
                "    </properties>\n" +
                "    \n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.junit.jupiter</groupId>\n" +
                "            <artifactId>junit-jupiter</artifactId>\n" +
                "            <version>5.9.3</version>\n" +
                "            <scope>test</scope>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "</project>\n\n" +
                "KEY SECTIONS:\n" +
                "- groupId: Your organization (like package)\n" +
                "- artifactId: Project name\n" +
                "- version: Your project version\n" +
                "- properties: Configuration (Java version, etc.)\n" +
                "- dependencies: External libraries you need")
            .addTheory("Common Maven Commands",
                "Run these from command line in project folder:\n\n" +
                "mvn clean\n" +
                "  → Deletes target/ folder (compiled output)\n\n" +
                "mvn compile\n" +
                "  → Compiles your main source code\n\n" +
                "mvn test\n" +
                "  → Compiles and runs all tests\n\n" +
                "mvn package\n" +
                "  → Compiles, tests, and creates JAR file\n\n" +
                "mvn install\n" +
                "  → Package and install to local repository\n\n" +
                "mvn clean test\n" +
                "  → Clean then test (combines commands)\n\n" +
                "MOST COMMON WORKFLOW:\n" +
                "1. Write code\n" +
                "2. mvn clean test (verify tests pass)\n" +
                "3. mvn package (create JAR)\n" +
                "4. Deploy JAR to production")
            .addKeyPoint("Maven Dependency Scopes",
                "SCOPE controls WHEN a dependency is available:\n\n" +
                "<scope>compile</scope> (default)\n" +
                "  - Available everywhere (main code, tests, runtime)\n" +
                "  - Example: Gson JSON library\n\n" +
                "<scope>test</scope>\n" +
                "  - Only for tests, not in final JAR\n" +
                "  - Example: JUnit\n\n" +
                "<scope>provided</scope>\n" +
                "  - Available at compile-time, but server provides it at runtime\n" +
                "  - Example: Servlet API (server like Tomcat has it)\n\n" +
                "<scope>runtime</scope>\n" +
                "  - Not needed for compilation, only at runtime\n" +
                "  - Example: JDBC database driver\n\n" +
                "RULE: Use test scope for testing libraries (saves space in final JAR)")
            .addExample("Real pom.xml Example",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n" +
                "         http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>my-web-app</artifactId>\n" +
                "    <version>1.0-SNAPSHOT</version>\n\n" +
                "    <properties>\n" +
                "        <maven.compiler.source>17</maven.compiler.source>\n" +
                "        <maven.compiler.target>17</maven.compiler.target>\n" +
                "    </properties>\n\n" +
                "    <dependencies>\n" +
                "        <!-- JSON parsing -->\n" +
                "        <dependency>\n" +
                "            <groupId>com.google.code.gson</groupId>\n" +
                "            <artifactId>gson</artifactId>\n" +
                "            <version>2.10.1</version>\n" +
                "        </dependency>\n\n" +
                "        <!-- Testing -->\n" +
                "        <dependency>\n" +
                "            <groupId>org.junit.jupiter</groupId>\n" +
                "            <artifactId>junit-jupiter</artifactId>\n" +
                "            <version>5.9.3</version>\n" +
                "            <scope>test</scope>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "</project>")
            .addChallenge(createMavenQuiz())
            .addChallenge(createPomQuiz())
            .addChallenge(createCommandQuiz())
            .estimatedMinutes(35)
            .build();
    }

    private static Challenge createMavenQuiz() {
        return new Challenge.Builder("epoch-4-lesson-4-maven", "Understanding Maven", ChallengeType.MULTIPLE_CHOICE)
            .description("What is Maven's primary purpose?")
            .addMultipleChoiceOption("A) A code editor")
            .addMultipleChoiceOption("B) A build tool that manages dependencies and project lifecycle")
            .addMultipleChoiceOption("C) A version control system")
            .addMultipleChoiceOption("D) A database")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createPomQuiz() {
        return new Challenge.Builder("epoch-4-lesson-4-pom", "pom.xml File", ChallengeType.MULTIPLE_CHOICE)
            .description("Where should test-only dependencies like JUnit go in pom.xml?")
            .addMultipleChoiceOption("A) In <dependencies> with <scope>compile</scope>")
            .addMultipleChoiceOption("B) In <dependencies> with <scope>test</scope>")
            .addMultipleChoiceOption("C) In a separate test-pom.xml file")
            .addMultipleChoiceOption("D) Test dependencies aren't needed in pom.xml")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createCommandQuiz() {
        return new Challenge.Builder("epoch-4-lesson-4-commands", "Maven Commands", ChallengeType.MULTIPLE_CHOICE)
            .description("Which Maven command compiles your code, runs tests, and creates a JAR file?")
            .addMultipleChoiceOption("A) mvn compile")
            .addMultipleChoiceOption("B) mvn test")
            .addMultipleChoiceOption("C) mvn package")
            .addMultipleChoiceOption("D) mvn jar")
            .correctAnswer("C")
            .build();
    }
}
