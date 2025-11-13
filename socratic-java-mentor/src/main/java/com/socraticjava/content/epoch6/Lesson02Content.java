package com.socraticjava.content.epoch6;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 6.2: REST APIs and JSON
 */
public class Lesson02Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-6-lesson-2", "Lesson 6.2: REST APIs - The Standard for Web Services")
            .addTheory("What is a REST API?",
                "REST = Representational State Transfer\n\n" +
                "A REST API is a set of rules for building web services:\n\n" +
                "KEY PRINCIPLES:\n" +
                "1. Resources identified by URLs\n" +
                "2. HTTP methods define actions\n" +
                "3. Stateless (each request independent)\n" +
                "4. JSON for data exchange\n\n" +
                "EXAMPLE API:\n" +
                "GET    /api/users        → Get all users\n" +
                "GET    /api/users/123    → Get user with ID 123\n" +
                "POST   /api/users        → Create new user\n" +
                "PUT    /api/users/123    → Update user 123\n" +
                "DELETE /api/users/123    → Delete user 123")
            .addTheory("HTTP Methods - The Verbs",
                "GET - Retrieve data (read-only, safe):\n" +
                "GET /api/products\n" +
                "Response: [{ \"id\": 1, \"name\": \"Laptop\", \"price\": 999 }]\n\n" +
                "POST - Create new resource:\n" +
                "POST /api/products\n" +
                "Body: { \"name\": \"Mouse\", \"price\": 25 }\n" +
                "Response: { \"id\": 2, \"name\": \"Mouse\", \"price\": 25 }\n\n" +
                "PUT - Update entire resource:\n" +
                "PUT /api/products/2\n" +
                "Body: { \"name\": \"Wireless Mouse\", \"price\": 30 }\n\n" +
                "PATCH - Update partial resource:\n" +
                "PATCH /api/products/2\n" +
                "Body: { \"price\": 28 }\n\n" +
                "DELETE - Remove resource:\n" +
                "DELETE /api/products/2\n\n" +
                "IDEMPOTENT:\n" +
                "GET, PUT, DELETE are idempotent (same result if called multiple times)\n" +
                "POST is NOT idempotent (creates new resource each time)")
            .addTheory("JSON - JavaScript Object Notation",
                "JSON is the standard data format for REST APIs:\n\n" +
                "SIMPLE OBJECT:\n" +
                "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Alice\",\n" +
                "    \"age\": 20,\n" +
                "    \"active\": true\n" +
                "}\n\n" +
                "ARRAY:\n" +
                "[\"apple\", \"banana\", \"cherry\"]\n\n" +
                "NESTED OBJECTS:\n" +
                "{\n" +
                "    \"user\": {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Alice\"\n" +
                "    },\n" +
                "    \"orders\": [\n" +
                "        { \"id\": 101, \"total\": 50.00 },\n" +
                "        { \"id\": 102, \"total\": 75.50 }\n" +
                "    ]\n" +
                "}\n\n" +
                "DATA TYPES:\n" +
                "- String: \"hello\" (double quotes only)\n" +
                "- Number: 42, 3.14\n" +
                "- Boolean: true, false\n" +
                "- Null: null\n" +
                "- Object: { }\n" +
                "- Array: [ ]")
            .addTheory("HTTP Status Codes - Response Indicators",
                "2xx SUCCESS:\n" +
                "200 OK - Request succeeded\n" +
                "201 Created - Resource created (POST)\n" +
                "204 No Content - Success but no body (DELETE)\n\n" +
                "3xx REDIRECTION:\n" +
                "301 Moved Permanently\n" +
                "304 Not Modified (cached)\n\n" +
                "4xx CLIENT ERRORS:\n" +
                "400 Bad Request - Invalid data\n" +
                "401 Unauthorized - Need authentication\n" +
                "403 Forbidden - Authenticated but not allowed\n" +
                "404 Not Found - Resource doesn't exist\n" +
                "409 Conflict - Resource state conflict\n\n" +
                "5xx SERVER ERRORS:\n" +
                "500 Internal Server Error - Server crashed\n" +
                "502 Bad Gateway - Upstream server failed\n" +
                "503 Service Unavailable - Server overloaded\n\n" +
                "EXAMPLE:\n" +
                "GET /api/users/999\n" +
                "→ 404 Not Found (user doesn't exist)\n\n" +
                "POST /api/users (with invalid data)\n" +
                "→ 400 Bad Request")
            .addAnalogy("REST API is Like a Restaurant Menu",
                "MENU (API Documentation):\n" +
                "GET /api/dishes → View all dishes\n" +
                "POST /api/orders → Place an order\n" +
                "GET /api/orders/123 → Check order status\n\n" +
                "YOU (Client):\n" +
                "\"I'll have the pasta\" (POST /api/orders { \"dish\": \"pasta\" })\n\n" +
                "WAITER (HTTP):\n" +
                "Takes your order to kitchen\n\n" +
                "KITCHEN (Server):\n" +
                "Processes order, returns response\n\n" +
                "RESULT:\n" +
                "201 Created: { \"order_id\": 123, \"dish\": \"pasta\", \"status\": \"preparing\" }")
            .addKeyPoint("REST API Best Practices",
                "1. USE NOUNS, NOT VERBS IN URLS:\n" +
                "   ✓ GET /api/users\n" +
                "   ✗ GET /api/getUsers\n\n" +
                "2. USE PLURAL NOUNS:\n" +
                "   ✓ /api/users/123\n" +
                "   ✗ /api/user/123\n\n" +
                "3. USE HTTP METHODS FOR ACTIONS:\n" +
                "   Don't: POST /api/users/delete/123\n" +
                "   Do: DELETE /api/users/123\n\n" +
                "4. VERSION YOUR API:\n" +
                "   /api/v1/users\n" +
                "   /api/v2/users\n\n" +
                "5. RETURN APPROPRIATE STATUS CODES:\n" +
                "   Success → 200\n" +
                "   Created → 201\n" +
                "   Not Found → 404\n\n" +
                "6. USE QUERY PARAMETERS FOR FILTERING:\n" +
                "   /api/users?age=20&active=true")
            .addChallenge(createRESTQuiz())
            .addChallenge(createHTTPMethodQuiz())
            .addChallenge(createStatusCodeQuiz())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createRESTQuiz() {
        return new Challenge.Builder("epoch-6-lesson-2-rest", "Understanding REST", ChallengeType.MULTIPLE_CHOICE)
            .description("What does REST stand for?")
            .addMultipleChoiceOption("A) Really Easy Server Technology")
            .addMultipleChoiceOption("B) Representational State Transfer")
            .addMultipleChoiceOption("C) Remote Execution Service Tool")
            .addMultipleChoiceOption("D) Rapid Exchange Synchronous Transfer")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createHTTPMethodQuiz() {
        return new Challenge.Builder("epoch-6-lesson-2-methods", "HTTP Methods", ChallengeType.MULTIPLE_CHOICE)
            .description("Which HTTP method should you use to create a new user?")
            .addMultipleChoiceOption("A) GET")
            .addMultipleChoiceOption("B) PUT")
            .addMultipleChoiceOption("C) POST")
            .addMultipleChoiceOption("D) DELETE")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createStatusCodeQuiz() {
        return new Challenge.Builder("epoch-6-lesson-2-status", "HTTP Status Codes", ChallengeType.MULTIPLE_CHOICE)
            .description("What does HTTP status code 404 mean?")
            .addMultipleChoiceOption("A) Success")
            .addMultipleChoiceOption("B) Server error")
            .addMultipleChoiceOption("C) Resource not found")
            .addMultipleChoiceOption("D) Unauthorized")
            .correctAnswer("C")
            .build();
    }
}
