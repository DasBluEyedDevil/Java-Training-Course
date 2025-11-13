package com.socraticjava.content.epoch6;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 6.1: How the Web Works
 */
public class Lesson01Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-6-lesson-1", "Lesson 6.1: How Does the Web Actually Work?")
            .addTheory("The Problem: Programs That Talk Over the Internet",
                "So far, your programs run on ONE computer.\n\n" +
                "But modern apps need to:\n" +
                "- Let users access from anywhere (phone, laptop, tablet)\n" +
                "- Share data between users\n" +
                "- Update in real-time\n\n" +
                "Example: Instagram\n" +
                "- You post a photo from your phone\n" +
                "- Your friend sees it on their laptop\n" +
                "- How did the photo get there?\n\n" +
                "This requires CLIENT-SERVER architecture.")
            .addAnalogy("The Web is Like a Restaurant",
                "YOU (Client):\n" +
                "- Sit at a table\n" +
                "- Look at menu\n" +
                "- Order food: \"I'll have the pasta\"\n\n" +
                "KITCHEN (Server):\n" +
                "- Receives your order\n" +
                "- Prepares the food\n" +
                "- Sends it back to you\n\n" +
                "WAITER (HTTP Protocol):\n" +
                "- Carries messages between you and kitchen\n" +
                "- Speaks a specific language (HTTP)\n\n" +
                "In web terms:\n" +
                "- Your browser = Client (customer)\n" +
                "- Java server = Server (kitchen)\n" +
                "- HTTP = Protocol (waiter's language)")
            .addTheory("HTTP Requests and Responses",
                "When you visit a website:\n\n" +
                "1. CLIENT SENDS REQUEST:\n" +
                "GET /api/users/123 HTTP/1.1\n" +
                "Host: example.com\n\n" +
                "This means: \"Give me information about user 123\"\n\n" +
                "2. SERVER PROCESSES REQUEST:\n" +
                "- Looks up user 123 in database\n" +
                "- Prepares the data\n\n" +
                "3. SERVER SENDS RESPONSE:\n" +
                "HTTP/1.1 200 OK\n" +
                "Content-Type: application/json\n" +
                "{\"name\": \"Alice\", \"age\": 20}\n\n" +
                "Common HTTP methods:\n" +
                "- GET: Retrieve data (\"show me\")\n" +
                "- POST: Create data (\"save this\")\n" +
                "- PUT: Update data (\"change this\")\n" +
                "- DELETE: Remove data (\"delete this\")")
            .addChallenge(createWebQuiz())
            .estimatedMinutes(30)
            .build();
    }

    private static Challenge createWebQuiz() {
        return new Challenge.Builder("epoch-6-lesson-1-quiz", "Understanding Web Architecture", ChallengeType.MULTIPLE_CHOICE)
            .description("In the client-server model, what is the browser's role?")
            .addMultipleChoiceOption("A) The server that stores data")
            .addMultipleChoiceOption("B) The client that makes requests")
            .addMultipleChoiceOption("C) The database")
            .addMultipleChoiceOption("D) The protocol")
            .correctAnswer("B")
            .build();
    }
}
