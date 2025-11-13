package com.socraticjava.content.epoch6;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 6.3: Making HTTP Requests from Java
 */
public class Lesson03Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-6-lesson-3", "Lesson 6.3: HttpClient - Calling APIs from Java")
            .addTheory("The Problem: Java Needs to Call External APIs",
                "Modern applications don't exist in isolation:\n\n" +
                "- Weather app → calls weather API\n" +
                "- E-commerce → calls payment API\n" +
                "- Social media → calls authentication API\n\n" +
                "Your Java code needs to make HTTP requests to external services.\n\n" +
                "Java 11+ provides: HttpClient (modern, built-in)\n" +
                "Before Java 11: HttpURLConnection (old, clunky)\n" +
                "Popular library: Apache HttpClient\n\n" +
                "We'll use Java's modern HttpClient!")
            .addTheory("Making a GET Request",
                "import java.net.URI;\n" +
                "import java.net.http.HttpClient;\n" +
                "import java.net.http.HttpRequest;\n" +
                "import java.net.http.HttpResponse;\n\n" +
                "public class APIExample {\n" +
                "    public static void main(String[] args) throws Exception {\n" +
                "        // 1. Create HttpClient\n" +
                "        HttpClient client = HttpClient.newHttpClient();\n" +
                "        \n" +
                "        // 2. Build request\n" +
                "        HttpRequest request = HttpRequest.newBuilder()\n" +
                "            .uri(URI.create(\"https://api.example.com/users/123\"))\n" +
                "            .GET()\n" +
                "            .build();\n" +
                "        \n" +
                "        // 3. Send request and get response\n" +
                "        HttpResponse<String> response = client.send(\n" +
                "            request,\n" +
                "            HttpResponse.BodyHandlers.ofString()\n" +
                "        );\n" +
                "        \n" +
                "        // 4. Process response\n" +
                "        System.out.println(\"Status: \" + response.statusCode());\n" +
                "        System.out.println(\"Body: \" + response.body());\n" +
                "    }\n" +
                "}")
            .addTheory("POST Request with JSON Body",
                "import com.google.gson.Gson;\n\n" +
                "// Create data object\n" +
                "class User {\n" +
                "    String name;\n" +
                "    int age;\n" +
                "    \n" +
                "    User(String name, int age) {\n" +
                "        this.name = name;\n" +
                "        this.age = age;\n" +
                "    }\n" +
                "}\n\n" +
                "// Convert to JSON\n" +
                "Gson gson = new Gson();\n" +
                "User newUser = new User(\"Alice\", 20);\n" +
                "String json = gson.toJson(newUser);  // {\"name\":\"Alice\",\"age\":20}\n\n" +
                "// Build POST request\n" +
                "HttpRequest request = HttpRequest.newBuilder()\n" +
                "    .uri(URI.create(\"https://api.example.com/users\"))\n" +
                "    .header(\"Content-Type\", \"application/json\")\n" +
                "    .POST(HttpRequest.BodyPublishers.ofString(json))\n" +
                "    .build();\n\n" +
                "// Send request\n" +
                "HttpResponse<String> response = client.send(\n" +
                "    request,\n" +
                "    HttpResponse.BodyHandlers.ofString()\n" +
                ");\n\n" +
                "// Parse response\n" +
                "if (response.statusCode() == 201) {\n" +
                "    User created = gson.fromJson(response.body(), User.class);\n" +
                "    System.out.println(\"Created user: \" + created.name);\n" +
                "}")
            .addTheory("Handling HTTP Headers",
                "COMMON HEADERS:\n\n" +
                "Content-Type: What format is the body?\n" +
                "- application/json (JSON data)\n" +
                "- application/x-www-form-urlencoded (form data)\n" +
                "- text/html (HTML)\n\n" +
                "Authorization: Authentication token\n" +
                "- Bearer <token>\n" +
                "- Basic <base64-credentials>\n\n" +
                "Accept: What format do you want back?\n" +
                "- application/json\n\n" +
                "SETTING HEADERS:\n" +
                "HttpRequest request = HttpRequest.newBuilder()\n" +
                "    .uri(URI.create(url))\n" +
                "    .header(\"Content-Type\", \"application/json\")\n" +
                "    .header(\"Authorization\", \"Bearer abc123xyz\")\n" +
                "    .header(\"Accept\", \"application/json\")\n" +
                "    .GET()\n" +
                "    .build();\n\n" +
                "READING RESPONSE HEADERS:\n" +
                "String contentType = response.headers()\n" +
                "    .firstValue(\"content-type\")\n" +
                "    .orElse(\"unknown\");")
            .addKeyPoint("Error Handling for HTTP Requests",
                "ALWAYS handle errors:\n\n" +
                "try {\n" +
                "    HttpResponse<String> response = client.send(request, ...);\n" +
                "    \n" +
                "    // Check status code\n" +
                "    if (response.statusCode() == 200) {\n" +
                "        // Success!\n" +
                "        String body = response.body();\n" +
                "    } else if (response.statusCode() == 404) {\n" +
                "        System.err.println(\"Resource not found\");\n" +
                "    } else if (response.statusCode() >= 500) {\n" +
                "        System.err.println(\"Server error: \" + response.statusCode());\n" +
                "    } else {\n" +
                "        System.err.println(\"Error: \" + response.statusCode());\n" +
                "    }\n" +
                "    \n" +
                "} catch (IOException e) {\n" +
                "    // Network error (can't reach server)\n" +
                "    System.err.println(\"Network error: \" + e.getMessage());\n" +
                "} catch (InterruptedException e) {\n" +
                "    // Request was interrupted\n" +
                "    System.err.println(\"Request interrupted\");\n" +
                "}\n\n" +
                "COMMON ERRORS:\n" +
                "- IOException: Network issues, timeout\n" +
                "- 4xx codes: Client problems (bad request, not found)\n" +
                "- 5xx codes: Server problems (crash, overload)")
            .addChallenge(createHttpClientQuiz())
            .addChallenge(createHeadersQuiz())
            .addChallenge(createErrorHandlingQuiz())
            .estimatedMinutes(40)
            .build();
    }

    private static Challenge createHttpClientQuiz() {
        return new Challenge.Builder("epoch-6-lesson-3-client", "HttpClient Basics", ChallengeType.MULTIPLE_CHOICE)
            .description("Which class do you use to make HTTP requests in Java 11+?")
            .addMultipleChoiceOption("A) HttpRequest")
            .addMultipleChoiceOption("B) HttpClient")
            .addMultipleChoiceOption("C) URLConnection")
            .addMultipleChoiceOption("D) WebClient")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createHeadersQuiz() {
        return new Challenge.Builder("epoch-6-lesson-3-headers", "HTTP Headers", ChallengeType.MULTIPLE_CHOICE)
            .description("Which header tells the server what format your request body is in?")
            .addMultipleChoiceOption("A) Accept")
            .addMultipleChoiceOption("B) Authorization")
            .addMultipleChoiceOption("C) Content-Type")
            .addMultipleChoiceOption("D) Content-Length")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createErrorHandlingQuiz() {
        return new Challenge.Builder("epoch-6-lesson-3-errors", "Error Handling", ChallengeType.MULTIPLE_CHOICE)
            .description("What should you catch when making HTTP requests?")
            .addMultipleChoiceOption("A) Only NullPointerException")
            .addMultipleChoiceOption("B) IOException and InterruptedException")
            .addMultipleChoiceOption("C) Only RuntimeException")
            .addMultipleChoiceOption("D) No exceptions needed")
            .correctAnswer("B")
            .build();
    }
}
