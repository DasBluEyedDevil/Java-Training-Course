package com.socraticjava.content.epoch7;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 7.1: Introduction to Spring Boot
 */
public class Lesson01Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-7-lesson-1", "Lesson 7.1: Why Spring Boot?")
            .addTheory("The Problem: Building Web Apps is Complex",
                "To build a web API manually, you need to:\n" +
                "- Set up a web server\n" +
                "- Configure routing (which URL goes where)\n" +
                "- Handle HTTP requests and responses\n" +
                "- Connect to a database\n" +
                "- Manage security\n" +
                "- Handle errors\n\n" +
                "This is HUNDREDS of lines of configuration code before you even start building features!\n\n" +
                "Spring Boot solves this: \"Convention over Configuration\"\n" +
                "- Auto-configures everything\n" +
                "- Provides sensible defaults\n" +
                "- You focus on business logic, not plumbing")
            .addAnalogy("Spring Boot is Like IKEA Furniture",
                "Building a web app from scratch:\n" +
                "= Chopping down trees and making furniture from raw wood\n" +
                "- Extremely difficult\n" +
                "- Takes forever\n" +
                "- Easy to mess up\n\n" +
                "Using Spring Boot:\n" +
                "= IKEA pre-packaged furniture\n" +
                "- All pieces provided\n" +
                "- Clear instructions\n" +
                "- Just assemble (write your business logic)\n\n" +
                "Spring Boot gives you the framework, you add your app's unique features.")
            .addTheory("Your First Spring Boot Endpoint",
                "A REST API endpoint in Spring Boot:\n\n" +
                "@RestController\n" +
                "public class HelloController {\n" +
                "    \n" +
                "    @GetMapping(\"/hello\")\n" +
                "    public String sayHello() {\n" +
                "        return \"Hello, World!\";\n" +
                "    }\n" +
                "}\n\n" +
                "That's it! Spring Boot handles:\n" +
                "- Starting a web server\n" +
                "- Routing GET /hello to this method\n" +
                "- Converting return value to HTTP response\n\n" +
                "Visit http://localhost:8080/hello → See \"Hello, World!\"")
            .addKeyPoint("Key Spring Boot Concepts",
                "@RestController - Marks a class as a web API controller\n" +
                "@GetMapping - Maps HTTP GET requests to a method\n" +
                "@PostMapping - Maps HTTP POST requests\n" +
                "@RequestBody - Reads JSON from request\n" +
                "@PathVariable - Extracts values from URL\n\n" +
                "Spring Boot's magic: Annotations do the work!")
            .addChallenge(createSpringQuiz())
            .estimatedMinutes(35)
            .build();
    }

    private static Challenge createSpringQuiz() {
        return new Challenge.Builder("epoch-7-lesson-1-quiz", "Understanding Spring Boot", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the main benefit of using Spring Boot?")
            .addMultipleChoiceOption("A) It makes Java faster")
            .addMultipleChoiceOption("B) It auto-configures web applications so you can focus on features")
            .addMultipleChoiceOption("C) It replaces Java with a new language")
            .addMultipleChoiceOption("D) It's only for desktop applications")
            .correctAnswer("B")
            .build();
    }
}
