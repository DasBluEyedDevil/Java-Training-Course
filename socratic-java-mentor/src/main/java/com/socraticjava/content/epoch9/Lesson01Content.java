package com.socraticjava.content.epoch9;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 9.1: The Capstone Project
 */
public class Lesson01Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-9-lesson-1", "Lesson 9.1: Your Capstone Project")
            .addTheory("The Final Challenge: Build Something Real",
                "You've learned:\n" +
                "✓ Java fundamentals (variables, loops, methods)\n" +
                "✓ Object-oriented programming (classes, inheritance)\n" +
                "✓ Data structures (arrays, lists, maps)\n" +
                "✓ Testing and professional tools\n" +
                "✓ Databases and SQL\n" +
                "✓ Web APIs and HTTP\n" +
                "✓ Spring Boot framework\n" +
                "✓ Full-stack integration\n\n" +
                "Now it's time to APPLY everything to build a complete, portfolio-worthy project.")
            .addTheory("Project Options",
                "Choose ONE of these projects (or propose your own):\n\n" +
                "1. TASK MANAGEMENT SYSTEM\n" +
                "- Users can create accounts\n" +
                "- Create, update, delete tasks\n" +
                "- Assign priorities and due dates\n" +
                "- Filter and search tasks\n" +
                "- Tech: Spring Boot API + Database + Simple HTML frontend\n\n" +
                "2. BLOG PLATFORM\n" +
                "- Users can register and login\n" +
                "- Create, edit, delete blog posts\n" +
                "- Comment on posts\n" +
                "- Like/unlike posts\n" +
                "- Tech: Spring Boot + MySQL + REST API\n\n" +
                "3. E-COMMERCE PRODUCT CATALOG\n" +
                "- Browse products\n" +
                "- Search and filter\n" +
                "- Shopping cart\n" +
                "- Order history\n" +
                "- Tech: Spring Boot + Database + JSON API\n\n" +
                "4. YOUR OWN IDEA!\n" +
                "- Propose a project that interests you\n" +
                "- Must include: API, database, authentication\n" +
                "- Discuss with mentor/community")
            .addTheory("Project Requirements",
                "Your capstone MUST include:\n\n" +
                "✓ Backend: Spring Boot REST API\n" +
                "✓ Database: Persistent storage with JPA\n" +
                "✓ CRUD Operations: Create, Read, Update, Delete\n" +
                "✓ Object-Oriented Design: Proper class structure\n" +
                "✓ Testing: Unit tests for key methods\n" +
                "✓ Error Handling: Graceful failure messages\n" +
                "✓ Documentation: README with setup instructions\n" +
                "✓ Git: Version control with meaningful commits\n\n" +
                "BONUS (Optional):\n" +
                "- Frontend interface (HTML/JS)\n" +
                "- User authentication\n" +
                "- Deployment to cloud (Heroku, AWS)\n" +
                "- Advanced features specific to your app")
            .addKeyPoint("The Development Process",
                "Step 1: PLAN (1-2 days)\n" +
                "- Sketch your database schema\n" +
                "- List API endpoints\n" +
                "- Define your models/entities\n\n" +
                "Step 2: BUILD BACKEND (5-7 days)\n" +
                "- Create entities and repositories\n" +
                "- Build REST controllers\n" +
                "- Test with Postman/curl\n\n" +
                "Step 3: ADD FRONTEND (3-4 days)\n" +
                "- Basic HTML forms\n" +
                "- Fetch API calls\n" +
                "- Display data\n\n" +
                "Step 4: TEST & REFINE (2-3 days)\n" +
                "- Fix bugs\n" +
                "- Add error handling\n" +
                "- Write tests\n\n" +
                "Step 5: DOCUMENT & DEPLOY (1-2 days)\n" +
                "- Write README\n" +
                "- Create demo video\n" +
                "- Deploy (optional)\n\n" +
                "Total: 2-3 weeks for a solid project")
            .addChallenge(createCapstoneQuiz())
            .estimatedMinutes(60)
            .build();
    }

    private static Challenge createCapstoneQuiz() {
        return new Challenge.Builder("epoch-9-lesson-1-quiz", "Capstone Readiness", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the PRIMARY goal of the capstone project?")
            .addMultipleChoiceOption("A) To learn a new programming language")
            .addMultipleChoiceOption("B) To apply everything you've learned in a real, portfolio-worthy project")
            .addMultipleChoiceOption("C) To pass a certification exam")
            .addMultipleChoiceOption("D) To copy an existing application")
            .correctAnswer("B")
            .build();
    }
}
