package com.socraticjava.content.epoch8;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 8.1: Frontend Meets Backend
 */
public class Lesson01Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-8-lesson-1", "Lesson 8.1: Connecting Frontend to Your API")
            .addTheory("The Problem: Users Need Interfaces",
                "Your Spring Boot API is amazing:\n" +
                "- GET /api/users returns user data\n" +
                "- POST /api/users creates users\n\n" +
                "But normal users don't type URLs and read JSON!\n\n" +
                "They need:\n" +
                "- Buttons to click\n" +
                "- Forms to fill out\n" +
                "- Visual displays\n\n" +
                "This is where FRONTEND comes in:\n" +
                "- HTML (structure)\n" +
                "- CSS (styling)\n" +
                "- JavaScript (interactivity)")
            .addAnalogy("Frontend is the Storefront, Backend is the Warehouse",
                "BACKEND (Your Java API):\n" +
                "= Warehouse\n" +
                "- Stores all products (data)\n" +
                "- Processes orders (business logic)\n" +
                "- Customers never see it directly\n\n" +
                "FRONTEND (HTML/JS/React):\n" +
                "= Storefront\n" +
                "- Beautiful display\n" +
                "- Shopping cart UI\n" +
                "- Checkout forms\n" +
                "- What customers interact with\n\n" +
                "They communicate:\n" +
                "Customer clicks \"Buy\" button (frontend)\n" +
                "→ Sends request to API (backend)\n" +
                "→ Backend processes order\n" +
                "→ Sends confirmation (frontend displays it)")
            .addTheory("Fetching Data from JavaScript",
                "Your API endpoint:\n" +
                "GET /api/users/1 → {\"name\": \"Alice\", \"age\": 20}\n\n" +
                "JavaScript code to call it:\n\n" +
                "fetch('http://localhost:8080/api/users/1')\n" +
                "  .then(response => response.json())\n" +
                "  .then(user => {\n" +
                "    console.log(user.name);  // \"Alice\"\n" +
                "    document.getElementById('username').textContent = user.name;\n" +
                "  });\n\n" +
                "This:\n" +
                "1. Sends HTTP GET request to your API\n" +
                "2. Receives JSON response\n" +
                "3. Displays it on the webpage\n\n" +
                "Full-stack = Backend (Java) + Frontend (HTML/JS) working together!")
            .addChallenge(createFrontendQuiz())
            .estimatedMinutes(30)
            .build();
    }

    private static Challenge createFrontendQuiz() {
        return new Challenge.Builder("epoch-8-lesson-1-quiz", "Understanding Full-Stack", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the role of the frontend in a full-stack application?")
            .addMultipleChoiceOption("A) Store data in a database")
            .addMultipleChoiceOption("B) Provide the user interface that communicates with the backend API")
            .addMultipleChoiceOption("C) Process business logic")
            .addMultipleChoiceOption("D) Replace the need for a backend")
            .correctAnswer("B")
            .build();
    }
}
