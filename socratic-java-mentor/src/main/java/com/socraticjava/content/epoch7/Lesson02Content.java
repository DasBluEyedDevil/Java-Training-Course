package com.socraticjava.content.epoch7;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 7.2: Building REST APIs with Spring Boot
 */
public class Lesson02Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-7-lesson-2", "Lesson 7.2: REST Controllers - Building Your API")
            .addTheory("Creating a Complete REST API",
                "Let's build a User API with full CRUD operations:\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/users\")\n" +
                "public class UserController {\n" +
                "    \n" +
                "    private List<User> users = new ArrayList<>();\n" +
                "    \n" +
                "    // GET all users\n" +
                "    @GetMapping\n" +
                "    public List<User> getAllUsers() {\n" +
                "        return users;\n" +
                "    }\n" +
                "    \n" +
                "    // GET single user by ID\n" +
                "    @GetMapping(\"/{id}\")\n" +
                "    public User getUserById(@PathVariable Long id) {\n" +
                "        return users.stream()\n" +
                "            .filter(u -> u.getId().equals(id))\n" +
                "            .findFirst()\n" +
                "            .orElse(null);\n" +
                "    }\n" +
                "    \n" +
                "    // POST create new user\n" +
                "    @PostMapping\n" +
                "    public User createUser(@RequestBody User user) {\n" +
                "        users.add(user);\n" +
                "        return user;\n" +
                "    }\n" +
                "    \n" +
                "    // PUT update user\n" +
                "    @PutMapping(\"/{id}\")\n" +
                "    public User updateUser(@PathVariable Long id, @RequestBody User updated) {\n" +
                "        // Find and update user\n" +
                "        return updated;\n" +
                "    }\n" +
                "    \n" +
                "    // DELETE user\n" +
                "    @DeleteMapping(\"/{id}\")\n" +
                "    public void deleteUser(@PathVariable Long id) {\n" +
                "        users.removeIf(u -> u.getId().equals(id));\n" +
                "    }\n" +
                "}")
            .addTheory("Key Annotations Explained",
                "@RestController:\n" +
                "- Combines @Controller + @ResponseBody\n" +
                "- Returns data (JSON) instead of views (HTML)\n" +
                "- Every method returns data, not page names\n\n" +
                "@RequestMapping(\"/api/users\"):\n" +
                "- Base URL for all methods in this controller\n" +
                "- Methods add to this path\n\n" +
                "@GetMapping, @PostMapping, etc.:\n" +
                "- Shorthand for @RequestMapping(method = GET/POST)\n" +
                "- Maps HTTP method to Java method\n\n" +
                "@PathVariable:\n" +
                "- Extracts value from URL path\n" +
                "- /api/users/{id} → @PathVariable Long id\n" +
                "- /api/users/123 → id = 123\n\n" +
                "@RequestBody:\n" +
                "- Reads JSON from request body\n" +
                "- Automatically converts JSON to Java object\n" +
                "- POST/PUT requests with data\n\n" +
                "@RequestParam:\n" +
                "- Extracts query parameters\n" +
                "- /api/users?age=20 → @RequestParam int age")
            .addTheory("ResponseEntity - Better Control",
                "Return ResponseEntity for more control over HTTP response:\n\n" +
                "@GetMapping(\"/{id}\")\n" +
                "public ResponseEntity<User> getUserById(@PathVariable Long id) {\n" +
                "    User user = findUserById(id);\n" +
                "    \n" +
                "    if (user == null) {\n" +
                "        return ResponseEntity.notFound().build();  // 404\n" +
                "    }\n" +
                "    \n" +
                "    return ResponseEntity.ok(user);  // 200 with body\n" +
                "}\n\n" +
                "@PostMapping\n" +
                "public ResponseEntity<User> createUser(@RequestBody User user) {\n" +
                "    User saved = userService.save(user);\n" +
                "    return ResponseEntity\n" +
                "        .status(HttpStatus.CREATED)  // 201\n" +
                "        .body(saved);\n" +
                "}\n\n" +
                "BENEFITS:\n" +
                "- Set custom status codes\n" +
                "- Add headers\n" +
                "- Full control over response")
            .addTheory("Exception Handling",
                "Handle errors gracefully:\n\n" +
                "@RestControllerAdvice\n" +
                "public class GlobalExceptionHandler {\n" +
                "    \n" +
                "    @ExceptionHandler(UserNotFoundException.class)\n" +
                "    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {\n" +
                "        ErrorResponse error = new ErrorResponse(\n" +
                "            \"User not found\",\n" +
                "            ex.getMessage()\n" +
                "        );\n" +
                "        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);\n" +
                "    }\n" +
                "    \n" +
                "    @ExceptionHandler(IllegalArgumentException.class)\n" +
                "    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {\n" +
                "        ErrorResponse error = new ErrorResponse(\n" +
                "            \"Invalid request\",\n" +
                "            ex.getMessage()\n" +
                "        );\n" +
                "        return ResponseEntity.badRequest().body(error);\n" +
                "    }\n" +
                "}\n\n" +
                "When controller throws UserNotFoundException:\n" +
                "→ Handler catches it\n" +
                "→ Returns 404 with error message")
            .addKeyPoint("Validation with Spring Boot",
                "Use Bean Validation annotations:\n\n" +
                "public class User {\n" +
                "    @NotNull(message = \"ID is required\")\n" +
                "    private Long id;\n" +
                "    \n" +
                "    @NotBlank(message = \"Name cannot be empty\")\n" +
                "    @Size(min = 2, max = 100, message = \"Name must be 2-100 characters\")\n" +
                "    private String name;\n" +
                "    \n" +
                "    @Min(value = 18, message = \"Must be at least 18\")\n" +
                "    private int age;\n" +
                "    \n" +
                "    @Email(message = \"Invalid email format\")\n" +
                "    private String email;\n" +
                "}\n\n" +
                "In controller:\n" +
                "@PostMapping\n" +
                "public ResponseEntity<User> createUser(@Valid @RequestBody User user) {\n" +
                "    // If validation fails, Spring returns 400 automatically\n" +
                "    return ResponseEntity.ok(userService.save(user));\n" +
                "}\n\n" +
                "@Valid triggers validation!\n" +
                "Invalid request → 400 Bad Request with error details")
            .addChallenge(createControllerQuiz())
            .addChallenge(createAnnotationsQuiz())
            .addChallenge(createValidationQuiz())
            .estimatedMinutes(45)
            .build();
    }

    private static Challenge createControllerQuiz() {
        return new Challenge.Builder("epoch-7-lesson-2-controller", "REST Controller", ChallengeType.MULTIPLE_CHOICE)
            .description("What does @RestController do?")
            .addMultipleChoiceOption("A) Creates a database connection")
            .addMultipleChoiceOption("B) Marks a class as a REST API controller that returns data (JSON)")
            .addMultipleChoiceOption("C) Handles HTML templates")
            .addMultipleChoiceOption("D) Configures security")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createAnnotationsQuiz() {
        return new Challenge.Builder("epoch-7-lesson-2-annotations", "Understanding Annotations", ChallengeType.MULTIPLE_CHOICE)
            .description("What does @PathVariable extract?")
            .addMultipleChoiceOption("A) Query parameters (?key=value)")
            .addMultipleChoiceOption("B) Request headers")
            .addMultipleChoiceOption("C) Values from the URL path (/users/{id})")
            .addMultipleChoiceOption("D) Request body")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createValidationQuiz() {
        return new Challenge.Builder("epoch-7-lesson-2-validation", "Bean Validation", ChallengeType.MULTIPLE_CHOICE)
            .description("Which annotation triggers validation in Spring Boot?")
            .addMultipleChoiceOption("A) @Validated")
            .addMultipleChoiceOption("B) @Valid")
            .addMultipleChoiceOption("C) @Check")
            .addMultipleChoiceOption("D) @Verify")
            .correctAnswer("B")
            .build();
    }
}
